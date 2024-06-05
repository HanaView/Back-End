package com.hana.api.saving.service;

import com.hana.api.deposit.entity.Deposit;
import com.hana.api.saving.dto.response.SavingResponseDto;
import com.hana.api.saving.entity.Saving;
import com.hana.api.saving.repository.SavingRepository;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.dto.response.UserDepositResponseDto;
import com.hana.api.user.dto.response.UserSavingResponseDto;
import com.hana.api.user.entity.User;
import com.hana.api.user.entity.UserDeposit;
import com.hana.api.user.entity.UserSaving;
import com.hana.api.user.repository.UserDepositRepository;
import com.hana.api.user.repository.UserSavingRepository;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    //사용자의 deposit 정보 가져오기 위해서
    private final UserDepositRepository userDepositRepository;


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

    @Transactional
    public ResponseEntity<?> joinSaving(Long savingId, UserRequestDto.UserSavingRequestDto dto) {
        //2. saving 정보 가져오기
        Saving saving = savingRepository.findById(savingId)
                .orElseThrow();
        User user = userRepository.findById(dto.getUserId()).orElse(null);
        UserDeposit userDeposit = userDepositRepository.getReferenceById(dto.getUserDepositId());

        // userDeposit의 balance 필드에서 dto.getPerMonth() 만큼 감소시키기
        long newBalance = userDeposit.getBalance() - dto.getPerMonth();
        userDeposit.updateBalance(newBalance);
        userDepositRepository.save(userDeposit);


        //계좌번호
        SecureRandom secureRandom = new SecureRandom();
        long randomNumber = secureRandom.nextLong();
        String accountNumber = String.format("%014d", Math.abs(randomNumber) % 100000000000000L);

        //3. 사용자 가입
        UserSaving userSaving = UserSaving.builder()
                .accountNumber(accountNumber)
                .isHuman(false)
                .isLoss(false)
                .balance(dto.getPerMonth())
                .perMonth(dto.getPerMonth())
                .period(dto.getPeriod())
                .user(user)
                .password(dto.getPassword())
                .bounds(300000L)
                .saving(saving)
                .userDeposit(userDepositRepository.getReferenceById(dto.getUserDepositId()))
                .build();
        userSavingRepository.save(userSaving);
        return response.success(new UserSavingResponseDto(userSaving));
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
        List<UserSaving> userSavings = userSavingRepository.findByUserId(userId);
        List<UserSavingResponseDto> responseDtos = userSavings.stream()
                .map(UserSavingResponseDto::new)
                .collect(Collectors.toList());
        return response.success(responseDtos);
    }

    public ResponseEntity<?> getUserSavingById(Long userId, Long savingId) {
        Saving saving = savingRepository.getDepositById(savingId).orElseThrow(() -> new RuntimeException("UserDeposit not found with id " + savingId));
        List<UserSaving> userSavings = userSavingRepository.findByUserIdAndSaving(userId, saving);
        List<UserSavingResponseDto> responseDtos = userSavings.stream()
                .map(UserSavingResponseDto::new)
                .collect(Collectors.toList());
        return  response.success(responseDtos);
    }
}
