package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query(
            value = "SELECT * FROM Calendar " +
                    "WHERE product_id = :productId " +
                    "AND date_part('year', date) = :year " +
                    "AND date_part('month', date) = :month " +
                    "ORDER BY date",
            nativeQuery = true
    )
    List<Calendar> getCalendarByProductIdAndYearAndMonth(Long productId, int year, int month);

}
