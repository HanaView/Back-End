package com.hana.api.deposit.service;

import com.hana.api.deposit.dto.request.DepositRequestDto;
import com.hana.api.deposit.dto.response.DepositResponseDto;
import com.hana.api.deposit.entity.Deposit;
import com.hana.api.deposit.repository.DepositRepository;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.dto.response.UserDepositResponseDto;
import com.hana.api.user.entity.UserDeposit;
import com.hana.api.user.repository.UserDepositRepository;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.dto.Response;

import com.hana.api.user.entity.User;
import com.hana.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositService {

    //deposit 관련 정보 가져오기
    private final DepositRepository depositRepository;
    //deposit 관련 정보 가져오기
    private final UserRepository userRepository;
    //사용자의 deposit 정보 가져오기 위해서
    private final UserDepositRepository userDepositRepository;

    private final Response response;
    public ResponseEntity<?> getAllDeposits() {
        List<DepositResponseDto> deposits = depositRepository.findAll().stream()
                .map(DepositResponseDto::new)
                .collect(Collectors.toList());
        return response.success(deposits);
    }

    //테스트 함수
    public List<DepositResponseDto> getAllDepositsTest() {
        List<DepositResponseDto> deposits = depositRepository.findAll().stream()
                .map(DepositResponseDto::new)
                .collect(Collectors.toList());
        return deposits;
    }

    //에러처리
    public ResponseEntity<?> getDepositById(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow();
//                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPOSIT_NOT_FOUND));
        return response.success(new DepositResponseDto(deposit));
    }
    //테스트 함수
    public Deposit getDepositByIdTest(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow();
//                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPOSIT_NOT_FOUND));
        return deposit;
    }


    public ResponseEntity<?> joinDeposit(Long depositId, UserRequestDto.UserDepositRequestDto dto) {
        //1. user의 가입정보 가져오기 > 중복가입 방지 //todo
        //2. deposit 정보 가져오기
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow();
        User user = userRepository.findById(dto.getUserId()).orElse(null);

        //계좌번호
        SecureRandom secureRandom = new SecureRandom();
        long randomNumber = secureRandom.nextLong();
        String accountNumber = String.format("%014d", Math.abs(randomNumber) % 100000000000000L);

        //3. 사용자 가입
        UserDeposit userDeposit = UserDeposit.builder()
                .accountNumber(accountNumber)
                .isHuman(false)
                .isLoss(false)
                .balance(dto.getBalance())
                .period(dto.getPeriod())
                .user(user)
                .password(dto.getPassword())
                .bounds(300000L)
                .deposit(deposit)
                .parent(dto.getUserDepositId2() == null ? null : userDepositRepository.getReferenceById(dto.getUserDepositId2()))
                .build();

        userDepositRepository.save(userDeposit);
        return response.success(new UserDepositResponseDto(userDeposit));
    }

    @Transactional
    public ResponseEntity<?> cancelDeposit(Long userDepositId) {
        UserDeposit userDeposit = userDepositRepository.findById(userDepositId)
                .orElseThrow(() -> new RuntimeException("UserDeposit not found with id " + userDepositId));

        if (userDeposit.getParent() != null) {
            UserDeposit parentDeposit = userDeposit.getParent();
            parentDeposit.updateBalance(parentDeposit.getBounds() + userDeposit.getBounds());
            userDeposit.updateBalance(0L);
            userDepositRepository.save(parentDeposit);
        }

        userDeposit.updateIsLoss(true);
        userDepositRepository.save(userDeposit);

        return response.success("Cancelled deposit with id " + userDepositId);
    }

    public ResponseEntity<?> setDormancy(Long userDepositId) {
        UserDeposit userDeposit = userDepositRepository.findById(userDepositId)
                .orElseThrow(() -> new RuntimeException("UserDeposit not found with id " + userDepositId));

        userDeposit.updateDormancy(true);
        userDepositRepository.save(userDeposit);

        return response.success("Set dormancy for deposit with id " + userDepositId);
    }

    @Transactional
    public ResponseEntity<?> getUserDeposits(Long userId) {
        List<UserDeposit> userDeposits = userDepositRepository.findByUserId(userId);
        List<UserDepositResponseDto> responseDtos = userDeposits.stream()
                .map(UserDepositResponseDto::new)
                .collect(Collectors.toList());
        return response.success(responseDtos);
    }

    public ResponseEntity<?> getUserDepositById(Long userId, Long depositId) {
        Deposit deposit = depositRepository.getDepositById(depositId).orElseThrow(() -> new RuntimeException("UserDeposit not found with id " + depositId));
        List<UserDeposit> userDeposits = userDepositRepository.findByUserIdAndDeposit(userId, deposit);
        List<UserDepositResponseDto> responseDtos = userDeposits.stream()
                .map(UserDepositResponseDto::new)
                .collect(Collectors.toList());
        // Fetch user-specific deposit by id logic here
        // This is a placeholder implementation
        return response.success(responseDtos);
    }

    public ResponseEntity<?> checkPw(Long userDepositId, UserRequestDto.CheckPwRequestDto checkPwRequestDto){
        UserDeposit userDeposit = userDepositRepository.findById(userDepositId).get();

        if(userDeposit.getPassword().equals(checkPwRequestDto.getPassword())){
            return response.success();
        }
        return response.fail(ErrorCode.INVALID_DEPOSIT_PASSWORD, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> getWithDrawDepositsById(Long userId){
        List<UserDeposit> deposits = userDepositRepository.findUserDepositsByDeposit_DepositCategoryIdAndUserId(1, userId);
        return response.success(deposits, HttpStatus.OK);
    }
}
