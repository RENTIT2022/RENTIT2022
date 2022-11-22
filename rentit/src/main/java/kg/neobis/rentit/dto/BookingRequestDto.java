package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    private Long bookingId;

    private Long productId;

    private Long clientId;

    private String clientName;

    private String productTitle;

    private String mainImageUrl;

    private LocalDate bookDateFrom;

    private LocalDate bookDateTill;

    private int price;

}
