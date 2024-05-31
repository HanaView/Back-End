package com.hana.api.saving.service;

import com.hana.api.saving.dto.response.SavingResponseDto;
import com.hana.api.saving.entity.Saving;
import com.hana.api.saving.repository.SavingRepository;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.entity.User;
import com.hana.api.user.entity.UserSaving;
import com.hana.api.user.repository.UserSavingRepository;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingService {

    //saving 관련 정보 가져오기
    private final SavingRepository savingRepository;
    //saving 관련 정보 가져오기
    private final UserRepository userRepository;
    //사용자의 saving 정보 가져오기 위해서
    private final UserSavingRepository userSavingRepository;

    private final Response response;
    public ResponseEntity<?> getAllSavings() {
        List<SavingResponseDto> savings = savingRepository.findAll().stream()
                .map(SavingResponseDto::new)
                .collect(Collectors.toList());
        return response.success(savings);
    }

    //에러처리
    public ResponseEntity<?> getSavingById(Long savingId) {
        Saving saving = savingRepository.findById(savingId)
                .orElseThrow();
//                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPOSIT_NOT_FOUND));
        return response.success(new SavingResponseDto(saving));
    }

    public ResponseEntity<?> joinSaving(Long savingId, UserRequestDto.UserSavingRequestDto dto) {
        //1. user의 가입정보 가져오기 > 중복가입 방지 //todo
        //2. saving 정보 가져오기
        Saving saving = savingRepository.findById(savingId)
                .orElseThrow();
        User user = userRepository.findById(dto.getUserId()).orElse(null);

        //계좌번호
        SecureRandom secureRandom = new SecureRandom();
        long randomNumber = secureRandom.nextLong();
        String accountNumber = String.format("%014d", Math.abs(randomNumber) % 100000000000000L);

        //3. 사용자 가입
        UserSaving userSaving = UserSaving.builder()
                .accountNumber(accountNumber)
                .isHuman(false)
                .isLoss(false)
                .balance(dto.getBalance())
                .period(dto.getPeriod())
                .user(user)
                .password(dto.getPassword())
                .bounds(300000L)
                .saving(saving)
                .build();
        userSavingRepository.save(userSaving);
        return response.success(userSaving);
    }

    public ResponseEntity<?> cancelSaving(Long userSavingId) {
        // Saving cancellation logic here
        // This is a placeholder implementation
        return response.success("Cancelled saving with id " + userSavingId);
    }

    public ResponseEntity<?> setDormancy(Long userSavingId) {
        // Set saving dormancy logic here
        // This is a placeholder implementation
        return response.success("Set dormancy for saving with id " + userSavingId);
    }

    public ResponseEntity<?> getUserSavings(Long userId) {
        // Fetch user-specific savings logic here
        // This is a placeholder implementation
        return response.success("Fetched savings for user with id " + userId);
    }

    public ResponseEntity<?> getUserSavingById(Long userId, Long savingId) {
        // Fetch user-specific saving by id logic here
        // This is a placeholder implementation
        return response.success("Fetched saving with id " + savingId + " for user with id " + userId);
    }
}
