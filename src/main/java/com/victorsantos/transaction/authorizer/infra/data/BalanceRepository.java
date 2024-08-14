package com.victorsantos.transaction.authorizer.infra.data;

import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModel;
import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<BalanceModel, BalanceModelId> {}
