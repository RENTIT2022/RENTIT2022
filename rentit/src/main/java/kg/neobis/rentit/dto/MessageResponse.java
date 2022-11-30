package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "MessageResponse для вывода сообщений")
public class MessageResponse {

    @Schema(description = "Сообщение")
    private String message;
}
