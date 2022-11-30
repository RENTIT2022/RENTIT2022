package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.neobis.rentit.entity.ImageUser;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChatUserDto объект для передачи данных")
public class ChatUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUser;
    private boolean Status;
}
