package kg.neobis.rentit.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoryFieldId implements Serializable {

    private Long categoryId;

    private Long fieldId;

}
