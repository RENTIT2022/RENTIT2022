package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRegistrationDto {

    private Long productId;

    private LocalDate dateFrom;

    private LocalDate dateTill;

    private int totalPrice;

}
