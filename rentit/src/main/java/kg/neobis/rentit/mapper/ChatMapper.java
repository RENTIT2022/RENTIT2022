package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.ChatDto;
import kg.neobis.rentit.dto.ChatUserDto;
import kg.neobis.rentit.entity.Chat;
import kg.neobis.rentit.entity.User;

import java.util.HashSet;
import java.util.Set;

public class ChatMapper {

    public static ChatDto chatToChatDto(Chat chat) {
        Set<ChatUserDto> userDtos = new HashSet<ChatUserDto>();
        if(chat.getUsers().size() != 0) {
            for (User user : chat.getUsers()) {
                userDtos.add(ChatUserMapper.userToChatUserDto(user));
            }
        }

        ChatDto chatDto = ChatDto.builder()
                .id(chat.getId())
                .chatUsers(userDtos)
                .dateCreated(chat.getDateCreated())
                .build();

        return chatDto;
    }
}
