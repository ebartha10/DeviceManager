package com.platform.device.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_EVENTS_EXCHANGE = "user.events.exchange";
    public static final String USER_CREATE_ROUTING_KEY = "user.create";
    public static final String USER_DELETE_ROUTING_KEY = "user.delete";
    public static final String MONITORING_USER_QUEUE = "monitoring.user.queue";

    public static final String DEVICE_EVENTS_EXCHANGE = "device.events.exchange";
    public static final String DEVICE_CREATE_ROUTING_KEY = "device.create";
    public static final String DEVICE_DELETE_ROUTING_KEY = "device.delete";
    public static final String MONITORING_DEVICE_QUEUE = "monitoring.device.queue";

    public static final String DEVICE_MEASUREMENTS_EXCHANGE = "device.measurements.exchange";
    public static final String DEVICE_MEASUREMENT_ROUTING_KEY = "device.measurement";
    // Central device measurement queue (consumed by load balancer, not monitoring
    // replicas)
    public static final String DEVICE_MEASUREMENT_QUEUE = "device.measurement.queue";

    public static final String OVERCONSUMPTION_NOTIFICATIONS_EXCHANGE = "overconsumption.notifications.exchange";
    public static final String OVERCONSUMPTION_NOTIFICATION_ROUTING_KEY = "overconsumption.notification";
    public static final String WEBSOCKET_OVERCONSUMPTION_QUEUE = "websocket.overconsumption.queue";

    // Per-replica ingest queues for load balancing
    public static final String INGEST_QUEUE_1 = "ingest.queue.1";
    public static final String INGEST_QUEUE_2 = "ingest.queue.2";
    public static final String INGEST_QUEUE_3 = "ingest.queue.3";

    @Bean
    public Queue monitoringUserQueue() {
        return QueueBuilder.durable(MONITORING_USER_QUEUE).build();
    }

    @Bean
    public Queue monitoringDeviceQueue() {
        return QueueBuilder.durable(MONITORING_DEVICE_QUEUE).build();
    }

    /**
     * Central queue that load balancer consumes from
     */
    @Bean
    public Queue deviceMeasurementQueue() {
        return QueueBuilder.durable(DEVICE_MEASUREMENT_QUEUE).build();
    }

    @Bean
    public Queue websocketOverconsumptionQueue() {
        return QueueBuilder.durable(WEBSOCKET_OVERCONSUMPTION_QUEUE).build();
    }

    @Bean
    public Queue ingestQueue1() {
        return QueueBuilder.durable(INGEST_QUEUE_1).build();
    }

    @Bean
    public Queue ingestQueue2() {
        return QueueBuilder.durable(INGEST_QUEUE_2).build();
    }

    @Bean
    public Queue ingestQueue3() {
        return QueueBuilder.durable(INGEST_QUEUE_3).build();
    }

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(USER_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange deviceEventsExchange() {
        return new TopicExchange(DEVICE_EVENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange deviceMeasurementsExchange() {
        return new TopicExchange(DEVICE_MEASUREMENTS_EXCHANGE);
    }

    @Bean
    public TopicExchange overconsumptionNotificationsExchange() {
        return new TopicExchange(OVERCONSUMPTION_NOTIFICATIONS_EXCHANGE);
    }

    @Bean
    public Binding monitoringUserCreateBinding() {
        return BindingBuilder
                .bind(monitoringUserQueue())
                .to(userEventsExchange())
                .with(USER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding monitoringUserDeleteBinding() {
        return BindingBuilder
                .bind(monitoringUserQueue())
                .to(userEventsExchange())
                .with(USER_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding monitoringDeviceCreateBinding() {
        return BindingBuilder
                .bind(monitoringDeviceQueue())
                .to(deviceEventsExchange())
                .with(DEVICE_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding monitoringDeviceDeleteBinding() {
        return BindingBuilder
                .bind(monitoringDeviceQueue())
                .to(deviceEventsExchange())
                .with(DEVICE_DELETE_ROUTING_KEY);
    }

    /**
     * Bind central device measurement queue to the exchange
     * This is what the load balancer consumes from
     */
    @Bean
    public Binding deviceMeasurementBinding() {
        return BindingBuilder
                .bind(deviceMeasurementQueue())
                .to(deviceMeasurementsExchange())
                .with(DEVICE_MEASUREMENT_ROUTING_KEY);
    }

    @Bean
    public Binding websocketOverconsumptionBinding() {
        return BindingBuilder
                .bind(websocketOverconsumptionQueue())
                .to(overconsumptionNotificationsExchange())
                .with(OVERCONSUMPTION_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
