package com.platform.user.config;

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
    public static final String AUTH_USER_QUEUE = "auth.user.queue";

    @Bean
    public Queue authUserQueue() {
        return QueueBuilder.durable(AUTH_USER_QUEUE).build();
    }

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange(USER_EVENTS_EXCHANGE);
    }

    @Bean
    public Binding authUserDeleteBinding() {
        return BindingBuilder
                .bind(authUserQueue())
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

