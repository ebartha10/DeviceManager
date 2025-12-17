package com.platform.device.config;

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
    public static final String DEVICE_USER_QUEUE = "device.user.queue";

    public static final String DEVICE_EVENTS_EXCHANGE = "device.events.exchange";
    public static final String DEVICE_CREATE_ROUTING_KEY = "device.create";
    public static final String DEVICE_DELETE_ROUTING_KEY = "device.delete";

    public static final String CHAT_EVENTS_EXCHANGE = "chat.events.exchange";
    public static final String CHAT_MESSAGE_ROUTING_KEY = "chat.message";

    public static final String CHAT_REQUESTS_EXCHANGE = "chat.requests.exchange";
    public static final String CHAT_REQUEST_ROUTING_KEY = "chat.request";
    public static final String CHAT_REQUESTS_QUEUE = "chat.requests.queue";

    @Bean
    public Queue chatRequestsQueue() {
        return QueueBuilder.durable(CHAT_REQUESTS_QUEUE).build();
    }

    @Bean
    public TopicExchange chatRequestsExchange() {
        return new TopicExchange(CHAT_REQUESTS_EXCHANGE);
    }

    @Bean
    public Binding chatRequestsBinding() {
        return BindingBuilder
                .bind(chatRequestsQueue())
                .to(chatRequestsExchange())
                .with(CHAT_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Queue deviceUserQueue() {
        return QueueBuilder.durable(DEVICE_USER_QUEUE).build();
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
    public TopicExchange chatEventsExchange() {
        return new TopicExchange(CHAT_EVENTS_EXCHANGE);
    }

    @Bean
    public Binding deviceUserCreateBinding() {
        return BindingBuilder
                .bind(deviceUserQueue())
                .to(userEventsExchange())
                .with(USER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding deviceUserDeleteBinding() {
        return BindingBuilder
                .bind(deviceUserQueue())
                .to(userEventsExchange())
                .with(USER_DELETE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

