import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarConfig {

    @Value("${pulsar.service-url}")
    private String pulsarServiceUrl;

    @Value("${pulsar.topic-name}")
    private String topicName;

    @Value("${pulsar.subscription-name}")
    private String subscriptionName;

    @Value("${pulsar.thread-pool-size}")
    private int threadPoolSize;

    @Bean
    public PulsarClient pulsarClient() throws PulsarClientException {
        return PulsarClient.builder()
                .serviceUrl(pulsarServiceUrl)
                .build();
    }

    public String getTopicName() {
        return topicName;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
