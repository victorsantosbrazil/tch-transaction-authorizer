package com.victorsantos.transaction.authorizer.application.service.benefit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BenefitCategoryServiceImpl.class})
class BenefitCategoryServiceTest {

    @Autowired
    private BenefitCategoryService benefitCategoryService;

    @SpyBean
    private MccBenefitCategory mccBenefitCategory;

    @SpyBean
    private MerchantNameBenefitCategory merchantNameBenefitCategory;

    @Test
    @DisplayName("Given merchant name with category, when find, then return merchant name category")
    void givenMerchantNameWithBenefitCategory_whenFind_thenReturnMerchantNameCategory() {
        var merchantName = "IFood";

        var expected = BenefitCategory.MEAL;

        doReturn(Optional.of(expected)).when(merchantNameBenefitCategory).find(merchantName);

        var response = benefitCategoryService.findByMerchantNameAndMcc(merchantName, "5411");

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Given merchant name without category and mcc has category, when find, then return mcc category")
    void givenMerchantNameWithoutCategoryAndMccHasCategory_thenReturnMccCategory() {
        String merchantName = "XYZ Store";
        var mcc = "5411";

        var expected = BenefitCategory.FOOD;

        doReturn(Optional.empty()).when(merchantNameBenefitCategory).find(merchantName);
        doReturn(Optional.of(expected)).when(mccBenefitCategory).find(mcc);

        var response = benefitCategoryService.findByMerchantNameAndMcc(merchantName, mcc);

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Given merchant name and mcc without category, when find, then return cash category")
    void givenMerchantNameAndMccWithoutCategory_whenFind_thenReturnCashCategory() {
        String merchantName = "XYZ Store";
        String mcc = "9999";

        doReturn(Optional.empty()).when(merchantNameBenefitCategory).find(any());
        doReturn(Optional.empty()).when(mccBenefitCategory).find(any());

        var response = benefitCategoryService.findByMerchantNameAndMcc(merchantName, mcc);
        assertEquals(BenefitCategory.CASH, response);
    }
}
