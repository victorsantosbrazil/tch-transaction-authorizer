package com.victorsantos.transaction.authorizer.infra.data.model;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "balance")
@IdClass(BalanceModelId.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BalanceModel {
    @Id
    private String accountId;

    @Id
    private BenefitCategory category;

    private BigDecimal totalAmount;
}
