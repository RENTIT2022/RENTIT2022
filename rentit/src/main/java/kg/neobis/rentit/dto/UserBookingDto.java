package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBookingDto {

    private Long bookingId;

    private Long productId;

    private String productTitle;

    private String mainImageUrl;

    private String status;

    private int totalPrice;

}
