package kg.neobis.rentit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFieldsDto {

    private Long categoryId;

    private String name;

    private Map<String, String> fields;

}
