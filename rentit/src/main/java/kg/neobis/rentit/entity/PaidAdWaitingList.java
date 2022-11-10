package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`paid_ad_waiting_list`")
public class PaidAdWaitingList {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "paid_ad_waiting_list_seq"
    )
    @SequenceGenerator(
            name = "paid_ad_waiting_list_seq",
            sequenceName = "paid_ad_waiting_list_seq",
            allocationSize = 1
    )
    private Long id;

    private double pricePerDay;

    private boolean isPaid;

    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

    @OneToOne(
            mappedBy = "paidAdWaitingList"
    )
    private PaidAd paidAd;

}
