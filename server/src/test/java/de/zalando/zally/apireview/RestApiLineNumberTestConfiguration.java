package de.zalando.zally.apireview;

import de.zalando.zally.rule.ApiValidator;
import de.zalando.zally.rule.AvoidTrailingSlashesRule;
import de.zalando.zally.rule.CompositeRulesValidator;
import de.zalando.zally.rule.InvalidApiSchemaRule;
import de.zalando.zally.rule.JsonRulesValidator;
import de.zalando.zally.rule.RulesPolicy;
import de.zalando.zally.rule.SpecificationPointerProvider;
import de.zalando.zally.rule.SwaggerRule;
import de.zalando.zally.rule.SwaggerRulesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RestApiLineNumberTestConfiguration {

    @Autowired
    private RulesPolicy rulesPolicy;

    @Autowired
    private InvalidApiSchemaRule invalidApiRule;

    @Autowired
    private SpecificationPointerProvider specPointerProvider;

    @Bean
    @Primary
    public ApiValidator validator() {
        final List<SwaggerRule> swaggerRules = Arrays.asList(
            new AvoidTrailingSlashesRule(specPointerProvider)
        );
        return new CompositeRulesValidator(
                new SwaggerRulesValidator(swaggerRules, rulesPolicy, invalidApiRule),
                new JsonRulesValidator(Arrays.asList(invalidApiRule), rulesPolicy, invalidApiRule));
    }
}