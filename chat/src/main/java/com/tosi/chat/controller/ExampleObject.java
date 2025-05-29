package com.tosi.chat.controller;

public class ExampleObject {
    public static final String init = """
            {
              "childName": "연아",
              "characterName": "토끼",
              "taleId": 6
            }
            """;

    public static final String progress = """
            {
              "preMultiChatMessageList": [
                  {
                    "role": "이전 채팅 리스트를",
                    "content": "붙여 넣어 주세요."
                  }
                ],
              "multiChatMessage": "새로운 채팅을 입력해 주세요."
            }
            """;

}