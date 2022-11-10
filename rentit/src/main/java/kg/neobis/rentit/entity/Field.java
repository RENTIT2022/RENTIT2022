package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`field`")
public class Field {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "field_seq"
    )
    @SequenceGenerator(
            name = "field_seq",
            sequenceName = "field_seq",
            allocationSize = 1
    )
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "field"
    )
    private Set<CategoryField> categories;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "field"
    )
    private Set<FieldProduct> fieldProducts;

}
