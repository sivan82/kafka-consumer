import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {
    @KafkaListener(topics = "your-kafka-topic")
    public void listen(String message) {
        // Process the received message
        System.out.println("Received message: " + message);
        // Add your business logic here
    }
}
