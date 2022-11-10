package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.BookingRegistrationDto;
import kg.neobis.rentit.dto.BookingRequestDto;
import kg.neobis.rentit.dto.UserBookingDto;
import kg.neobis.rentit.entity.Booking;
import kg.neobis.rentit.entity.ImageProduct;
import kg.neobis.rentit.entity.Product;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.enums.BookingStatus;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.BookingRepository;
import kg.neobis.rentit.repository.ProductRepository;
import kg.neobis.rentit.repository.UserRepository;
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

                            List<ImageProduct> imageProductList = entity.getProduct().getImageProduct();

                            for (ImageProduct imageProduct : imageProductList) {

                                if (imageProduct.getOrderNumber() == 0) {
                                    dto.setMainImageUrl(imageProduct.getImage().getUrl());
                                    break;
                                }
                            }

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
                    dto.setPrice(entity.getProduct().getPrice());
                    dto.setProductTitle(entity.getProduct().getTitle());
                    dto.setProductId(entity.getProduct().getId());

                    List<ImageProduct> imageProductList = entity.getProduct().getImageProduct();

                    for (ImageProduct imageProduct : imageProductList) {

                        if (imageProduct.getOrderNumber() == 0) {
                            dto.setMainImageUrl(imageProduct.getImage().getUrl());
                            break;
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public BookingRegistrationDto bookProduct(BookingRegistrationDto dto) {
        LocalDate from = dto.getDateFrom();
        LocalDate till = dto.getDateTill();

        for (Booking entity : bookingRepository.findAll()) {
            LocalDate efrom = entity.getDateFrom();
            LocalDate etill = entity.getDateTill();
            if((from.compareTo(efrom) >= 0 && from.compareTo(etill) < 0) ||
                    (from.compareTo(efrom) > 0 && from.compareTo(etill) <= 0) ||
                    (till.compareTo(efrom) >= 0 && till.compareTo(etill) < 0) ||
                    (till.compareTo(efrom) > 0 && till.compareTo(etill) <= 0)) {
                throw new AlreadyExistException("The given dates are already booked.");
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
        List<Booking> bookings = bookingRepository.findAllByProductIdOrderByDateFrom(productId);

        Map<String, List<Integer>> monthSchedule = new HashMap<>();

        for (int i = 1; i <= YearMonth.of(year, month).lengthOfMonth(); i++) {
            LocalDate today = LocalDate.now();

            LocalDate date = LocalDate.of(year, month, i);

            if (today.compareTo(date) >= 0) {

                boolean added = false;

                for (Booking booking : bookings) {
                    LocalDate from = booking.getDateFrom();
                    LocalDate till = booking.getDateTill();

                    if ((date.compareTo(from) >= 0 && date.compareTo(till) < 0) ||
                            (date.compareTo(from) > 0 && date.compareTo(till) <= 0)) {
                        if (monthSchedule.containsKey("booked")) {
                            monthSchedule.get("booked").add(i);
                        } else {
                            List<Integer> list = new ArrayList<>();

                            list.add(i);

                            monthSchedule.put("booked", list);
                        }
                        added = true;

                        break;
                    }
                }
                if (!added) {
                    if (monthSchedule.containsKey("free")) {
                        monthSchedule.get("free").add(i);
                    } else {
                        List<Integer> list = new ArrayList<>();

                        list.add(i);

                        monthSchedule.put("free", list);
                    }
                }
            }
        }

        return monthSchedule;
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
