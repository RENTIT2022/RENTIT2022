package kg.neobis.rentit.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`permission`")
public class Permission {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "permission_seq"
    )
    @SequenceGenerator(
            name = "permission_seq",
            sequenceName = "permission_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
}
