package com.hana.api.card.controller;

import com.hana.api.card.dto.request.CardRequestDto;
import com.hana.api.card.service.CardService;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.common.dto.Response;
import com.hana.common.util.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/cards")
@RestController
@Tag(name = "Card API", description = "카드 상품 가입 및 카드 관련 업무")
public class CardController {

    private final CardService cardService;
    private final Response response;

    @Operation(summary = "카드 상품 등록", description = "카드 상품을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerCard(@Validated @RequestBody CardRequestDto.CardRegisterRequest cardRegisterRequest) {
        return cardService.registerCard(cardRegisterRequest);
    }

    @Operation(summary = "카드 혜택 등록", description = "특정 카드의 혜택을 등록합니다.")
    @PostMapping("/register/benefit")
    public ResponseEntity<?> registerCardBenefit(@Validated @RequestBody CardRequestDto.CardBenefitRegisterRequest cardBenefitRegisterRequest) {

        return cardService.registerCardBenefit(cardBenefitRegisterRequest);
    }

    @Operation(summary = "카드 카테고리 등록", description = "카드 상품의 카테고리를 등록합니다.")
    @PostMapping("/register/cardCategory")
    public ResponseEntity<?> registerCardCategory(@Validated @RequestBody CardRequestDto.CardCategoryRegisterRequest cardCategoryRegisterRequest) {

        return cardService.registerCardCategory(cardCategoryRegisterRequest);
    }

    @Operation(summary = "모든 카드 상품 조회", description = "모든 카드 상품을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<?> getAllCards() {
        return cardService.getAllCards();
    }

    @Operation(summary = "카드 상품 카테고리 조회", description = "카드 상품의 카테고리들을 조회합니다.")
    @GetMapping("/category")
    public ResponseEntity<?> getCardCategory() {
        return cardService.getCardCategory();
    }

    @Operation(summary = "카드 카테고리별 상품 조회", description = "카드 카테고리별로 등록된 카드 상품들을 조회합니다.")
    @Parameter(name = "cardCategoryId", description = "카드 카테고리 ID")
    @GetMapping("/{cardCategoryId}")
    public ResponseEntity<?> getCards(@PathVariable Long cardCategoryId) {
        return cardService.getCards(cardCategoryId);
    }

    @Operation(summary = "카드 상품 가입", description = "특정 카드 상품에 가입합니다.")
    @Parameter(name = "cardId", description = "카드 상품 ID")
    @PostMapping("/{cardId}/join")
    public ResponseEntity<?> joinCard(@PathVariable Long cardId, @Validated @RequestBody UserRequestDto.UserCardRequestDto userCardRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors)); //UserDepositRequestDto 오류가 있는 경우
        }
        return cardService.joinCard(cardId, userCardRequestDto);
    }

    @Operation(summary = "카드 정지", description = "특정 사용자의 특정 카드를 정지합니다.")
    @Parameter(name = "userCardId", description = "사용자 카드 Id")
    @PutMapping("/{userCardId}/cancellation")
    public ResponseEntity<?> cancelUserCard(@PathVariable Long userCardId) {
        return cardService.cancelUserCard(userCardId);
    }

    @Operation(summary = "사용자 발급 카드 리스트 조회", description = "특정 사용자의 모든 카드 상품들을 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID")
    @GetMapping("/{userId}/products")
    public ResponseEntity<?> getUserCards(@PathVariable Long userId) {
        return cardService.getUserCards(userId);
    }

    @Operation(summary = "사용자의 특정 카드 상품 보유 여부", description = "특정 사용자가 특정 카드 상품을 보유하고 있는지 조회합니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID"),
            @Parameter(name = "cardId", description = "카드 상품 ID")
    })
    @GetMapping("/check/{userId}/{cardId}")
    public ResponseEntity<?> checkOwnCard(@PathVariable Long userId, @PathVariable Long cardId) {
        return cardService.checkOwnCard(userId, cardId);
    }
}
