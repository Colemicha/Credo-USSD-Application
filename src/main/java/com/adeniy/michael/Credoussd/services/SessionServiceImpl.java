package com.adeniy.michael.Credoussd.services;

import com.adeniy.michael.Credoussd.entity.Account;
import com.adeniy.michael.Credoussd.entity.UssdSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final RedisTemplate<String, Object> template;
    private final SmsService smsService;
    private final String HASH_KEY = "sessions";


    @Override
    public UssdSession createUssdSession(UssdSession session){
        String acctNo = RandomStringUtils.randomNumeric(10);
        session.setSessionId(UUID.randomUUID().toString().split("-")[0]);
        Account acc = new Account();
        acc.setAccountNumber(acctNo);
        acc.setAccountBalance(BigDecimal.ZERO);
        session.setAccount(acc);
        template.opsForHash().put(HASH_KEY, session.getId(), session);
        smsService.sendSms(session.getPhoneNumber(), "Your new account number is "+acctNo);
        return session;
    }

    @Override
    public UssdSession get(String id){
        return (UssdSession) template.opsForHash().get(HASH_KEY, id);
    }

    @Override
    public UssdSession update(UssdSession session) {
        Object obj  = template.opsForHash().get(HASH_KEY, session.getId());
        if (!Objects.isNull(obj)) {
            template.opsForHash().put(HASH_KEY, session.getId(), session);
            return session;
        }
        throw new IllegalArgumentException("Session must have an id to be updated");
    }


}
