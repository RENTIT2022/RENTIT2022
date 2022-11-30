package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            value = "SELECT * FROM Booking WHERE product_id IN(:products) AND booking_status = 2 " +
                    "ORDER BY booking_status DESC",
            nativeQuery = true
    )
    List<Booking> getBookingRequests(List<Long> products);

    @Query(
            value = "SELECT * FROM Booking " +
                    "WHERE product_id = :productId " +
                    "AND date_till > :date " +
                    "AND booking_status = 0 " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Booking getBookingsByProductIdAndDate(Long productId, LocalDate date);

    @Query(
            value = "SELECT * FROM Booking " +
                    "WHERE product_id = :productId " +
                    "AND date_till < :date " +
                    "AND booking_status = 0 " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Booking checkUpdateDateTill(Long productId, LocalDate date);

    List<Booking> findAllByProductIdOrderByDateFrom(Long productId);

}
