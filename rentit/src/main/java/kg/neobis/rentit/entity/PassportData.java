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
@Table(name = "`passport_data`")
public class PassportData {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "passport_data_seq"
    )
    @SequenceGenerator(
            name = "passport_data_seq",
            sequenceName = "passport_data_seq",
            allocationSize = 1
    )
    private Long id;

    private String tin;

    @Column(name = "date_of_issue")
    private LocalDate dateOfIssue;

    private String authority;

    public PassportData(String tin, LocalDate dateOfIssue, String authority) {
        this.tin = tin;
        this.dateOfIssue = dateOfIssue;
        this.authority = authority;
    }
}
