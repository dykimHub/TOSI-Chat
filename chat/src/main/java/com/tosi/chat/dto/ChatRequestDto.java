package com.tosi.chat.dto;

import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequestDto {
    private List<MultiChatMessage> preMultiChatMessageList;
    private String multiChatMessage;
}
