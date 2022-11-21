package kg.neobis.rentit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ApiError объект для отображения подробной информации об ошибке")
public class ApiError {

    @Schema(description = "Статус код")
    private int statusCode;

    @Schema(description = "Временная метка")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Сообщение")
    private String message;

    @Schema(description = "Список ошибок")
    private List<String> errors;
}
