package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.neobis.rentit.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChatDto объект для передачи данных")
public class ChatDto {
    private Long id;
    private Set<ChatUserDto> chatUsers;
    private LocalDate dateCreated;
}
