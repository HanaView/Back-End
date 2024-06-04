package com.hana.common.exception.consultant;

import com.hana.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ConsultantNotAuthenticationException extends RuntimeException{
    private ErrorCode errorCode;

    public ConsultantNotAuthenticationException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
