package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewDto {

    private Long productId;

    private String text;

    private byte star;

}
