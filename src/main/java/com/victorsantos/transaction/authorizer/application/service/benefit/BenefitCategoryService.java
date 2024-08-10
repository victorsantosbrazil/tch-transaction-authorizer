package com.victorsantos.transaction.authorizer.application.service.benefit;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;

public interface BenefitCategoryService {
    BenefitCategory findByMcc(String mcc);
}
