package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`review`")
public class Review {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_seq"
    )
    @SequenceGenerator(
            name = "review_seq",
            sequenceName = "review_seq",
            allocationSize = 1
    )
    private Long id;

    private String text;

    private LocalDateTime dateTime;

    private byte star;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

}
