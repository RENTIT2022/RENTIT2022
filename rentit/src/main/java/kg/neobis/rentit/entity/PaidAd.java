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
@Table(name = "`paid_ad`")
public class PaidAd {


    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "paid_ad_seq"
    )
    @SequenceGenerator(
            name = "paid_ad_seq",
            sequenceName = "paid_ad_seq",
            allocationSize = 1
    )
    private Long id;

    private LocalDate dateFrom;

    private LocalDate dateTill;

    @OneToOne
    @JoinColumn(
            name = "paid_ad_waiting_list_id"
    )
    private PaidAdWaitingList paidAdWaitingList;

}
