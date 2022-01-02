package blog.comments;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers =  BaseIntegrationTest.Initializer.class)
public abstract class BaseIntegrationTest {
    @Container
    public static GenericContainer<?> mongoDbContainer = new GenericContainer<>(DockerImageName.parse("bitnami/mongodb"))
            .withEnv("MONGODB_ROOT_USER", "root")
            .withEnv("MONGODB_ROOT_PASSWORD", "test")
            .withExposedPorts(27017);

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.username=root",
                    "spring.data.mongodb.password=test",
                    "spring.data.mongodb.database=admin",
                    "spring.data.mongodb.port=" + mongoDbContainer.getMappedPort(27017)
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
