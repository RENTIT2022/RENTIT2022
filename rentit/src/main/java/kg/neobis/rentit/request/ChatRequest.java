package kg.neobis.rentit.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private Long sender_id;
    private Long receiver_id;
}
