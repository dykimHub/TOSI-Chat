package com.tosi.chat.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

}
