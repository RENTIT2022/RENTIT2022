package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.ChatUserDto;
import kg.neobis.rentit.entity.User;

public class ChatUserMapper {

    public static ChatUserDto userToChatUserDto(User user) {
        ChatUserDto chatUserDto = ChatUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .imageUser(user.getImageUser().get(0).getImage().getUrl())
                .build();

        return chatUserDto;
    }
}
