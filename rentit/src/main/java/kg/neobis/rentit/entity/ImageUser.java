package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`image_user`")
public class ImageUser {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_user_seq"
    )
    @SequenceGenerator(
            name = "image_user_seq",
            sequenceName = "image_user_seq",
            allocationSize = 1
    )
    private Long id;

    private Byte orderNumber;

    @OneToOne
    @JoinColumn(
            name = "image_id"
    )
    private Image image;
}
