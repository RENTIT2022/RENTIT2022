package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewDto {

    private String name;

    private String text;

    private int star;

    private LocalDate date;

}
