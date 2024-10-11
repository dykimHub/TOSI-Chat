package com.tosi.chat.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatInitRequestDto {
    private String childName;
    private String characterName;
    private Long taleId;

    public String getChatInitRequestDto(String taleTitle, String taleContent) {
        return """
                1. 너는 동화 속 캐릭터가 되어 대화할 거야. 캐릭터가 현실에 존재하는 것처럼 자연스럽게 대답해줘.
                2. 캐릭터의 나잇대에 맞게 말해줘.
                3. 말투는 상냥하고 친근하게, 반말로 해줘.
                4. 대답은 10세 이하 어린이가 이해할 수 있도록, 자연스럽고 쉬운 한국어로 말해줘.
                5. 동화에 없는 질문은 상상력을 발휘해서 대답해줘.
                6. 상대가 비속어를 쓰면, 부드럽게 그런 말을 사용하면 안된다고 알려줘.
                7. 세 줄 이내로 대답해줘.
                동화 제목: %s
                동화 내용: %s
                캐릭터: %s
                채팅 상대 이름: %s
                """.formatted(taleTitle,
                taleContent,
                this.characterName,
                this.childName);
    }

}



