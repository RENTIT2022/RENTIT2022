package kg.neobis.rentit.controller;

import kg.neobis.rentit.dto.ChatMessageDto;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.request.ChatMessageRequest;
import kg.neobis.rentit.service.ChatMessageService;
import kg.neobis.rentit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GeneralChatController {

    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/chat-sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageRequest chatMessageRequest) {
        return chatMessageService.addChatMessage(chatMessageRequest);
    }

    @MessageMapping("/chat-addUser")
    @SendTo("/topic/public")
    public ChatMessageRequest addUser(@Payload ChatMessageRequest chatMessageRequest,
                               SimpMessageHeaderAccessor headerAccessor) {

        User user = userService.getById(chatMessageRequest.getSenderId());

        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("firstName", user.getFirstName());

        return chatMessageRequest;
    }
}
