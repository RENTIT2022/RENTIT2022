package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long userId;

    private String fullName;

    private int profileRating;

    private String phoneNumber;

    private String imageUrl;

    private List<ProductPageDto> products;

}
