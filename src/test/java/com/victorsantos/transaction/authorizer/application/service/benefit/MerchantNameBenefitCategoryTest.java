package com.victorsantos.transaction.authorizer.application.service.benefit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MerchantNameBenefitCategoryTest {

    private final MerchantNameBenefitCategory merchantNameBenefitCategory = new MerchantNameBenefitCategory();

    @ParameterizedTest
    @MethodSource("provideMerchantNameAndCategory")
    @DisplayName("Given known merchantName, when find, then return optional with category")
    void givenKnownMcc_whenFind_thenReturnBenefitCategory(String merchantName, BenefitCategory expected) {
        var response = merchantNameBenefitCategory.find(merchantName);
        assertTrue(response.isPresent());
        assertEquals(expected, response.get());
    }

    public static Stream<Arguments> provideMerchantNameAndCategory() {
        return Stream.of(
                Arguments.of("ifood rio de janeiro", BenefitCategory.MEAL),
                Arguments.of("uber eats sp", BenefitCategory.MEAL),
                Arguments.of("restaurante italiano", BenefitCategory.MEAL),
                Arguments.of("supermercado extra", BenefitCategory.FOOD),
                Arguments.of("mercearia pão de açúcar", BenefitCategory.FOOD),
                Arguments.of("cafe sociedade", BenefitCategory.MEAL),
                Arguments.of("padaria real", BenefitCategory.MEAL),
                Arguments.of("pizzaria braz", BenefitCategory.MEAL),
                Arguments.of("fast food mcdonalds", BenefitCategory.MEAL),
                Arguments.of("food delivery rappi", BenefitCategory.MEAL));
    }

    @Test
    @DisplayName("Given unknown merchantName, when find, then return optional empty")
    void givenUnknownMcc_whenFind_thenReturnOptionalEmpty() {
        var response = merchantNameBenefitCategory.find("unknown");
        assertTrue(response.isEmpty());
    }
}
