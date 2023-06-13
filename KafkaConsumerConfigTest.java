import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerConfigTest {

    private KafkaConsumerConfig kafkaConsumerConfig;

    @BeforeEach
    public void setup() {
        kafkaConsumerConfig = new KafkaConsumerConfig();
    }

    @Test
    public void testConsumerFactory() {
        // Mock the necessary properties
        String bootstrapServers = "localhost:9092";
        String truststoreLocation = "/path/to/truststore.jks";
        String truststorePassword = "truststore-password";
        String keystoreLocation = "/path/to/keystore.jks";
        String keystorePassword = "keystore-password";

        // Set up the expected properties
        Map<String, Object> expectedProps = new HashMap<>();
        expectedProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        expectedProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        expectedProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        expectedProps.put("security.protocol", "SSL");
        expectedProps.put("ssl.truststore.location", truststoreLocation);
        expectedProps.put("ssl.truststore.password", truststorePassword);
        expectedProps.put("ssl.keystore.location", keystoreLocation);
        expectedProps.put("ssl.keystore.password", keystorePassword);

        // Create the consumer factory
        ConsumerFactory<String, String> consumerFactory = kafkaConsumerConfig.consumerFactory();

        // Verify the consumer factory properties
        assertEquals(expectedProps, ((DefaultKafkaConsumerFactory<String, String>) consumerFactory).getConfigurationProperties());
    }

    @Test
    public void testKafkaListenerContainerFactory() {
        // Mock the consumer factory
        ConsumerFactory<String, String> consumerFactory = mock(ConsumerFactory.class);

        // Set up the expected container factory
        DefaultKafkaListenerContainerFactory<String, String> expectedFactory = new DefaultKafkaListenerContainerFactory<>();
        expectedFactory.setConsumerFactory(consumerFactory);

        // Create the container factory
        DefaultKafkaListenerContainerFactory<String, String> actualFactory = kafkaConsumerConfig.kafkaListenerContainerFactory();

        // Verify the container factory configuration
        assertEquals(expectedFactory.getConsumerFactory(), actualFactory.getConsumerFactory());
    }
}
