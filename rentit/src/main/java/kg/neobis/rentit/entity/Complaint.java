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
@Table(name = "`complaint`")
public class Complaint {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "complaint_seq"
    )
    @SequenceGenerator(
            name = "complaint_seq",
            sequenceName = "complaint_seq",
            allocationSize = 1
    )
    private Long id;

    private String reason;

    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(
            name = "addressee_id"
    )
    private User addressee;

    @ManyToOne
    @JoinColumn(
            name = "sender_id"
    )
    private User sender;

}
