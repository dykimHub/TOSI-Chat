package com.tosi.chat.service;

import com.tosi.chat.common.config.ChatGptProperties;
import com.tosi.chat.common.exception.CustomException;
import com.tosi.chat.common.exception.ExceptionCode;
import com.tosi.chat.dto.*;
import com.tosi.chat.repository.TaleDetailDtoRedisRepository;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatRequest;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private static final String ROLE_USER = "user";
    private static final String ROLE_SYSTEM = "system";
    private static final String TALE_CACHE_PREFIX = "taleDetailCache::";
    private final ChatGptProperties chatGptProperties;
    private final RestTemplate restTemplate;
    private final TaleDetailDtoRedisRepository taleDetailDtoRedisRepository;
    @Value("${openai.api-key}")
    private String apiKey;
    @Value("${service.tale.url}")
    private String taleURL;
    @Value("${service.user.url}")
    private String userURL;

    /**
     * 동화 제목, 동화 내용, 사용자, 선택한 등장인물 정보로 채팅을 시작합니다.
     * OpenAI API에 요청한 메시지와 응답받은 메시지를 담은 리스트를 반환합니다.
     *
     * @param chatInitRequestDto 채팅을 시작할 때 필요한 학습 정보가 담긴 ChatInitInfoDto 객체
     * @return 사용자와 시스템 간의 채팅 메시지를 담은 MultiChatMessage 객체 리스트
     */
    @Override
    public List<MultiChatMessage> sendInitChat(ChatInitRequestDto chatInitRequestDto) {
        List<MultiChatMessage> multiChatMessageList = new ArrayList<>();
        // 프롬프트 생성 및 사용자 메시지 추가
        String initPrompt = this.makeChatInitPrompt(chatInitRequestDto);
        multiChatMessageList.add(new MultiChatMessage(ROLE_SYSTEM, initPrompt));
        return processChatRequest(multiChatMessageList);
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
        // 이전 채팅 메시지를 사용하여 리스트 초기화 및 새로운 사용자 메시지 추가
        List<MultiChatMessage> updatedMultiChatMessageList = new ArrayList<>(chatRequestDto.getPreMultiChatMessageList());
        updatedMultiChatMessageList.add(new MultiChatMessage(ROLE_USER, chatRequestDto.getMultiChatMessage()));
        return processChatRequest(updatedMultiChatMessageList);
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
        // 이전 채팅 메시지를 사용하여 새 채팅 메시지 리스트 초기화
        List<MultiChatMessage> updatedMultiChatMessageList = new ArrayList<>(chatRequestDto.getPreMultiChatMessageList());
        // 새로운 사용자 메시지와 마지막 인사 프롬프트 추가
        updatedMultiChatMessageList.add(new MultiChatMessage(ROLE_USER, chatRequestDto.getMultiChatMessage()));
        updatedMultiChatMessageList.add(new MultiChatMessage(ROLE_SYSTEM, ChatFinalRequestDto.finalPrompt));
        return processChatRequest(updatedMultiChatMessageList);
    }

    /**
     * Redis에서 동화 정보 캐시를 먼저 확인하고, 없으면 Tale 서비스에서 동화 정보를 가져옵니다.
     * 가져온 동화 정보를 바탕으로 채팅에 사용할 초기 프롬프트를 생성합니다.
     *
     * @param chatInitRequestDto 사용자, 동화 정보가 담긴 ChatInitRequest 객체
     * @return 채팅 시작용 프롬프트
     */
    private String makeChatInitPrompt(ChatInitRequestDto chatInitRequestDto) {
        Long taleId = chatInitRequestDto.getTaleId();
        TaleDetailDto taleDetailDto = taleDetailDtoRedisRepository.findById(TALE_CACHE_PREFIX + taleId)
                .orElse(restTemplate.getForObject(taleURL + "/content/" + taleId, TaleDetailDto.class));

        return chatInitRequestDto.getChatInitRequestDto(taleDetailDto.getTitle(), taleDetailDto.getContent());
    }

    /**
     * MultiChatRequest 객체를 생성하고 이를 감싼 HttpEntity 객체로 OpenAI API에 요청을 보냅니다.
     * 응답받은 시스템 메시지를 기존 채팅 메시지 리스트에 추가한 후 업데이트된 리스트를 반환합니다.
     *
     * @param multiChatMessageList 사용자와 시스템 간의 기존 채팅 메시지 리스트
     * @return OpenAI API 응답을 반영한 업데이트된 채팅 메시지 리스트
     */
    private List<MultiChatMessage> processChatRequest(List<MultiChatMessage> multiChatMessageList) {
        // MultiChatRequest 객체 생성
        MultiChatRequest multiChatRequest = makeMultiChatRequest(multiChatMessageList);

        // OpenAI API에 요청 보내고 받은 응답을 추가
        MultiChatResponse multiChatResponse = restTemplate.postForEntity(
                        chatGptProperties.getApiURL(),
                        buildHttpEntity(multiChatRequest),
                        MultiChatResponse.class)
                .getBody();
        multiChatMessageList.add(new MultiChatMessage(ROLE_SYSTEM, multiChatResponse.getChoices().get(0).getMessage().getContent()));

        return multiChatMessageList;
    }


    /**
     * OpenAI API 설정 정보, 채팅 메시지 리스트를 담은 MultiChatRequest 객체를 생성합니다.
     *
     * @param multiChatMessageList 사용자와 시스템 간의 채팅 메시지 리스트
     * @return OpenAI API 요청을 위한 MultiChatRequest 객체
     */
    private MultiChatRequest makeMultiChatRequest(List<MultiChatMessage> multiChatMessageList) {
        return new MultiChatRequest(
                chatGptProperties.getModel(),
                multiChatMessageList,
                chatGptProperties.getMaxTokens(),
                chatGptProperties.getTemperature(),
                chatGptProperties.getTopP()
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
        headers.add("Authorization", "Bearer " + apiKey);
        return new HttpEntity<>(multiChatRequest, headers);
    }

    /**
     * 회원 서비스로 토큰을 보내고 인증이 완료되면 회원 번호를 반환합니다.
     *
     * @param accessToken 로그인한 회원의 토큰
     * @return 회원 번호
     * @throws CustomException 인증에 성공하지 못하면 예외 처리
     */
    @Override
    public Long findUserAuthorization(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            Long userId = restTemplate.exchange(userURL + "/auth",
                    HttpMethod.GET, httpEntity, Long.class).getBody();
            return userId;
        } catch (Exception e) {
            throw new CustomException(ExceptionCode.INVALID_TOKEN);
        }
    }


}
