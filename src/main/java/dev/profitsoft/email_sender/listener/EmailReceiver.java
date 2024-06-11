package dev.profitsoft.email_sender.listener;

import dev.profitsoft.email_sender.dto.EmailDto;
import dev.profitsoft.email_sender.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listener class for receiving email messages from a RabbitMQ queue.
 */
@Component
@RequiredArgsConstructor
public class EmailReceiver {

    private final EmailService emailService;

    /**
     * Receives an email message from the specified RabbitMQ queue and sends the email using EmailService.
     *
     * @param email The DTO containing the email details.
     */
    @RabbitListener(queues = "${rabbit.queue.name}")
    public void receiveEmail(EmailDto email) {
        emailService.sendEmail(email);
    }
}
