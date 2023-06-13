import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaMessageListenerTest {

    @Mock
    private ConsumerFactory<String, String> consumerFactory;

    @Captor
    private ArgumentCaptor<MessageListener<String, String>> messageListenerCaptor;

    private KafkaMessageListener kafkaMessageListener;

    @BeforeEach
    public void setup() {
        kafkaMessageListener = new KafkaMessageListener();
    }

    @Test
    public void testMessageListenerContainerCreation() {
        // Mock the necessary objects
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = mock(ConcurrentKafkaListenerContainerFactory.class);
        KafkaMessageListenerContainer<String, String> kafkaMessageListenerContainer = mock(KafkaMessageListenerContainer.class);

        // Set up the container factory to return the mocked listener container
        when(containerFactory.createContainer(any(ContainerProperties.class))).thenReturn(kafkaMessageListenerContainer);

        // Set up the consumer factory to return the container factory
        when(consumerFactory.createContainer(any(ContainerProperties.class))).thenReturn(containerFactory);

        // Create the message listener container
        MessageListenerContainer listenerContainer = kafkaMessageListener.createMessageListenerContainer(consumerFactory);

        // Verify the container creation
        verify(containerFactory).createContainer(any(ContainerProperties.class));
        verify(consumerFactory).createContainer(any(ContainerProperties.class));
        verify(kafkaMessageListenerContainer).setupMessageListener(messageListenerCaptor.capture());

        // Test the message listener behavior
        MessageListener<String, String> messageListener = messageListenerCaptor.getValue();
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("test-topic", 0, 0, "key", "value");
        Headers headers = mock(Headers.class);

        messageListener.onMessage(consumerRecord);
        // Add your assertions or verify the behavior based on the message received from the Kafka topic
    }
}
