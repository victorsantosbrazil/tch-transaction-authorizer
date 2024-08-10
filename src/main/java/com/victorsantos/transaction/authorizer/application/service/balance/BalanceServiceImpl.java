package com.victorsantos.transaction.authorizer.application.service.balance;

import com.victorsantos.transaction.authorizer.domain.entity.Balance;
import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import com.victorsantos.transaction.authorizer.infra.data.BalanceRepository;
import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModel;
import com.victorsantos.transaction.authorizer.infra.data.model.BalanceModelId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository repository;

    private final BalanceServiceMapper mapper;

    @Override
    public Optional<Balance> findById(String accountId, BenefitCategory category) {
        var modelId = new BalanceModelId(accountId, category);
        Optional<BalanceModel> optionalModel = repository.findById(modelId);
        return optionalModel.map(mapper::toEntity);
    }

    @Override
    public void save(Balance balance) {
        var model = mapper.toModel(balance);
        repository.save(model);
    }
}
