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
    public List<MultiChatMessage> sendInitChat(ChatInitInfoDto chatInitInfoDto) {
        // 채팅 메시지 리스트 생성
        List<MultiChatMessage> multiChatMessagesList = new ArrayList<>();

        // 프롬프트 생성 및 사용자 메시지 추가
        String prompt = this.makeChatInitRequestDto(chatInitInfoDto);
        multiChatMessagesList.add(new MultiChatMessage("user", prompt));

        // 채팅 메시지 리스트를 기반으로 OpenAI API 요청 객체 생성
        MultiChatRequest multiChatRequest = this.makeMultiChatRequest(multiChatMessagesList);

        // OpenAI API 응답 처리 및 시스템 메시지 추가
        MultiChatResponse multiChatResponse = this.getResponse(
                this.buildHttpEntity(multiChatRequest),
                openAIProperties.getApiURL()
        );
        multiChatMessagesList.add(new MultiChatMessage("system", multiChatResponse.getChoices().get(0).getMessage().getContent()));

        return multiChatMessagesList;
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

    /**
     * OpenAI API 설정 정보, 채팅 메시지 리스트를 담은 MultiChatRequest 객체를 생성합니다.
     *
     * @param multiChatMessages 사용자와 시스템 간의 채팅 메시지 리스트
     * @return OpenAI API 요청을 위한 MultiChatRequest 객체
     */
    private MultiChatRequest makeMultiChatRequest(List<MultiChatMessage> multiChatMessages) {
        return new MultiChatRequest(
                openAIProperties.getModel(),
                multiChatMessages,
                openAIProperties.getMaxTokens(),
                openAIProperties.getTemperature(),
                openAIProperties.getTopP()
        );
    }

    /**
     * MultiChatRequest 객체와 헤더를 포함한 HttpEntity 객체를 생성합니다.
     *
     * @param multiChatRequest OpenAI API 설정 정보, 채팅 메시지 리스트를 담은 MultiChatRequest 객체
     * @return OpenAI API에 보낼 HttpEntity 객체
     */
    private HttpEntity<?> buildHttpEntity(MultiChatRequest multiChatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Authorization", "Bearer " + openAIProperties.getApiKey());
        return new HttpEntity<>(multiChatRequest, headers);
    }

    /**
     * OpenAI API에 요청을 보내고 응답을 반환합니다.
     *
     * @param httpEntity Http 요청 객체
     * @param url OpenAI API URL
     * @return OpenAI API에서 받은 응답을 파싱하여 만든 MultiChatResponse 객체
     */
    private MultiChatResponse getResponse(HttpEntity<?> httpEntity, String url) {
        return restTemplate.postForEntity(url, httpEntity, MultiChatResponse.class).getBody();
    }


}
