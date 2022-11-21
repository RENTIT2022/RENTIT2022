package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`question`")
public class Question {

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

    private String question;

    private String answer;

}
