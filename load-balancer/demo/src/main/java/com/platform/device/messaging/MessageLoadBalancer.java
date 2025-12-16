package com.platform.device.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MessageLoadBalancer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Random random = new Random();

    // Weighted distribution: 50%, 30%, 20%
    private static final int[] WEIGHTS = { 50, 30, 20 };
    private static final String[] INGEST_QUEUES = {
            "ingest.queue.1",
            "ingest.queue.2",
            "ingest.queue.3"
    };

    /**
     * Consumes from central device measurements queue
     * and forwards to per-replica ingest queues using weighted distribution.
     * Uses raw Message object to avoid deserialization overhead/errors.
     */
    @RabbitListener(queues = "device.measurement.queue")
    public void handleDeviceMeasurement(Message message) {
        try {
            // Select target queue using weighted distribution
            String targetQueue = selectQueueWeighted();

            // Forward message to the selected replica's ingest queue
            rabbitTemplate.send(targetQueue, message);

            System.out.println("Message Load Balancer: Forwarded message to " + targetQueue);

        } catch (Exception e) {
            System.err.println("Error in message load balancer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Selects a queue using weighted distribution.
     * Queue 1 gets 50% of messages, Queue 2 gets 30%, Queue 3 gets 20%
     */
    private String selectQueueWeighted() {
        // Calculate total weight
        int totalWeight = WEIGHTS[0] + WEIGHTS[1] + WEIGHTS[2]; // 100

        // Generate random number between 0 and totalWeight
        int randomValue = random.nextInt(totalWeight);

        // Select queue based on weighted ranges
        int cumulativeWeight = 0;
        for (int i = 0; i < INGEST_QUEUES.length; i++) {
            cumulativeWeight += WEIGHTS[i];
            if (randomValue < cumulativeWeight) {
                return INGEST_QUEUES[i];
            }
        }

        // Fallback (should not reach here)
        return INGEST_QUEUES[0];
    }
}
