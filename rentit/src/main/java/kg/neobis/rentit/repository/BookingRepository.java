package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(
            value = "SELECT * FROM Booking WHERE product_id IN(:products) AND booking_status = 'Pending' " +
                    "ORDER BY booking_status DESC",
            nativeQuery = true
    )
    List<Booking> getBookingRequests(List<Long> products);

    List<Booking> findAllByProductIdOrderByDateFrom(Long productId);

}