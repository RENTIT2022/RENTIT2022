package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`calendar`")
public class Calendar {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "calendar_seq"
    )
    @SequenceGenerator(
            name = "calendar_seq",
            sequenceName = "calendar_seq",
            allocationSize = 1
    )
    private Long id;

    private LocalDate date;

    private boolean booked;

    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

}
