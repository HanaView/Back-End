package com.hana.common.exception;

import com.hana.common.dto.Response;
import com.hana.common.exception.card.CardCategoryRegisterFailException;
import com.hana.common.exception.user.UserNotAuthenticationException;
import com.hana.common.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 200번대 (성공): 요청이 성공적으로 처리되었음을 나타냅니다.
     * 300번대 (리다이렉션): 클라이언트가 요청한 리소스가 다른 위치에 있음을 나타냅니다.
     * 400번대 (클라이언트 오류): 클라이언트의 요청이 잘못되었거나 인증이 필요함을 나타냅니다.
     * 500번대 (서버 오류): 서버에서 요청을 처리하는 중에 오류가 발생했음을 나타냅니다.
     */

    private final Response response;
    @ExceptionHandler(UserNotAuthenticationException.class)
    public ResponseEntity<?> handleUserNotAuthentication(UserNotAuthenticationException ex){
        log.error("UserNotAuthentication : ",ex);
        return response.fail(ex.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){
        log.error("UserNotFoundException : ",ex);

        return response.fail(ex.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CardCategoryRegisterFailException.class)
    public ResponseEntity<?> handleCardCategoryRegisterFailException(CardCategoryRegisterFailException ex){
        log.error("CardCategoryRegisterFailException : ",ex);

        return response.fail(ex.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}