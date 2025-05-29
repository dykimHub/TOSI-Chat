package com.tosi.chat.controller;

import com.tosi.chat.dto.ChatInitRequestDto;
import com.tosi.chat.dto.ChatRequestDto;
import com.tosi.chat.service.ChatService;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.init
                    )
            )
    )
    @PostMapping("/init")
    public ResponseEntity<List<MultiChatMessage>> sendInitChat(@RequestHeader("Authorization") String accessToken, @RequestBody ChatInitRequestDto chatInitRequestDto) {
        Long userId = chatService.findUserAuthorization(accessToken);
        List<MultiChatMessage> multiChatMessageList = chatService.sendInitChat(chatInitRequestDto);
        return ResponseEntity.ok()
                .body(multiChatMessageList);
    }

    @Operation(summary = "이전 채팅을 바탕으로 채팅 시작")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.progress
                    )
            )
    )
    @PostMapping
    public ResponseEntity<List<MultiChatMessage>> sendChat(@RequestBody ChatRequestDto chatRequestDto) {
        List<MultiChatMessage> multiChatMessageList = chatService.sendChat(chatRequestDto);
        return ResponseEntity.ok()
                .body(multiChatMessageList);
    }

    @Operation(summary = "7번의 질문이 끝나고 마지막 인사로 응답")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.progress
                    )
            )
    )
    @PostMapping("/final")
    public ResponseEntity<List<MultiChatMessage>> sendFinalChat(@RequestBody ChatRequestDto chatRequestDto) {
        List<MultiChatMessage> multiChatMessageList = chatService.sendFinalChat(chatRequestDto);
        return ResponseEntity.ok()
                .body(multiChatMessageList);
    }
}
