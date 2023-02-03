import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;

import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.graphql_java_generator.graphql.GraphQLRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProcessor {
    @Autowired
    private GraphQLRequest graphQLRequest;
    @Autowired
    private Driver neo4jDriver;

    public void process(String message) {
        try (Session session = neo4jDriver.session()) {
            // Start a new transaction
            try (Transaction transaction = session.beginTransaction()) {
                // Execute Cypher query to store the received message in the Neo4j database
                transaction.run("CREATE (a:Message {content: $message})", parameters("message", message));
                transaction.success();
            }
        } catch (GraphQLRequestPreparationException | GraphQLRequestExecutionException e) {
            // handle errors
        }
    }
}
