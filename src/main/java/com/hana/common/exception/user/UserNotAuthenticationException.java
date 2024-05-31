package com.hana.common.exception.user;

import com.hana.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserNotAuthenticationException extends RuntimeException{
    private ErrorCode errorCode;

    public UserNotAuthenticationException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
