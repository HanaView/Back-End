package com.hana.api.deposit.controller;
import com.hana.api.card.dto.request.CardRequestDto;
import com.hana.api.deposit.dto.request.DepositRequestDto;
import com.hana.api.deposit.service.DepositService;
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
@RequestMapping("/api/deposit")
@RestController
@Tag(name = "Deposit API", description = "예금 서비스 가입, 휴면, 조회 처리")
public class DepositController {

    private final DepositService depositService;
    private final Response response;

    @Operation(summary = "예금 상품 등록", description = "예금 상품을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerDeposit(@Validated @RequestBody DepositRequestDto.DepositRegisterRequest depositRegisterRequest) {
        return depositService.registerDeposit(depositRegisterRequest);
    }

    @Operation(summary = "예금 상품 연관 이율 등록", description = "예금 상품 연관 이율 등록을 등록합니다.")
    @PostMapping("/register/DepositRateCategory")
    public ResponseEntity<?> registerDepositRate(@Validated @RequestBody DepositRequestDto.DepositRateRegisterRequest depositRateRegisterRequest) {

        return depositService.registerDepositRate(depositRateRegisterRequest);
    }

    @Operation(summary = "예금 카테고리 등록", description = "예금 상품의 카테고리를 등록합니다.")
    @PostMapping("/register/DepositCategory")
    public ResponseEntity<?> registerDepositCategory(@Validated @RequestBody DepositRequestDto.DepositCategoryRegisterRequest depositCategoryRegisterRequest) {
        return depositService.registerDepositCategory(depositCategoryRegisterRequest);
    }

    @Operation(summary = "예금 상품 카테고리 조회", description = "예금 상품의 카테고리들을 조회합니다.")
    @GetMapping("/category")
    public ResponseEntity<?> getDepositCategory() {
        return depositService.getDepositCategory();
    }


    //OK
    @Operation(summary = "모든 예금 상품 조회", description = "모든 예금 상품을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<?> getAllDeposits() {
        return depositService.getAllDeposits();
    }

    @Operation(summary = "특정 예금 상품 조회", description = "특정 예금 상품을 ID로 조회합니다.")
    @Parameter(name = "depositId", description = "예금 상품 ID")
    @GetMapping("/{depositId}")
    public ResponseEntity<?> getDepositById(@PathVariable Long depositId) {
        return depositService.getDepositById(depositId);
    }

    @Operation(summary = "예금 상품 가입", description = "특정 예금 상품에 가입합니다.")
    @Parameter(name = "depositId", description = "예금 상품 ID")
    @PostMapping("/{depositId}/join")
    public ResponseEntity<?> joinDeposit(@PathVariable Long depositId, @Validated @RequestBody UserRequestDto.UserDepositRequestDto dto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors)); //UserDepositRequestDto 오류가 있는 경우
        }
        return depositService.joinDeposit(depositId, dto);
    }

    @Operation(summary = "예금 해지", description = "특정 사용자의 예금을 해지합니다.")
    @Parameter(name = "userDepositId", description = "사용자 예금 ID")
    @PutMapping("/{userDepositId}/cancellation")
    public ResponseEntity<?> cancelUserDeposit(@PathVariable Long userDepositId) {
        return depositService.cancelDeposit(userDepositId);
    }

    @Operation(summary = "예금 휴면", description = "특정 사용자의 예금을 휴면 상태로 만듭니다.")
    @Parameter(name = "userDepositId", description = "사용자 예금 ID")
    @PutMapping("/{userDepositId}/dormancy")
    public ResponseEntity<?> dormancyUserDeposit(@PathVariable Long userDepositId) {
        return depositService.setDormancy(userDepositId);
    }

    @Operation(summary = "사용자 예금 상품 조회", description = "특정 사용자의 모든 예금 상품을 조회합니다.")
    @Parameter(name = "userId", description = "사용자 ID")
    @GetMapping("/{userId}/products")
    public ResponseEntity<?> getUserDeposits(@PathVariable Long userId) {

        return depositService.getUserDeposits(userId);
    }

    @Operation(summary = "사용자의 특정 예금 상품 조회", description = "특정 사용자의 특정 예금 상품을 조회합니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID"),
            @Parameter(name = "depositId", description = "예금 상품 ID")
    })
    @GetMapping("/{userId}/{depositId}")
    public ResponseEntity<?> getUserDepositById(@PathVariable Long userId, @PathVariable Long depositId) {
        return depositService.getUserDepositById(userId, depositId);
    }

    @Operation(summary = "사용자의 자유로운 입출금 계좌 조회", description = "특정 사용자의 특자유로운 입출금 계좌를 조회합니다.")
    @Parameters({
            @Parameter(name = "userId", description = "사용자 ID"),
    })
    @GetMapping("/{userId}/withdraws")
    public ResponseEntity<?> getWithDrawDepositsById(@PathVariable Long userId) {
        return depositService.getWithDrawDepositsById(userId);
    }

    @Operation(summary = "유저 입출금 계좌 비밀번호 인증", description = "출금 계좌 설정을 위해 유저의 입출금 계좌 비밀번호를 확인합니다.")
    @Parameter(name = "userDepositId", description = "입출금 계좌 ID")
    @PostMapping("/check/{userDepositId}")
    public ResponseEntity<?> joinDeposit(@PathVariable Long userDepositId, @Validated @RequestBody UserRequestDto.CheckPwRequestDto checkPwRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors)); //CheckPwRequestDto 오류가 있는 경우
        }
        return depositService.checkPw(userDepositId, checkPwRequestDto);
    }


}
