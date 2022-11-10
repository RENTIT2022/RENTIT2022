package kg.neobis.rentit.entity;

import kg.neobis.rentit.enums.BookingStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`booking`")
public class Booking {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booking_seq"
    )
    @SequenceGenerator(
            name = "booking_seq",
            sequenceName = "booking_seq",
            allocationSize = 1
    )
    private Long id;

    private Integer totalPrice;

    private LocalDate dateFrom;

    private LocalDate dateTill;

    private LocalDateTime bookingDateTime;

    private BookingStatus bookingStatus;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

}
