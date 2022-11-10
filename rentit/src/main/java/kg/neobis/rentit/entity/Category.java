package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`category`")
public class Category {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_seq"
    )
    @SequenceGenerator(
            name = "category_seq",
            sequenceName = "category_seq",
            allocationSize = 1
    )
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "category"
    )
    private Set<CategoryField> fields = new HashSet<>();

}
