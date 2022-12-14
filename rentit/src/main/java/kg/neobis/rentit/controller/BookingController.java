package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.BookingRegistrationDto;
import kg.neobis.rentit.dto.BookingRequestDto;
import kg.neobis.rentit.dto.UserBookingDto;
import kg.neobis.rentit.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@Validated
@RequiredArgsConstructor
@Tag(name = "Booking Controller", description = "The Booking API with documentation annotations")
public class BookingController {

    private final BookingService bookingService;


    @GetMapping("/get/user-bookings")
    @Operation(summary = "Получение бронирований пользователя")
    public ResponseEntity<List<UserBookingDto>> getUserBookings() {
        return ResponseEntity.ok(bookingService.getUserBookings());
    }

    @GetMapping("/get/requests")
    @Operation(summary = "Получение запросов на бронирование")
    public ResponseEntity<List<BookingRequestDto>> getBookingRequests() {
        return ResponseEntity.ok(bookingService.getBookingRequests());
    }

    @PostMapping("/book-product")
    @Operation(summary = "Бронирование продукта")
    public ResponseEntity<BookingRegistrationDto> bookProduct(@RequestBody BookingRegistrationDto dto) {
        return ResponseEntity.ok(bookingService.bookProduct(dto));
    }

    @GetMapping("/get/product-schedule/{productId}/{year}/{month}")
    @Operation(summary = "Получение расписания бронирования продукта по году и месяцу")
    public ResponseEntity<Map<String, List<Integer>>> getProductSchedule(@PathVariable Long productId,
                                                                         @PathVariable int year,
                                                                         @PathVariable int month) {
        return ResponseEntity.ok(bookingService.getProductSchedule(productId, year, month));
    }

    @DeleteMapping("/cancel-booking/{bookingId}")
    @Operation(summary = "Отменить бронирование")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    @PutMapping("/accept-booking-request/{bookingId}")
    @Operation(summary = "Подтверждить бронирование")
    public ResponseEntity<String> acceptBookingRequest(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.acceptBookingRequest(bookingId));
    }

    @PutMapping("/reject-booking-request/{bookingId}")
    @Operation(summary = "Отклонить бронирование")
    public ResponseEntity<String> rejectBookingRequest(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.rejectBookingRequest(bookingId));
    }

}
