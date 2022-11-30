package kg.neobis.rentit.controller;

import kg.neobis.rentit.dto.ChatMessageDto;
import kg.neobis.rentit.entity.ChatMessage;
import kg.neobis.rentit.request.ChatMessageRequest;
import kg.neobis.rentit.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<List<ChatMessageDto>> getChatMessagesByChatId(@PathVariable Long id) {
        return ResponseEntity.ok(chatMessageService.getChatMessagesByChatId(id));
    }

    @PostMapping(value = "/add-chat-message", produces = "application/json")
    public ResponseEntity<ChatMessageDto> addChatMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
        return new ResponseEntity<>(chatMessageService.addChatMessage(chatMessageRequest), HttpStatus.OK);
    }
}
