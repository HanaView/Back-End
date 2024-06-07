package com.hana.common.exception.card;

import com.hana.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CardBenefitRegisterFailException extends RuntimeException{
    private ErrorCode errorCode;

    public CardBenefitRegisterFailException(/*String message,*/ ErrorCode errorCode){
        //super(message);
        this.errorCode = errorCode;
    }
}
