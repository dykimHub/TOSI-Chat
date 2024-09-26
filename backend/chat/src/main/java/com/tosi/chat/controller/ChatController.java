package com.tosi.chat.controller;

import com.tosi.chat.dto.ChatInitInfoDto;
import com.tosi.chat.dto.ChatRequestDto;
import com.tosi.chat.service.ChatService;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "사용자가 선택한 등장인물과 채팅 시작")
    @PostMapping("/init")
    public ResponseEntity<List<MultiChatMessage>> sendInitChat(@RequestHeader("Authorization") String accessToken, @RequestBody ChatInitInfoDto chatInitInfoDto) {
        List<MultiChatMessage> multiChatMessageList = chatService.sendInitChat(accessToken, chatInitInfoDto);
        return ResponseEntity.ok()
                .body(multiChatMessageList);
    }

    @Operation(summary = "사용자가 선택한 등장인물과 이어서 채팅하기")
    @PostMapping
    public ResponseEntity<List<MultiChatMessage>> sendChat(@RequestBody ChatRequestDto chatRequestDto) {
        List<MultiChatMessage> multiChatMessageList = chatService.sendChat(chatRequestDto);
        return ResponseEntity.ok()
                .body(multiChatMessageList);
    }

    @Operation(summary = "7번의 질문이 끝나고 마지막 인사로 응답")
    @PostMapping("/final")
    public ResponseEntity<List<MultiChatMessage>> sendFinalChat(@RequestBody ChatRequestDto chatRequestDto) {
        List<MultiChatMessage> multiChatMessageList = chatService.sendFinalChat(chatRequestDto);
        return ResponseEntity.ok()
                .body(multiChatMessageList);
    }
}
