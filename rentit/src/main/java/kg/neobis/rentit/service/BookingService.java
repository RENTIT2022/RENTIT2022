package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.BookingRegistrationDto;
import kg.neobis.rentit.dto.BookingRequestDto;
import kg.neobis.rentit.dto.UserBookingDto;
import kg.neobis.rentit.entity.Calendar;
import kg.neobis.rentit.entity.*;
import kg.neobis.rentit.enums.BookingStatus;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.BadRequestException;
import kg.neobis.rentit.exception.ProductViolationException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private final ComplaintRepository complaintRepository;

    private final ReviewRepository reviewRepository;


    public List<UserBookingDto> getUserBookings() {
        User user = getAuthentication();

        return user == null ? null : user.getBookings().stream()
                .map(
                        entity -> {
                            UserBookingDto dto = new UserBookingDto();

                            dto.setBookingId(entity.getId());
                            dto.setStatus(entity.getBookingStatus().getStatus());
                            dto.setProductId(entity.getProduct().getId());
                            dto.setTotalPrice(entity.getTotalPrice());
                            dto.setProductTitle(entity.getProduct().getTitle());
                            dto.setMainImageUrl(returnMainImageUrl(entity));
                            dto.setBookFrom(entity.getDateFrom());
                            dto.setBookTill(entity.getDateTill());

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
                    dto.setTotalPrice(entity.getTotalPrice());
                    dto.setProductTitle(entity.getProduct().getTitle());
                    dto.setProductId(entity.getProduct().getId());
                    dto.setBookDateFrom(entity.getDateFrom());
                    dto.setBookDateTill(entity.getDateTill());
                    dto.setMainImageUrl(returnMainImageUrl(entity));

                    int rating = 100 - complaintRepository
                            .getComplaintsNumberByAddresseeId(dto.getClientId());

                    for(Product p: entity.getUser().getProducts()) {
                        List<Review> reviews = reviewRepository.findAllByProductId(p.getId());

                        double sum = (double) reviews.stream()
                                .mapToInt(Review::getStar).sum() / reviews.size();

                        if(sum >= 4.9) {
                            rating += 5;
                        }
                    }

                    dto.setProfileRating(rating > 100 ? 100 : Math.max(rating, 1));

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
            if (image != null && image.getUrl().startsWith("http")) {
                return image.getUrl().replace("http", "https");
            }
        }

        return "";
    }

    @Transactional
    public BookingRegistrationDto bookProduct(BookingRegistrationDto dto) {
        User user = getAuthentication();

        if (user == null) {
            throw new ResourceNotFoundException("???????????????????????? ???? ??????????????????????.");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("?????????????? ???? ?????? ???????????? ?? ????????: " + dto.getProductId())
                );

        LocalDate sumFrom = dto.getDateFrom();
        LocalDate sumTill = dto.getDateTill();

        int days = 1;

        while (!sumFrom.isEqual(sumTill)) {
            sumFrom = sumFrom.plusDays(1);
            days++;
        }

        List<Calendar> calendar = calendarRepository.getCalendarByDates(dto.getDateFrom(),
                dto.getDateTill(), product.getId());

        if (days != calendar.size()) {
            throw new BadRequestException("???????????????????????? ???????? ????????????. ?????????????????? ???????????????????? ???????? ???? " +
                    "???????????????? ?????? ????????????????????????.");
        }

        for (Calendar entity : calendar) {
            if (entity.isBooked()) {
                throw new AlreadyExistException("???????? ?????? ??????????????????????????: " + entity.getDate());
            } else {
                entity.setBooked(true);
            }
            calendarRepository.save(entity);
        }

        if (user.getId().equals(product.getUser().getId())) {
            throw new BadRequestException("???? ???? ???????????? ?????????????????????????? ???????? ??????????????.");
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setProduct(product);
        booking.setDateFrom(dto.getDateFrom());
        booking.setDateTill(dto.getDateTill());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingDateTime(LocalDateTime.now());
        booking.setTotalPrice(days * product.getPrice());

        bookingRepository.save(booking);

        return dto;
    }

    public Map<String, List<Integer>> getProductSchedule(Long productId, int year, int month) {
        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("?????????????? ???? ?????? ???????????? ?? ????????: " + productId)
                );

        List<Calendar> calendar = calendarRepository
                .getCalendarByProductIdAndYearAndMonth(productId, year, month);

        int days = YearMonth.of(year, month).lengthOfMonth();

        int[] results = new int[days + 1];

        for (Calendar entity : calendar) {
            int d = entity.getDate().getDayOfMonth();

            if (entity.isBooked()) {
                results[d] = 1;
            } else if (!entity.isBooked()) {
                results[d] = -1;
            }
        }

        Map<String, List<Integer>> schedule = new HashMap<>();

        schedule.put("booked", new ArrayList<>());
        schedule.put("free", new ArrayList<>());
        schedule.put("no booking", new ArrayList<>());

        for (int i = 1; i < results.length; i++) {
            if (results[i] == 1) {
                schedule.get("booked").add(i);
            } else if (results[i] == -1) {
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
                        () -> new ResourceNotFoundException("???????????????????????? ???? ???????? ?????????????? ?? ????????: "
                                + bookingId)
                );

        User user = getAuthentication();

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ProductViolationException("?????????? ???? ?????????????????????? ??????.");
        }

        List<Calendar> calendar = calendarRepository.getCalendarByDates(booking.getDateFrom(),
                booking.getDateTill(), booking.getProduct().getId());

        for (Calendar entity : calendar) {
            entity.setUser(null);
            entity.setBooked(false);
            calendarRepository.save(entity);
        }

        bookingRepository.deleteById(booking.getId());

        return "???????????????????????? ???????????????? ??????????????.";
    }

    public String acceptBookingRequest(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("???????????????????????? ???? ???????? ?????????????? ?? ????????: "
                                + bookingId)
                );

        if(!getAuthentication().getId().equals(booking.getProduct().getUser().getId())) {
            throw new ProductViolationException("???? ???? ???????????? ?????????????? ?????????????? ???? ?????????? ????????????????, " +
                    "?????? ?????? ???? ???? ???????????????????????? ??????.");
        }

        booking.setBookingStatus(BookingStatus.ACCEPTED);

        bookingRepository.save(booking);

        List<Calendar> calendar = calendarRepository.getCalendarByDates(booking.getDateFrom(),
                booking.getDateTill(), booking.getProduct().getId());

        for (Calendar entity : calendar) {
            entity.setUser(booking.getUser());
            calendarRepository.save(entity);
        }

        return "???????????????????????? ???????????????? ??????????????.";
    }

    public String rejectBookingRequest(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("???????????????????????? ???? ???????? ?????????????? ?? ????????: "
                                + bookingId)
                );

        if(!getAuthentication().getId().equals(booking.getProduct().getUser().getId())) {
            throw new ProductViolationException("???? ???? ???????????? ?????????????? ?????????????? ???? ?????????? ????????????????, " +
                    "?????? ?????? ???? ???? ???????????????????????? ??????.");
        }

        booking.setBookingStatus(BookingStatus.REJECTED);

        List<Calendar> calendar = calendarRepository.getCalendarByDates(booking.getDateFrom(),
                booking.getDateTill(), booking.getProduct().getId());

        for (Calendar entity : calendar) {
            entity.setBooked(false);
            entity.setUser(null);
            calendarRepository.save(entity);
        }

        bookingRepository.save(booking);

        return "???????????????????????? ???????????????? ??????????????.";
    }

    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName());
    }

}
