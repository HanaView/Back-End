package com.hana.common.exception.card;

import com.hana.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CardRegisterFailException extends RuntimeException{
    private ErrorCode errorCode;

    public CardRegisterFailException(/*String message,*/ ErrorCode errorCode){
        //super(message);
        this.errorCode = errorCode;
    }
}
