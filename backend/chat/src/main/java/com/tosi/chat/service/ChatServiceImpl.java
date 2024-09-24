package com.tosi.chat.service;

import com.tosi.chat.common.config.OpenAIProperties;
import com.tosi.chat.dto.ChatInitInfoDto;
import com.tosi.chat.dto.ChatInitRequestDto;
import com.tosi.chat.dto.TaleDetailDto;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatRequest;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final OpenAIProperties openAIProperties;
    private final RestTemplate restTemplate;
    @Value("${service.tale.url}")
    private String taleURL;

    /**
     * 동화 제목, 동화 내용, 사용자, 선택한 등장인물 정보로 채팅을 시작합니다.
     * OpenAI API에 요청한 메시지와 응답받은 메시지를 담은 리스트를 반환합니다.
     *
     * @param chatInitInfoDto 채팅을 시작할 때 필요한 학습 정보가 담긴 ChatInitInfoDto 객체
     * @return 사용자와 시스템 간의 채팅 메시지를 담은 MultiChatMessage 객체 리스트
     */
    @Override
    public List<MultiChatMessage> initChat(ChatInitInfoDto chatInitInfoDto) {
        // 채팅 메시지 리스트 생성
        List<MultiChatMessage> multiChatMessages = new ArrayList<>();

        // 프롬프트 생성 및 사용자 메시지 추가
        String prompt = this.makeChatInitRequestDto(chatInitInfoDto);
        multiChatMessages.add(new MultiChatMessage("user", prompt));

        return multiChatMessages;
    }

    /**
     * Tale 서비스에 동화 정보를 요청하고, 채팅에 사용할 프롬프트를 생성합니다.
     *
     * @param chatInitInfoDto 채팅을 시작할 때 필요한 학습 정보가 담긴 ChatInitInfoDto 객체
     * @return ChatInitInfoDto를 기반으로 생성된 프롬프트 문자열
     */
    private String makeChatInitRequestDto(ChatInitInfoDto chatInitInfoDto) {
        TaleDetailDto taleDetailDto = restTemplate.getForObject(taleURL + "content/" + chatInitInfoDto.getTaleId(), TaleDetailDto.class);

        return ChatInitRequestDto.builder()
                .childName(chatInitInfoDto.getChildName())
                .characterName(chatInitInfoDto.getCharacterName())
                .taleTitle(taleDetailDto.getTitle())
                .taleContent(taleDetailDto.getContent())
                .build()
                .getPrompt();
    }




}
