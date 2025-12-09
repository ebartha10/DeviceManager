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
    public static final String MONITORING_MEASUREMENTS_QUEUE = "monitoring.measurements.queue";

    public static final String CHAT_EVENTS_EXCHANGE = "chat.events.exchange";
    public static final String CHAT_MESSAGE_ROUTING_KEY = "chat.message";
    public static final String WEBSOCKET_CHAT_QUEUE = "websocket.chat.queue";

    @Bean
    public Queue monitoringUserQueue() {
        return QueueBuilder.durable(MONITORING_USER_QUEUE).build();
    }

    @Bean
    public Queue monitoringDeviceQueue() {
        return QueueBuilder.durable(MONITORING_DEVICE_QUEUE).build();
    }

    @Bean
    public Queue monitoringMeasurementsQueue() {
        return QueueBuilder.durable(MONITORING_MEASUREMENTS_QUEUE).build();
    }

    @Bean
    public Queue websocketChatQueue() {
        return QueueBuilder.durable(WEBSOCKET_CHAT_QUEUE).build();
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
    public TopicExchange chatEventsExchange() {
        return new TopicExchange(CHAT_EVENTS_EXCHANGE);
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

    @Bean
    public Binding monitoringMeasurementsBinding() {
        return BindingBuilder
                .bind(monitoringMeasurementsQueue())
                .to(deviceMeasurementsExchange())
                .with(DEVICE_MEASUREMENT_ROUTING_KEY);
    }

    @Bean
    public Binding websocketChatBinding() {
        return BindingBuilder
                .bind(websocketChatQueue())
                .to(chatEventsExchange())
                .with(CHAT_MESSAGE_ROUTING_KEY);
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

