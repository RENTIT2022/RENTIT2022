package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.ChatDto;
import kg.neobis.rentit.entity.Chat;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.mapper.ChatMapper;
import kg.neobis.rentit.mapper.UserMapper;
import kg.neobis.rentit.repository.ChatRepository;
import kg.neobis.rentit.request.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    public Chat getChatById(Long id) {
        Chat chat = chatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Чат с таким id не найден =  " + id));

        return chat;
    }

    public List<ChatDto> getChatByUserId(Long id) {
        List<Chat> chats = chatRepository.findAll();
        List<ChatDto> chatsByUserId = new ArrayList<>();

        for(Chat chat: chats) {
            Set<User> users = chat.getUsers();
            for(User user: users) {
                if(user.getId() == id) {
                    User userTemp = userService.getById(id);
                    Chat chatTemp = chat;
                    chatTemp.getUsers().remove(userTemp);
                    chatsByUserId.add(ChatMapper.chatToChatDto(chatTemp));
                    return chatsByUserId;
                }
            }
        }

        return null;
    }

    public ChatDto addChat(ChatRequest chatRequest) {
        User user1 = UserMapper.userDtoToUser(userService.getUserById(chatRequest.getSender_id()));
        User user2 = UserMapper.userDtoToUser(userService.getUserById(chatRequest.getReceiver_id()));

        List<Chat> chats = chatRepository.findAll();
        for(Chat chat: chats) {
            boolean check = true;
            Set<User> users = chat.getUsers();
            for (User user : users) {
                if (user.getId() != user1.getId() && user.getId() != user2.getId()) {
                    check = false;
                }
            }
            if (check) {
                return ChatMapper.chatToChatDto(chat);
            }
        }

        Set<User> newUsers = new HashSet<>();
        newUsers.add(user1);
        newUsers.add(user2);

        Chat newChat = new Chat();
        newChat.setUsers(newUsers);
        newChat.setDateCreated(LocalDate.now());

        return ChatMapper.chatToChatDto(chatRepository.save(newChat));
    }
}
