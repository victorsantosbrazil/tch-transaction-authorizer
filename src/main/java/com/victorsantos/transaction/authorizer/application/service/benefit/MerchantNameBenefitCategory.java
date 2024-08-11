package com.victorsantos.transaction.authorizer.application.service.benefit;

import com.victorsantos.transaction.authorizer.domain.enums.BenefitCategory;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
class MerchantNameBenefitCategory {

    private final Pattern pattern = Pattern.compile(
            "(uber\\s*eats|ifood|restaurante|supermercado|mercearia|cafe|padaria|pizzaria|fast\\s*food|food\\s*delivery)");

    private final Map<String, BenefitCategory> merchantNameToCategoryMap = Map.ofEntries(
            Map.entry("ifood", BenefitCategory.MEAL),
            Map.entry("uber eats", BenefitCategory.MEAL),
            Map.entry("restaurante", BenefitCategory.MEAL),
            Map.entry("supermercado", BenefitCategory.FOOD),
            Map.entry("mercearia", BenefitCategory.FOOD),
            Map.entry("cafe", BenefitCategory.MEAL),
            Map.entry("padaria", BenefitCategory.MEAL),
            Map.entry("pizzaria", BenefitCategory.MEAL),
            Map.entry("fast food", BenefitCategory.MEAL),
            Map.entry("food delivery", BenefitCategory.MEAL));

    public Optional<BenefitCategory> find(String merchantName) {
        Matcher matcher = pattern.matcher(merchantName.toLowerCase());
        if (matcher.find()) {
            var group = matcher.group();
            return Optional.ofNullable(merchantNameToCategoryMap.get(group));
        }
        return Optional.empty();
    }
}
