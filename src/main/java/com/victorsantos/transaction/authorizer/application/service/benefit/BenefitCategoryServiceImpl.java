package com.victorsantos.transaction.authorizer.application.service.benefit;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BenefitCategoryServiceImpl implements BenefitCategoryService {

    private final MerchantNameBenefitCategory merchantNameBenefitCategory;

    private final MccBenefitCategory mccBenefitCategory;

    @Override
    public BenefitCategory findByMerchantNameAndMcc(String merchantName, String mcc) {
        var nonNullMerchantName = Objects.requireNonNullElse(merchantName, "");
        var optionalMerchantNameCategory = merchantNameBenefitCategory.find(nonNullMerchantName);
        if (optionalMerchantNameCategory.isEmpty()) {
            var nonNullMcc = Objects.requireNonNullElse(mcc, "");
            return mccBenefitCategory.find(nonNullMcc).orElse(BenefitCategory.CASH);
        }
        return optionalMerchantNameCategory.get();
    }
}
