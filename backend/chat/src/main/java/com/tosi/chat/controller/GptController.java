package com.tosi.chat.controller;

import com.tosi.chat.dto.ChatInitInfoDto;
import com.tosi.chat.service.ChatService;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/chat")
@RestController
public class GptController {
    private final ChatService chatService;

    @Operation(summary = "사용자가 선택한 등장인물과 채팅 시작")
    @PostMapping
    public ResponseEntity<List<MultiChatMessage>> initChat(@RequestBody ChatInitInfoDto chatInitInfoDto) {
        List<MultiChatMessage> multiChatMessage = chatService.initChat(chatInitInfoDto);
        return ResponseEntity.ok()
                .body(multiChatMessage);
    }

    // 다음 메소드는 마지막 응답을 호출합니다.
//    @PostMapping("/bye")
//    ResponseEntity<?> putByeMessage(@Parameter(description = "user가 입력하는 메세지", required = true, example = "세상에서 제일 맛있는 도너츠는?") @RequestBody UserInputMessage userInputMessage) {
//        Message responseMessage = (
//                new Message("assistant", chatgptService.sendBye(userInputMessage)));
//
//        return new ResponseEntity<>(responseMessage,
//                HttpStatus.OK);
//    }
}
