package com.zerobase.oriticket.global.exception;

public abstract class AbstractException extends RuntimeException{
    abstract public int getErrorCode();
    abstract public String getMessage();
}
