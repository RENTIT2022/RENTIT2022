package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`image_product`")
public class ImageProduct {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_product_seq"
    )
    @SequenceGenerator(
            name = "image_product_seq",
            sequenceName = "image_product_seq",
            allocationSize = 1
    )
    private Long id;

    private Byte orderNumber;

    @OneToOne
    @JoinColumn(
            name = "image_id"
    )
    private Image image;

    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

}
