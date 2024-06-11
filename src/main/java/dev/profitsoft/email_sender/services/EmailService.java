package dev.profitsoft.email_sender.services;

import dev.profitsoft.email_sender.dto.EmailDto;
import dev.profitsoft.email_sender.entities.email.Email;
import dev.profitsoft.email_sender.entities.email.Status;
import dev.profitsoft.email_sender.repositories.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service class for handling email operations.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    /**
     * The delay (in milliseconds) between retry attempts for sending failed emails.
     */
    private static final long FIVE_MINUTE_DELAY = 300000;

    private final JavaMailSender mailSender;
    private final EmailRepository repository;

    /**
     * Sends an email using the provided email data.
     *
     * @param emailDto The DTO containing the email details.
     */
    public void sendEmail(EmailDto emailDto) {
        try {
            SimpleMailMessage message = createMessage(emailDto);
            mailSender.send(message);
            emailDto.setStatus(Status.SENT);
        } catch (Exception e) {
            updateEmailDto(emailDto, e);
        }

        Email email = toEmail(emailDto);
        repository.save(email);
    }

    /**
     * Scheduled task to retry sending failed emails.
     * This task runs at a fixed delay of every five minutes.
     */
    @Scheduled(fixedDelay = FIVE_MINUTE_DELAY)
    public void retrySendFailedEmailsTask() {
        List<Email> emails = repository.findAllByStatus(Status.FAILED);

        for (Email email : emails) {
            EmailDto dto = toDto(email);
            sendEmail(dto);
        }
    }

    /**
     * Creates a SimpleMailMessage object from the provided EmailDto.
     *
     * @param emailDto The DTO containing the email details.
     * @return The created SimpleMailMessage object.
     */
    private static SimpleMailMessage createMessage(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getEmail());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getContent());
        return message;
    }

    /**
     * Updates the provided EmailDto with error information.
     *
     * @param emailDto The DTO containing the email details.
     * @param e        The exception that occurred during the email sending process.
     */
    private static void updateEmailDto(EmailDto emailDto, Exception e) {
        emailDto.setStatus(Status.FAILED);
        emailDto.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
        emailDto.setAttemptCount(emailDto.getAttemptCount() + 1);
        emailDto.setLastAttempt(Instant.now());
    }

    /**
     * Converts an EmailDto object to an Email entity.
     *
     * @param emailDto The DTO containing the email details.
     * @return The created Email entity.
     */
    private Email toEmail(EmailDto emailDto) {
        return Email.builder()
                .id(emailDto.getId())
                .content(emailDto.getContent())
                .subject(emailDto.getSubject())
                .email(emailDto.getEmail())
                .status(emailDto.getStatus())
                .attemptCount(emailDto.getAttemptCount())
                .errorMessage(emailDto.getErrorMessage())
                .lastAttempt(emailDto.getLastAttempt())
                .build();
    }

    /**
     * Converts an Email entity to an EmailDto object.
     *
     * @param email The Email entity.
     * @return The created EmailDto object.
     */
    private EmailDto toDto(Email email) {
        return EmailDto.builder()
                .id(email.getId())
                .content(email.getContent())
                .subject(email.getSubject())
                .email(email.getEmail())
                .status(email.getStatus())
                .attemptCount(email.getAttemptCount())
                .errorMessage(email.getErrorMessage())
                .lastAttempt(email.getLastAttempt())
                .build();
    }
}
