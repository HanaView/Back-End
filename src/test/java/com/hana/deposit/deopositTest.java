package com.hana.deposit;

import com.hana.HanaViewApplication;
import com.hana.api.deposit.dto.response.DepositResponseDto;
import com.hana.api.deposit.entity.Deposit;
import com.hana.api.deposit.repository.DepositRepository;
import com.hana.api.deposit.service.DepositService;
import com.hana.api.user.dto.request.UserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Slf4j
public class deopositTest {
    @Autowired
    DepositService depositService;
    @Test
    @Transactional
    public void select() {
        List<DepositResponseDto> items = depositService.getAllDepositsTest();
        for(DepositResponseDto d : items){
            System.out.println("예금 정보" + d.toString());
        }
    }
    @Test
    @Transactional
    public void selectOne() {
        Deposit item = depositService.getDepositByIdTest(5L);
        System.out.println("예금 정보" + item.getName() + ", " + item.getId());
    }

    @Test
    public void delete() {
        UserRequestDto.Auth auth = new UserRequestDto.Auth();
        auth.setName("바보");
        auth.setTele("01080195950");
    }

}
