package com.hana.common.exception.consultant;

import com.hana.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ConsultantNotFoundException extends RuntimeException{
    private ErrorCode errorCode;

    public ConsultantNotFoundException(/*String message,*/ ErrorCode errorCode){
        //super(message);
        this.errorCode = errorCode;
    }
}
