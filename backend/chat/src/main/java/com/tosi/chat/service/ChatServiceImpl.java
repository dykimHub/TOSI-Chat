package com.tosi.chat.service;

import com.tosi.chat.common.config.OpenAIProperties;
import com.tosi.chat.dto.*;
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
        // 채팅 메시지 리스트 초기화
        List<MultiChatMessage> multiChatMessagesList = new ArrayList<>();

        // 프롬프트 생성 및 사용자 메시지 추가
        String initPrompt = this.makeChatInitRequestDto(chatInitInfoDto);
        multiChatMessagesList.add(new MultiChatMessage("user", initPrompt));

        // 채팅 메시지 리스트를 기반으로 OpenAI API 요청 객체 생성
        MultiChatRequest multiChatRequest = this.makeMultiChatRequest(multiChatMessagesList);

        // OpenAI API 응답 처리 및 시스템 메시지 추가
        MultiChatResponse multiChatResponse = this.getResponse(this.buildHttpEntity(multiChatRequest));
        multiChatMessagesList.add(new MultiChatMessage("system", multiChatResponse.getChoices().get(0).getMessage().getContent()));

        return multiChatMessagesList;
    }

    /**
     * 채팅 메시지 리스트에 기존 채팅 메시지와 사용자가 보낸 새로운 메시지를 추가하고 OpenAI API에 메시지를 요청합니다.
     * 응답 메시지를 받아 사용자와 시스템 간의 새로운 메시지 리스트를 반환합니다.
     *
     * @param chatRequestDto 이전 채팅 메시지 리스트와 사용자가 보낸 새로운 메시지가 담긴 ChatRequestDto 객체
     * @return 사용자가 보낸 메시지와 OpenAI 응답 메시지를 포함한 MultiChatMessage 객체 리스트
     */
    @Override
    public List<MultiChatMessage> sendChat(ChatRequestDto chatRequestDto) {
        // 이전 채팅 메시지를 사용하여 새 채팅 메시지 리스트 초기화
        List<MultiChatMessage> updatedMultiChatMessageList = new ArrayList<>(chatRequestDto.getPreMultiChatMessageList());

        // 채팅 메시지 리스트를 기반으로 OpenAI API 요청 객체 생성
        updatedMultiChatMessageList.add(new MultiChatMessage("user", chatRequestDto.getMultiChatMessage()));
        MultiChatRequest multiChatRequest = this.makeMultiChatRequest(updatedMultiChatMessageList);

        // OpenAI API 응답 처리 및 시스템 메시지 추가
        MultiChatResponse multiChatResponse = this.getResponse(this.buildHttpEntity(multiChatRequest));
        updatedMultiChatMessageList.add(new MultiChatMessage("system", multiChatResponse.getChoices().get(0).getMessage().getContent()));

        return updatedMultiChatMessageList;
    }

    /**
     * 채팅 메시지 리스트에 기존 채팅 메시지와 사용자가 보낸 새로운 메시지, 마지막 인사 프롬프트를 추가하고 OpenAI API에 메시지를 요청합니다.
     * 응답 메시지를 받아 사용자와 시스템 간의 새로운 메시지 리스트를 반환합니다.
     *
     * @param chatRequestDto 이전 채팅 메시지 리스트와 사용자가 보낸 새로운 메시지가 담긴 ChatRequestDto 객체
     * @return 사용자가 보낸 메시지와 OpenAI 응답 메시지를 포함한 MultiChatMessage 객체 리스트
     */
    @Override
    public List<MultiChatMessage> sendFinalChat(ChatRequestDto chatRequestDto) {
        List<MultiChatMessage> updatedMultiChatMessageList = new ArrayList<>(chatRequestDto.getPreMultiChatMessageList());

        String finalPrompt = chatRequestDto.getMultiChatMessage() + ChatFinalRequestDto.finalPrompt;
        updatedMultiChatMessageList.add(new MultiChatMessage("user", finalPrompt));
        MultiChatRequest multiChatRequest = this.makeMultiChatRequest(updatedMultiChatMessageList);

        MultiChatResponse multiChatResponse = this.getResponse(this.buildHttpEntity(multiChatRequest));
        updatedMultiChatMessageList.add(new MultiChatMessage("system", multiChatResponse.getChoices().get(0).getMessage().getContent()));

        return updatedMultiChatMessageList;
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
                .getInitPrompt();
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
     * @return OpenAI API에서 받은 응답을 파싱하여 만든 MultiChatResponse 객체
     */
    private MultiChatResponse getResponse(HttpEntity<?> httpEntity) {
        return restTemplate.postForEntity(openAIProperties.getApiURL(), httpEntity, MultiChatResponse.class).getBody();
    }


}
