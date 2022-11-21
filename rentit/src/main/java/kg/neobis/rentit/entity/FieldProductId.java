package kg.neobis.rentit.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FieldProductId implements Serializable {

    private Long fieldId;

    private Long productId;

}
