package kg.neobis.rentit.entity;

import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`role`")
public class Role {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_seq"
    )
    @SequenceGenerator(
            name = "role_seq",
            sequenceName = "role_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));

        authorities.add(new SimpleGrantedAuthority("ROLE_" + name));

        return authorities;
    }
}
