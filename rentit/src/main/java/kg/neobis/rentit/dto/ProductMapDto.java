package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMapDto {

    private Long productId;

    private double locationX;

    private double locationY;

    private String title;

    private int price;

    private String rating;

    private int reviewNum;

    private String imageUrl;

}
