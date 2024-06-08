package com.hana.api.saving.controller;
import com.hana.api.deposit.dto.request.DepositRequestDto;
import com.hana.api.saving.dto.request.SavingRequestDto;
import com.hana.api.saving.service.SavingService;
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
@RequestMapping("/api/saving")
@RestController
@Tag(name = "saving Open API", description = "적금 서비스 가입, 휴면, 조회 처리")
public class SavingController {

    private final SavingService savingService;
    private final Response response;
    //OK
    @Operation(summary = "모든 적금 상품 조회", description = "모든 적금 상품을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<?> getAllSavings() {
        return savingService.getAllSavings();
    }

    @Operation(summary = "특정 적금 상품 조회", description = "특정 적금 상품을 ID로 조회합니다.")
    @Parameter(name = "savingId", description = "적금 상품 ID")
    @GetMapping("/{savingId}")
    public ResponseEntity<?> getSavingById(@PathVariable Long savingId) {
        return savingService.getSavingById(savingId);
    }

    @Operation(summary = "적금 상품 가입", description = "특정 적금 상품에 가입합니다.")
    @Parameter(name = "savingId", description = "적금 상품 ID")
    @PostMapping("/{savingId}/join")
    public ResponseEntity<?> joinSaving(@PathVariable Long savingId, @Validated @RequestBody UserRequestDto.UserSavingRequestDto dto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors)); //UserSavingRequestDto 오류가 있는 경우
        }
        return savingService.joinSaving(savingId, dto);
    }

    @Operation(summary = "적금 해지", description = "특정 사용자의 적금을 해지합니다.")
    @Parameter(name = "userSavingId", description = "사용자 적금 ID")
    @PutMapping("/{userSavingId}/cancellation")
    public ResponseEntity<?> cancelUserSaving(@PathVariable Long userSavingId) {
        return savingService.cancelSaving(userSavingId);
    }

    @Operation(summary = "적금 휴면", description = "특정 사용자의 적금을 휴면 상태로 만듭니다.")
    @Parameter(name = "userSavingId", description = "사용자 적금 ID")
    @PutMapping("/{userSavingId}/dormancy")
    public ResponseEntity<?> dormancyUserSaving(@PathVariable Long userSavingId) {
        return savingService.setDormancy(userSavingId);
    }

    @Operation(summary = "적금 상품 등록", description = "예금 상품을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerDeposit(@Validated @RequestBody SavingRequestDto.SavingRegisterRequest savingRequestDto) {
        return savingService.registerSaving(savingRequestDto);
    }

    @Operation(summary = "사용자 적금 상품 조회", description = "특정 사용자의 모든 적금 상품을 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID")
    @GetMapping("/{userId}/products")
    public ResponseEntity<?> getUserSavings(@PathVariable Long userId) {
        return savingService.getUserSavings(userId);
    }

    @Operation(summary = "사용자의 특정 적금 상품 조회", description = "특정 사용자의 특정 적금 상품을 조회합니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID"),
            @Parameter(name = "savingId", description = "적금 상품 ID")
    })
    @GetMapping("/{userId}/{savingId}")
    public ResponseEntity<?> getUserSavingById(@PathVariable Long userId, @PathVariable Long savingId) {
        return savingService.getUserSavingById(userId, savingId);
    }
    @Operation(summary = "적금 카테고리 등록", description = "예금 상품의 카테고리를 등록합니다.")
    @PostMapping("/register/SavingCategory")
    public ResponseEntity<?> registerDepositCategory(@Validated @RequestBody SavingRequestDto.SavingCategoryRegisterRequest savingCategoryRegisterRequest) {
        return savingService.registerSavingCategory(savingCategoryRegisterRequest);
    }

    @Operation(summary = "적금 상품 연관 이율 등록", description = "적금 상품 연관 이율 등록을 등록합니다.")
    @PostMapping("/register/SavingRateCategory")
    public ResponseEntity<?> registerDepositRate(@Validated @RequestBody SavingRequestDto.SavingRateRegisterRequest savingRateRegisterRequest) {

        return savingService.registerSavingRate(savingRateRegisterRequest);
    }
    @Operation(summary = "적금 상품 카테고리 조회", description = "적금 상품의 카테고리들을 조회합니다.")
    @GetMapping("/category")
    public ResponseEntity<?> getSavingCategory() {
        return savingService.getSavingCategory();
    }

}
