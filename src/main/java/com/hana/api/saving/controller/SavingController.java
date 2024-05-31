package com.hana.api.saving.controller;
import com.hana.api.saving.service.SavingService;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.common.dto.Response;
import com.hana.common.util.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/saving")
@RestController
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

//    @Operation(summary = "적금 해지", description = "특정 사용자의 적금을 해지합니다.")
//    @Parameter(name = "userSavingId", description = "사용자 적금 ID")
//    @PutMapping("/{userSavingId}/cancellation")
//    public ResponseEntity<?> cancelUserSaving(@PathVariable Long userSavingId) {
//        return savingService.cancelUserSaving(userSavingId);
//    }
//
//    @Operation(summary = "적금 휴면", description = "특정 사용자의 적금을 휴면 상태로 만듭니다.")
//    @Parameter(name = "userSavingId", description = "사용자 적금 ID")
//    @PutMapping("/{userSavingId}/dormancy")
//    public ResponseEntity<?> dormancyUserSaving(@PathVariable Long userSavingId) {
//        return savingService.dormancyUserSaving(userSavingId);
//    }

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
}
