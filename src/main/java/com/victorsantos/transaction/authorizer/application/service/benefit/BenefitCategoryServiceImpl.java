package com.victorsantos.transaction.authorizer.application.service.benefit;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
class BenefitCategoryServiceImpl implements BenefitCategoryService {

    static final Map<String, BenefitCategory> mccToCategoryMap = Map.of(
            "5411", BenefitCategory.FOOD,
            "5412", BenefitCategory.FOOD,
            "5811", BenefitCategory.MEAL,
            "5812", BenefitCategory.MEAL);

    @Override
    public BenefitCategory findByMcc(String mcc) {
        return mccToCategoryMap.getOrDefault(mcc, BenefitCategory.CASH);
    }
}
