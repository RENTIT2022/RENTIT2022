package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.ChatMessageDto;
import kg.neobis.rentit.entity.ChatMessage;

public class ChatMessageMapper {

    public static ChatMessageDto chatMessageToChatChatMessageDto(ChatMessage chatMessage) {
        ChatMessageDto chatMessageDto = ChatMessageDto.builder()
                .id(chatMessage.getId())
                //.chatDto(null)
                //.chatDto(ChatMapper.chatToChatDto(chatMessage.getChat()))
                .chatUserDto(ChatUserMapper.userToChatUserDto(chatMessage.getUser()))
                .content(chatMessage.getContent())
                .dateCreated(chatMessage.getDateCreated())
                .dateUpdated(chatMessage.getDateUpdated())
                .dateDeleted(chatMessage.getDateDeleted())
                .build();

        return chatMessageDto;
    }
}
