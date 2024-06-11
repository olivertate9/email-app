package dev.profitsoft.email_sender.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ setup.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${rabbit.queue.name}")
    private String queueName;

    /**
     * Creates a Queue bean for the specified queue name.
     *
     * @return A new Queue instance.
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(queueName, false);
    }

    /**
     * Creates a MessageConverter bean that converts messages to JSON format using Jackson.
     *
     * @return A new Jackson2JsonMessageConverter instance.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Creates a SimpleRabbitListenerContainerFactory bean configured with the provided connection factory and message converter.
     *
     * @param connectionFactory The connection factory for RabbitMQ.
     * @param messageConverter The message converter to use.
     * @return A new SimpleRabbitListenerContainerFactory instance.
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter)
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
