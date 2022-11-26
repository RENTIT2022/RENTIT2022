package kg.neobis.rentit.dto;

import kg.neobis.rentit.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsDto {

    private Long productId;

    private String title;

    private String description;

    private int clickNumber;

    private int price;

    private int likedNum;

    private String rating;

    private boolean isFavorite;

    private boolean isActive;

    private boolean isBlocked;

    private int minimumBookingNumberDay;

    private Location location;

    private List<ProductImageDto> images;

    private TreeMap<String, String> characteristics;

}
