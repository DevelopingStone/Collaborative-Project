package com.zerobase.oriticket.global.exception.impl;

import com.zerobase.oriticket.global.exception.AbstractException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomException extends AbstractException {

    private final int errorCode;
    private final String message;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
