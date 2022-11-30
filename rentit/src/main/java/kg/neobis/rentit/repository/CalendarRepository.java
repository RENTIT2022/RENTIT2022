package kg.neobis.rentit.repository;

import kg.neobis.rentit.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Query(
            value = "SELECT MAX(date) FROM Calendar",
            nativeQuery = true
    )
    Calendar getMaxCalendar();

    @Query(
            value = "DELETE FROM Calendar " +
                    "WHERE date < :bookFromDate",
            nativeQuery = true
    )
    void cutProductCalendarBeforeDate(LocalDate bookFromDate);

    @Query(
            value = "DELETE FROM Calendar " +
                    "WHERE date > :bookTillDate",
            nativeQuery = true
    )
    void cutProductCalendarAfterDate(LocalDate bookTillDate);

    @Query(
            value = "SELECT * FROM Calendar " +
                    "WHERE date >= :dateFrom " +
                    "AND date <= :dateTill " +
                    "AND product_id = :productId",
            nativeQuery = true
    )
    List<Calendar> getCalendarByDates(LocalDate dateFrom, LocalDate dateTill, Long productId);

}
