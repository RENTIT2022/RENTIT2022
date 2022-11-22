package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.BookingRegistrationDto;
import kg.neobis.rentit.dto.BookingRequestDto;
import kg.neobis.rentit.dto.UserBookingDto;
import kg.neobis.rentit.entity.*;
import kg.neobis.rentit.entity.Calendar;
import kg.neobis.rentit.enums.BookingStatus;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final BookingRepository bookingRepository;

    private final CalendarRepository calendarRepository;

    private final ImageProductRepository imageProductRepository;


    public List<UserBookingDto> getUserBookings() {
        User user = getAuthentication();

        return user.getBookings().stream()
                .map(
                        entity -> {
                            UserBookingDto dto = new UserBookingDto();

                            dto.setBookingId(entity.getId());
                            dto.setStatus(entity.getBookingStatus().getStatus());
                            dto.setProductId(entity.getProduct().getId());
                            dto.setPrice(entity.getProduct().getPrice());
                            dto.setProductTitle(entity.getProduct().getTitle());
                            dto.setMainImageUrl(returnMainImageUrl(entity));

                            return dto;
                        }
                )
                .collect(Collectors.toList());
    }

    public List<BookingRequestDto> getBookingRequests() {
        User user = getAuthentication();

        Set<Product> products = user.getProducts();

        List<Booking> bookings = bookingRepository.getBookingRequests(
                products.stream()
                        .map(Product::getId)
                        .collect(Collectors.toList())
        );

        return bookings.stream()
                .map(entity -> {
                    BookingRequestDto dto = new BookingRequestDto();

                    dto.setBookingId(entity.getId());
                    dto.setClientId(entity.getUser().getId());
                    dto.setClientName(entity.getUser().getLastName() + " " + entity.getUser().getFirstName());
                    dto.setPrice(entity.getProduct().getPrice());
                    dto.setProductTitle(entity.getProduct().getTitle());
                    dto.setProductId(entity.getProduct().getId());
                    dto.setBookDateFrom(entity.getDateFrom());
                    dto.setBookDateTill(entity.getDateTill());
                    dto.setMainImageUrl(returnMainImageUrl(entity));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    private String returnMainImageUrl(Booking entity) {
        ImageProduct imageProduct =
                imageProductRepository.findByProductIdAndOrderNumber(entity.getProduct().getId(),
                        (byte) 1);

        if (imageProduct != null) {
            Image image = imageProduct.getImage();
            if(image != null && image.getUrl().startsWith("http")) {
                return image.getUrl().replace("http", "https");
            }
        }

        return "";
    }

    public BookingRegistrationDto bookProduct(BookingRegistrationDto dto) {
        LocalDate from = dto.getDateFrom();
        LocalDate till = dto.getDateTill();

        for (Booking entity : bookingRepository.findAll()) {
            LocalDate efrom = entity.getDateFrom();
            LocalDate etill = entity.getDateTill();
            if(entity.getBookingStatus().equals(BookingStatus.ACCEPTED)) {
                if ((from.compareTo(efrom) >= 0 && from.compareTo(etill) < 0) ||
                        (from.compareTo(efrom) > 0 && from.compareTo(etill) <= 0) ||
                        (till.compareTo(efrom) >= 0 && till.compareTo(etill) < 0) ||
                        (till.compareTo(efrom) > 0 && till.compareTo(etill) <= 0)) {
                    throw new AlreadyExistException("The given dates are already booked.");
                }
            }
        }

        User user = getAuthentication();

        if (user == null) {
            throw new ResourceNotFoundException("User is not authenticated");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: " + dto.getProductId())
                );

        if(user.getId().equals(product.getUser().getId())) {
            throw new BadRequestException("You cannot add your product to favorites.");
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setProduct(product);
        booking.setDateFrom(dto.getDateFrom());
        booking.setDateTill(dto.getDateTill());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTotalPrice(dto.getTotalPrice());
        booking.setBookingDateTime(LocalDateTime.now());

        bookingRepository.save(booking);

        return dto;
    }

    public Map<String, List<Integer>> getProductSchedule(Long productId, int year, int month) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product was not found with ID: " + productId)
                );

        List<Calendar> calendar = calendarRepository
                .getCalendarByProductIdAndYearAndMonth(productId, year, month);

        int days = YearMonth.of(year, month).lengthOfMonth();

        int[] results = new int[days + 1];

        for(Calendar entity: calendar) {
            int d = entity.getDate().getDayOfMonth();

            if(entity.isBooked()) {
                results[d] = 1;
            } else if(!entity.isBooked()) {
                results[d] = -1;
            }
        }

        Map<String, List<Integer>> schedule = new HashMap<>();

        schedule.put("booked", new ArrayList<>());
        schedule.put("free", new ArrayList<>());
        schedule.put("no booking", new ArrayList<>());

        for(int i = 1; i < results.length; i++) {
            if(results[i] == 1) {
                schedule.get("booked").add(i);
            } else if(results[i] == -1) {
                schedule.get("free").add(i);
            } else {
                schedule.get("no booking").add(i);
            }
        }

        return schedule;
    }

    public String cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Booking was not found with ID: " + bookingId)
                );

        bookingRepository.deleteById(booking.getId());

        return "The booking was canceled.";
    }

    public String acceptBookingRequest(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Booking was not found with ID: " + bookingId)
                );

        booking.setBookingStatus(BookingStatus.ACCEPTED);

        bookingRepository.save(booking);

        List<Calendar> calendar = booking.getProduct().getCalendars();

        for(Calendar entity: calendar) {
            if((entity.getDate().isEqual(booking.getDateFrom()) ||
            entity.getDate().isEqual(booking.getDateTill())) ||
            entity.getDate().isAfter(booking.getDateFrom()) && entity.getDate().isBefore(booking.getDateTill())) {
                entity.setBooked(true);
                entity.setUser(booking.getUser());
            }
            calendarRepository.save(entity);
        }

        return "The booking was accepted.";
    }

    public String rejectBookingRequest(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Booking was not found with ID: " + bookingId)
                );

        booking.setBookingStatus(BookingStatus.REJECTED);

        bookingRepository.save(booking);

        return "The booking was rejected.";
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName());
    }

}
