import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("kalo");
        factory.setPassword("kalo");

        GenerationScript generator = new GenerationScript();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Sending random messages to queue '" + QUEUE_NAME + "'. Press CTRL+C to stop.");

            while (true) {
                String message = generator.generateMessage();

                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");

                Thread.sleep(1000); // Wait 1 second before next send
            }
        }
    }
}
