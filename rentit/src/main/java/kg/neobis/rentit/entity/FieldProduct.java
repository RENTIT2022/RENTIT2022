package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`field_product`")
public class FieldProduct {

    @EmbeddedId
    private FieldProductId id;

    @ManyToOne
    @MapsId("fieldId")
    @JoinColumn(
            name = "field_id"
    )
    private Field field;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @MapsId("productId")
    @JoinColumn(
            name = "product_id"
    )
    private Product product;

    private String value;

    public void setId(Field field, Product product) {
        FieldProductId fieldProductId = new FieldProductId();

        fieldProductId.setFieldId(field.getId());
        fieldProductId.setProductId(product.getId());

        id = fieldProductId;
    }
}
