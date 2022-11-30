package kg.neobis.rentit.controller;

import kg.neobis.rentit.dto.ChatDto;
import kg.neobis.rentit.entity.Chat;
import kg.neobis.rentit.request.ChatRequest;
import kg.neobis.rentit.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<List<ChatDto>> getChatByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(chatService.getChatByUserId(id));
    }

    @PostMapping(value = "/add-chat", produces = "application/json")
    public ResponseEntity<ChatDto> addChat(@RequestBody ChatRequest chatRequest) {
        return new ResponseEntity<>(chatService.addChat(chatRequest), HttpStatus.OK);
    }
}
