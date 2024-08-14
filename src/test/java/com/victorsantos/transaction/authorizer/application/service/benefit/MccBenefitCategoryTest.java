package com.victorsantos.transaction.authorizer.application.service.benefit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MccBenefitCategoryTest {

    private final MccBenefitCategory mccBenefitCategory = new MccBenefitCategory();

    @ParameterizedTest
    @MethodSource("provideMccAndCategory")
    @DisplayName("Given known mcc, when find, then return category")
    void givenKnownMcc_whenFind_thenReturnBenefitCategory(String mcc, BenefitCategory expected) {
        var response = mccBenefitCategory.find(mcc);
        assertTrue(response.isPresent());
        assertEquals(expected, response.get());
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
    @DisplayName("Given unknown mcc, when find, then return optional empty")
    void givenUnknownMcc_whenFindByMcc_thenReturnOptionalEmpty(String mcc) {
        var response = mccBenefitCategory.find(mcc);
        assertEquals(Optional.empty(), response);
    }

    public static Stream<Arguments> provideUnknownMcc() {
        int nTests = 8;
        return Stream.generate(() -> Arguments.of(generateUnknownMcc())).limit(nTests);
    }

    public static String generateUnknownMcc() {
        var mcc = String.valueOf((int) (Math.random() * 10000));
        if (MccBenefitCategory.mccToCategoryMap.containsKey(mcc)) {
            return generateUnknownMcc();
        }
        return mcc;
    }
}
