package org.flexess.games.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
public class MessageConfiguration {

    static final String positionsExchangeName = "flexess.computer-player.positions";
    static final String positionsQueueName = "positions";

    static final String movesExchangeName = "flexess.computer-player.moves";
    static final String movesQueueName = "moves";


    @Bean
    Queue movesQueue() {
        return new Queue(movesQueueName, false);
    }

    @Bean
    Queue positionsQueue() {
        return new Queue(positionsQueueName, false);
    }


    @Bean
    MessageListenerAdapter listenerAdapter(MoveReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    DirectExchange movesExchange() {
        return new DirectExchange(movesExchangeName);
    }

    @Bean
    DirectExchange positionsExchange() {
        return new DirectExchange(positionsExchangeName);
    }

    @Bean
    Binding positionsBinding(@Qualifier("positionsQueue") Queue queue, @Qualifier("positionsExchange") DirectExchange exchange) {
        return bind(queue).to(exchange).with("");
    }

    @Bean
    Binding movesBinding(@Qualifier("movesQueue") Queue queue, @Qualifier("movesExchange") DirectExchange exchange) {
        return bind(queue).to(exchange).with("");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(movesQueueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
