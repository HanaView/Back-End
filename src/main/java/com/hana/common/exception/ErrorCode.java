package com.hana.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ErrorCode {

    // Auth
    AUTH_FAIL("A001", "인증 실패"),

    // User
    USER_NOT_FOUND("U001","등록된 회원이 아닙니다."),
    USER_NOT_AUTHENTICATION("U002", "인증된 회원이 아닙니다."),
    USER_DUPLICATION("U003","이미 존재하는 유저 정보입니다."),
    USER_UNAUTHORIZED("U004", "유저 인증에 실패했습니다."),

    // Consultant
    CONSULTANT_NOT_FOUND("C001","등록된 상담원이 아닙니다."),
    CONSULTANT_NOT_AUTHENTICATION("C002", "인증된 상담원이 아닙니다."),
    CONSULTANT_DUPLICATION("C003","이미 존재하는 아이디 입니다."),
    CONSULTANT_UNAUTHORIZED("C004", "상담원 인증에 실패했습니다."),

    // Token
    INVALID_REFRESHTOKEN("T001", "유효하지 않은 리프레쉬 토큰 입니다."),
    NOT_FOUND_REFRESHTOKEN("T002", "리프레쉬 토큰이 존재하지 않습니다."),
    INVALID_ACCESSTOKEN("T003", "유효하지 않은 액세스 토큰 입니다."),
    NOT_FOUND_ACCESSTOKEN("T004", "액세스 토큰이 존재하지 않습니다."),

    // Deposit
    DEPOSIT_NOT_FOUND("U099", "예금이 존재하지 않습니다."),
    INVALID_DEPOSIT_PASSWORD("D002", "입출금 계좌의 비밀번호가 일치하지 않습니다."),

    // Saving

    // Card
    CARD_JOIN_FAILED("C001", "카드 가입에 실패했습니다. 연관 데이터들의 존재 유무를 확인해주세요."),
    CARD_REGISTER_FAILED("C002", "카드 상품 등록이 실패했습니다. 연관 데이터들의 존재 유무를 확인해주세요."),
    CARD_BENEFIT_REGISTER_FAILED("C003", "카드 혜택 등록에 실패했습니다. 존재하는 카드인지 확인해주세요."),
    CARD_CATEGORY_REGISTER_FAILED("C004", "카드 카테고리 등록에 실패했습니다. 정보를 다시 확인해주세요."),

    // UserCard
    USERCARD_NOT_FOUND("UC001", "가입한 카드 정보가 존재하지 않습니다.")
    ;

    private final String errorCode;
    private final String message;

    @JsonValue
    public ErrorDetails toJson() {
        return new ErrorDetails(errorCode, message);
    }

    @AllArgsConstructor
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetails {
        @JsonProperty("errorCode")
        private final String errorCode;
        @JsonProperty("message")
        private final String message;
    }
}
