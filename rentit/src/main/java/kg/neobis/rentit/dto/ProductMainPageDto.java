package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMainPageDto {

    private Long productId;

    private String title;

    private int price;

    private int clickNumber;

    private String mainImageUrl;

    private boolean isFavorite;

}
