package com.adeniy.michael.Credoussd.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Data
public class Account {
    private String accountNumber;
    private BigDecimal accountBalance;
}
