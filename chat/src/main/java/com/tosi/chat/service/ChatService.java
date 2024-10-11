package com.tosi.chat.service;

import com.tosi.chat.dto.ChatInitRequestDto;
import com.tosi.chat.dto.ChatRequestDto;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;

import java.util.List;

public interface ChatService {

    List<MultiChatMessage> sendInitChat(ChatInitRequestDto chatInitRequestDto);

    List<MultiChatMessage> sendChat(ChatRequestDto chatRequestDto);

    List<MultiChatMessage> sendFinalChat(ChatRequestDto chatRequestDto);

    Long findUserAuthorization(String accessToken);
}
