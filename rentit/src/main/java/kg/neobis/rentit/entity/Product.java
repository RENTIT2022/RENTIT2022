package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`product`")
public class Product {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_seq"
    )
    @SequenceGenerator(
            name = "product_seq",
            sequenceName = "product_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private int price;

    private String currency;

    private int minimumBookingNumberDay;

    private int clickedNum;

    @Column(nullable = false)
    private LocalDate bookDateFrom;

    @Column(nullable = false)
    private LocalDate bookDateTill;

    private LocalDateTime creationTime;

    private Boolean active;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "location_id"
    )
    private Location location;

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
            name = "category_id"
    )
    private Category category;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    private Set<FieldProduct> fieldProducts;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    private List<ImageProduct> imageProduct;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "product"
    )
    private List<Booking> bookings;

    @OneToMany(
            mappedBy = "product"
    )
    private List<Review> reviews;

}
