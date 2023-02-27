package com.adeniy.michael.Credoussd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Embedded;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "sessions", timeToLive = 180)
public class UssdSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String sessionId;
    private String serviceCode;
    private String phoneNumber;

    @Embedded
    private Account account;
    private String text;
    private String previousMenuLevel;
    private String currentMenuLevel;

}
