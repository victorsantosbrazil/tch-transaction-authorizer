package com.victorsantos.transaction.authorizer.application.service;

import static com.victorsantos.transaction.authorizer.application.service.BenefitCategoryServiceImpl.mccToCategoryMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BenefitCategoryServiceImpl.class})
class BenefitCategoryServiceTest {

    @Autowired
    private BenefitCategoryService benefitCategoryService;

    @ParameterizedTest
    @MethodSource("provideMccAndCategory")
    @DisplayName("Given mcc, when findByMcc, then return category")
    void givenMcc_whenFindByMcc_thenReturnBenefitCategory(String mcc, BenefitCategory expected) {
        var response = benefitCategoryService.findByMcc(mcc);
        assertEquals(expected, response);
    }

    public static Stream<Arguments> provideMccAndCategory() {
        return Stream.of(
                Arguments.of("5411", BenefitCategory.FOOD),
                Arguments.of("5412", BenefitCategory.FOOD),
                Arguments.of("5811", BenefitCategory.MEAL),
                Arguments.of("5812", BenefitCategory.MEAL));
    }

    @ParameterizedTest
    @MethodSource("provideUnknownMcc")
    @DisplayName("Given unknown mcc, when findByMcc, then return cash category")
    void givenUnknownMcc_whenFindByMcc_thenReturnCashBenefitCategory(String mcc) {
        var response = benefitCategoryService.findByMcc(mcc);
        assertEquals(BenefitCategory.CASH, response);
    }

    public static Stream<Arguments> provideUnknownMcc() {
        int nTests = 8;
        return Stream.generate(() -> Arguments.of(generateUnknownMcc())).limit(nTests);
    }

    public static String generateUnknownMcc() {
        var mcc = String.valueOf((int) (Math.random() * 10000));
        if (mccToCategoryMap.containsKey(mcc)) {
            return generateUnknownMcc();
        }
        return mcc;
    }
}
