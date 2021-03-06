package de.zalando.zally.rule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DisabledAvoidJavascriptKeywordsRuleTest {

    @Autowired
    private RulesValidator rulesValidator;

    @Test
    public void ruleShouldNotBePartOfRulesValidator() {
        assertThat(rulesValidator.getRules().stream()
            .map(r -> r.getClass()).collect(Collectors.<Class>toList()))
            .doesNotContain(AvoidJavascriptKeywordsRule.class);
    }
}
