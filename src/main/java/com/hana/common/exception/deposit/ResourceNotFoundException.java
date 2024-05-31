package com.hana.common.exception.deposit;

import com.hana.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException {
    private ErrorCode errorCode;

    public ResourceNotFoundException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
