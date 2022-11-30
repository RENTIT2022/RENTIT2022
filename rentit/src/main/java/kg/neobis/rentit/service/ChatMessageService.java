package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.ChatMessageDto;
import kg.neobis.rentit.dto.UserDto;
import kg.neobis.rentit.entity.Chat;
import kg.neobis.rentit.entity.ChatMessage;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.mapper.ChatMessageMapper;
import kg.neobis.rentit.mapper.UserMapper;
import kg.neobis.rentit.repository.ChatMessageRepository;
import kg.neobis.rentit.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatService chatService;
    private final UserService userService;

    public List<ChatMessageDto> getChatMessagesByChatId(Long id) {
        Iterable<ChatMessage> chatMessages = chatMessageRepository.findByChatIdOrderByDateCreated(id);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<ChatMessageDto>();

        for (ChatMessage chatMessage : chatMessages){
            chatMessageDtos.add(ChatMessageMapper.chatMessageToChatChatMessageDto(chatMessage));
        }

        return chatMessageDtos;
    }

    public ChatMessageDto addChatMessage(ChatMessageRequest chatMessageRequest) {
        Chat chat = chatService.getChatById(chatMessageRequest.getChatId());
        User user = userService.getById(chatMessageRequest.getSenderId());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChat(chat);
        chatMessage.setUser(user);
        chatMessage.setContent(chatMessageRequest.getContent());
        chatMessage.setDateCreated(new Date());

        return ChatMessageMapper.chatMessageToChatChatMessageDto(chatMessageRepository.save(chatMessage));
    }
}
