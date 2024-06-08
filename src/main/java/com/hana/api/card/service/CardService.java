package com.hana.api.card.service;

import com.hana.api.card.dto.request.CardRequestDto;
import com.hana.api.card.dto.response.CardCategoryResponseDto;
import com.hana.api.card.dto.response.CardResponseDto;
import com.hana.api.card.entity.Card;
import com.hana.api.card.entity.CardBenefit;
import com.hana.api.card.entity.CardCategory;
import com.hana.api.card.repository.CardBenefitRepository;
import com.hana.api.card.repository.CardCategoryRepository;
import com.hana.api.card.repository.CardRepository;
import com.hana.api.deposit.dto.response.DepositResponseDto;
import com.hana.api.deposit.entity.Deposit;
import com.hana.api.deposit.repository.DepositRepository;
import com.hana.api.user.dto.request.UserRequestDto;
import com.hana.api.user.dto.response.UserCardResponseDto;
import com.hana.api.user.dto.response.UserDepositResponseDto;
import com.hana.api.user.entity.User;
import com.hana.api.user.entity.UserCard;
import com.hana.api.user.entity.UserDeposit;
import com.hana.api.user.repository.UserCardRepository;
import com.hana.api.user.repository.UserDepositRepository;
import com.hana.api.user.repository.UserRepository;
import com.hana.common.dto.Response;
import com.hana.common.exception.ErrorCode;
import com.hana.common.exception.card.*;
import com.hana.common.exception.deposit.ResourceNotFoundException;
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
public class CardService {

    private final CardRepository cardRepository;
    private final CardCategoryRepository cardCategoryRepository;
    private final CardBenefitRepository  cardBenefitRepository;
    private final UserRepository userRepository;
    private final UserCardRepository userCardRepository;
    private final UserDepositRepository userDepositRepository;
    private final Response response;

    public ResponseEntity<?> registerCard(CardRequestDto.CardRegisterRequest cardRegisterRequest){

        log.info("[registerCard]");

        Card card;
        try{
            card = Card.builder()
                    .name(cardRegisterRequest.getName())
                    .annualFee(cardRegisterRequest.getAnnualFee())
                    .brand(cardRegisterRequest.getBrand())
                    .link(cardRegisterRequest.getLink())
                    .spending(cardRegisterRequest.getSpending())
                    .category(cardCategoryRepository.findById(cardRegisterRequest.getCardCategoryId()).get())
                    .build();
        } catch(Exception e){
            throw new CardRegisterFailException(ErrorCode.CARD_REGISTER_FAILED);
        }

        cardRepository.save(card);
        return response.success();
    }

    public ResponseEntity<?> registerCardBenefit(CardRequestDto.CardBenefitRegisterRequest cardBenefitRegisterRequest){
        CardBenefit cardBenefit;
        try{
            cardBenefit = CardBenefit.builder()
                    .content(cardBenefitRegisterRequest.getContent())
                    .card(cardRepository.findById(cardBenefitRegisterRequest.getCardId()).get())
                    .build();
        } catch(Exception e){
            throw new CardBenefitRegisterFailException(ErrorCode.CARD_BENEFIT_REGISTER_FAILED);
        }

        cardBenefitRepository.save(cardBenefit);
        return response.success();
    }

    public ResponseEntity<?> registerCardCategory(CardRequestDto.CardCategoryRegisterRequest cardCategoryRegisterRequest){

        Long parentId = cardCategoryRegisterRequest.getParentId();
        CardCategory cardCategory;

        try {
            cardCategory = CardCategory.builder()
                    .name(cardCategoryRegisterRequest.getName())
                    .parent(parentId == null ? null : cardCategoryRepository.findById(parentId).get())
                    .build();
        } catch (Exception e){
            throw new CardCategoryRegisterFailException(ErrorCode.CARD_CATEGORY_REGISTER_FAILED);
        }

        cardCategoryRepository.save(cardCategory);
        return response.success();
    }

    public ResponseEntity<?> getAllCards() {
        List<CardResponseDto> cards = cardRepository.findAll().stream()
                .map(CardResponseDto::new)
                .toList();

        return response.success(cards);
    }

    public ResponseEntity<?> getCardCategory() {
        List<CardCategoryResponseDto> cardCategoryList = cardCategoryRepository.findAllByParentIdIsNull().stream()
                .map(CardCategoryResponseDto::new)
                .toList();

        return response.success(cardCategoryList);
    }

    public ResponseEntity<?> getCards(Long cardCategoryId) {
        List<CardResponseDto> cards = cardRepository.getCardsByCategoryId(cardCategoryId).stream()
                .map(CardResponseDto::new)
                .toList();

        return response.success(cards);
    }

    public ResponseEntity<?> joinCard(Long cardId, UserRequestDto.UserCardRequestDto userCardRequestDto) {

        try {
            Card card = cardRepository.findById(cardId).get();
            User user = userRepository.findById(userCardRequestDto.getUserId()).get();
            UserDeposit userDeposit = userDepositRepository.findById(userCardRequestDto.getUserDepositId()).get();
            UserCard userCard = UserCard.builder()
                    .card(card).user(user)
                    .userDeposit(userDeposit)
                    .password(userCardRequestDto.getPassword())
                    .build();

            userCardRepository.save(userCard);
        } catch (Exception e){
            throw new CardJoinFailException(ErrorCode.CARD_JOIN_FAILED);
        }

        return response.success();
    }

    @Transactional
    public ResponseEntity<?> cancelUserCard(Long userCardId) {
        UserCard userCard = userCardRepository.findById(userCardId)
                .orElseThrow(() -> new CardCancelFailException(ErrorCode.USERCARD_NOT_FOUND));

        userCardRepository.delete(userCard);

        return response.success();
    }

    @Transactional
    public ResponseEntity<?> getUserCards(Long userId) {
        List<UserCardResponseDto> userCards = userCardRepository.findByUserId(userId).stream()
                .map(UserCardResponseDto::new)
                .toList();
        return response.success(userCards);
    }

    public ResponseEntity<?> checkOwnCard(Long userId, Long cardId) {
        Optional<UserCard> opUserCard = userCardRepository.findByUserIdAndCardId(userId, cardId);

        if(opUserCard.isPresent()){
            UserCard userCard = opUserCard.get();
            return response.success(userCard);
        }else{
            return response.success();
        }
    }
}
