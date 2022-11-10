package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistrationDto {

    private Long productId;

    private String title;

    private String description;

    private int price;

    private String currency;

    private int minimumBookingNumberDay;

    private LocalDate bookDateFrom;

    private LocalDate bookDateTill;

    private Double locationX;

    private Double locationY;

    private Long categoryId;

    HashMap<String, String> fieldValue;

}
