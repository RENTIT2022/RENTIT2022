package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChatMessageDto объект для передачи данных")
public class ChatMessageDto {
    private Long id;
    //private ChatDto chatDto;
    private ChatUserDto chatUserDto;
    private String content;
    private Date dateCreated;
    private Date dateUpdated;
    private Date dateDeleted;
}
