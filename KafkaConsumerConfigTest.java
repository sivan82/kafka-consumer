import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@PropertySource("classpath:application.properties")
@TestPropertySource("classpath:application.properties")
@EnableConfigurationProperties
public class KafkaConsumerConfigTest {

    @Value("${bootstrap.servers}")
    private String bootstrapServers;

    @Value("${security.protocol}")
    private String securityProtocol;

    @Value("${ssl.truststore.location}")
    private String truststoreLocation;

    @Value("${ssl.truststore.password}")
    private String truststorePassword;

    @Value("${ssl.keystore.location}")
    private String keystoreLocation;

    @Value("${ssl.keystore.password}")
    private String keystorePassword;

    private KafkaConsumerConfig kafkaConsumerConfig;

    @PostConstruct
    public void setup() {
        kafkaConsumerConfig = new KafkaConsumerConfig();
    }

    @Test
    public void testConsumerFactory() {
        // Create the consumer factory
        ConsumerFactory<String, String> consumerFactory = kafkaConsumerConfig.consumerFactory();

        // Verify the consumer factory properties
        assertEquals(bootstrapServers, consumerFactory.getConfigurationProperties().get("bootstrap.servers"));
        assertEquals(securityProtocol, consumerFactory.getConfigurationProperties().get("security.protocol"));
        assertEquals(truststoreLocation, consumerFactory.getConfigurationProperties().get("ssl.truststore.location"));
        assertEquals(truststorePassword, consumerFactory.getConfigurationProperties().get("ssl.truststore.password"));
        assertEquals(keystoreLocation, consumerFactory.getConfigurationProperties().get("ssl.keystore.location"));
        assertEquals(keystorePassword, consumerFactory.getConfigurationProperties().get("ssl.keystore.password"));
    }

    @Test
    public void testKafkaListenerContainerFactory() {
        // Mock the consumer factory
        ConsumerFactory<String, String> consumerFactory = mock(ConsumerFactory.class);

        // Create the container factory
        DefaultKafkaListenerContainerFactory<String, String> containerFactory = kafkaConsumerConfig.kafkaListenerContainerFactory();

        // Verify the container factory configuration
        assertEquals(consumerFactory, containerFactory.getConsumerFactory());
    }
}
