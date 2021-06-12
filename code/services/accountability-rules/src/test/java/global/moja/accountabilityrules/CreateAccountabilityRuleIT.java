/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules;

import global.moja.accountabilityrules.models.AccountabilityRule;
import org.assertj.core.api.Assertions;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = CreateAccountabilityRuleIT.Initializer.class)
public class CreateAccountabilityRuleIT {

    @Autowired
    WebTestClient webTestClient;
    static final PostgreSQLContainer postgreSQLContainer;
    static final AccountabilityRule accountabilityRule4;

    static {

        postgreSQLContainer =
                new PostgreSQLContainer("postgres:10.15")
                        .withDatabaseName("accountabilityRules")
                        .withUsername("postgres")
                        .withPassword("postgres");

        postgreSQLContainer
                .withInitScript("init.sql")
                .start();

        accountabilityRule4 =
                AccountabilityRule.builder()
                        .id(null)
                        .accountabilityTypeId(4L)
                        .parentPartyTypeId(4L)
                        .subsidiaryPartyTypeId(4L)
                        .version(null)
                        .build();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername()
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @AfterClass
    public static void shutdown() {

        postgreSQLContainer.stop();
    }

    @Test
    public void Given_AccountabilityRuleDetails_When_Post_Then_AccountabilityRuleRecordWillBeCreatedAndReturned() {

        webTestClient
                .post()
                .uri("/api/v1/accountability_rules")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accountabilityRule4), AccountabilityRule.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountabilityRule.class)
                .value(response -> {
                            Assertions.assertThat(response.getId()).isEqualTo(4L);
                            Assertions.assertThat(response.getAccountabilityTypeId()).isEqualTo(accountabilityRule4.getAccountabilityTypeId());
                            Assertions.assertThat(response.getParentPartyTypeId()).isEqualTo(accountabilityRule4.getParentPartyTypeId());
                            Assertions.assertThat(response.getSubsidiaryPartyTypeId()).isEqualTo(accountabilityRule4.getSubsidiaryPartyTypeId());
                            Assertions.assertThat(response.getVersion()).isEqualTo(1);
                        }
                );
    }
}
