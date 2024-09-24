package com.tosi.chat.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatInitInfoDto {
    private String childName;
    private String characterName;
    private Long taleId;

}
