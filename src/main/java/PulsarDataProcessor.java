import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors; 
import javax.annotation.PostConstruct;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.PulsarClientException;

@Component
public class PulsarDataProcessor {

    @Autowired
    private PulsarClient pulsarClient;

    @Autowired
    private PulsarConfig pulsarConfig;

    @Autowired
    private GraphQLProcessor graphQLProcessor;

    private ExecutorService executorService;

    private Consumer<String> consumer;

    @PostConstruct
    public void init() {
        try {
            // Create a new fixed thread pool with the number of threads specified in the config
            this.executorService = Executors.newFixedThreadPool(pulsarConfig.getThreadPoolSize());
            // Create a new consumer for the specified Pulsar topic and subscription
            this.consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic(pulsarConfig.getTopicName())
                    .subscriptionName(pulsarConfig.getSubscriptionName())
                    .subscribe();

            while (true) {
                // Receive message from Pulsar topic
                String message = consumer.receive().getValue();
                // Use the GraphQLProcessor to process the received message and store it in the Neo4j database
                executorService.submit(() -> graphQLProcessor.process(message));
            }
        } catch (PulsarClientException e) {
            // Handle the exception
        }
    }

    public void destroy() {
        // Close the consumer and the thread pool when the bean is destroyed
        consumer.close();
        executorService.shutdown();
    }
}
