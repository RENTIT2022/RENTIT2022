package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`category_field`")
public class CategoryField {

    @EmbeddedId
    private CategoryFieldId id;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(
            name = "category_id"
    )
    private Category category;

    @ManyToOne
    @MapsId("fieldId")
    @JoinColumn(
            name = "field_id"
    )
    private Field field;

    private boolean isVisible;

}
