package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Booking;
import kg.neobis.rentit.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            value = "SELECT b FROM Booking b WHERE b.product.id IN(:products) AND b.bookingStatus = :status " +
                    "ORDER BY b.bookingStatus DESC"
    )
    List<Booking> getBookingRequests(List<Long> products, BookingStatus bookingStatus);

    @Query(
            value = "SELECT * FROM Booking " +
                    "WHERE product_id = :productId " +
                    "AND date_till > :date " +
                    "AND booking_status = 'ACCEPTED'" +
                    "LIMIT 1",
            nativeQuery = true
    )
    Booking getBookingsByProductIdAndDate(Long productId, LocalDate date);

    @Query(
            value = "SELECT * FROM Booking " +
                    "WHERE product_id = :productId " +
                    "AND date_till < :date " +
                    "AND booking_status = 'ACCEPTED'" +
                    "LIMIT 1",
            nativeQuery = true
    )
    Booking checkUpdateDateTill(Long productId, LocalDate date);

    List<Booking> findAllByProductIdOrderByDateFrom(Long productId);

}
