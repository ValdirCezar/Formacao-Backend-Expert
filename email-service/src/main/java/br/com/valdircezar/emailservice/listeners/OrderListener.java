package br.com.valdircezar.emailservice.listeners;

import br.com.valdircezar.emailservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.dtos.OrderCreatedMessage;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class OrderListener {

    private final EmailService emailService;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "helpdesk", type = "topic"),
            value = @Queue(value = "queue.orders"),
            key = "rk.orders.create"
    ))
    public void listener(final OrderCreatedMessage message) {
        log.info("Ordem de serviço recebida: {}", message);
        emailService.sendMail(message);
    }
}
