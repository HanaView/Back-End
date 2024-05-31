package com.hana.common.exception;

import lombok.Getter;

@Getter
public class NameDuplicateException extends RuntimeException{

    private ErrorCode errorCode;

    public NameDuplicateException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}