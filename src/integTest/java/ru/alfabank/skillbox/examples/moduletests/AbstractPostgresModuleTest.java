package ru.alfabank.skillbox.examples.moduletests;

import lombok.SneakyThrows;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alfabank.skillbox.examples.moduletests.containers.PostgresContainerWrapper;

@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {AbstractPostgresModuleTest.PropertiesInitializer.class})
public class AbstractPostgresModuleTest extends AbstractModuleTest {

    @Container
    protected static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper()
            .withInitScript("postgres/init-db-schema-integTest.sql");

    // Еще один пример инициализации контейнера
    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            // тут можно делать разные операции с контейнером после его старта
//            postgresContainer.copyFileToContainer(
//                    MountableFile.forClasspathResource("/postgres/init-db-and-user-integTest.sh"),
//                    "/docker-entrypoint-initdb.d/init-db-and-user-integTest.sh");
//            postgresContainer.copyFileToContainer(
//                    MountableFile.forClasspathResource("/postgres/init-db-schema-integTest.sql"),
//                    "/var/lib/postgresql/init/init-db-schema-integTest.sql");
//            org.testcontainers.containers.Container.ExecResult lsResult =
//                    postgresContainer.execInContainer("chmod", "u+x", "/docker-entrypoint-initdb.d/init-db-and-user-integTest.sh");
//            lsResult = postgresContainer.execInContainer("bash", "/docker-entrypoint-initdb.d/init-db-and-user-integTest.sh");
//            TestPropertyValues.of(
//                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
//                    "spring.datasource.username=" + postgresContainer.getUsername(),
//                    "spring.datasource.password=" + postgresContainer.getPassword())
//                    .applyTo(applicationContext.getEnvironment());
        }
    }

    @DynamicPropertySource
    public static void initSystemParams(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}
