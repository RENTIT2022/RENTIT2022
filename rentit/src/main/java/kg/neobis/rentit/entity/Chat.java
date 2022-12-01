package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`chat`")
public class Chat {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_seq"
    )
    @SequenceGenerator(
            name = "chat_seq",
            sequenceName = "chat_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users;

    @Column(name = "date_created")
    private LocalDate dateCreated;
}
