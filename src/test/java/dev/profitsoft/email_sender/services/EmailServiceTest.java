package dev.profitsoft.email_sender.services;

import dev.profitsoft.email_sender.dto.EmailDto;
import dev.profitsoft.email_sender.entities.email.Email;
import dev.profitsoft.email_sender.entities.email.Status;
import dev.profitsoft.email_sender.repositories.EmailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private EmailRepository emailRepository;

    @Test
    void testSendEmailSuccess() {
        EmailDto emailDto = EmailDto.builder()
                .email("test@example.com")
                .subject("Test Subject")
                .content("Test Content")
                .build();

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail(emailDto);

        verify(javaMailSender, times(1))
                .send(any(SimpleMailMessage.class));

        Email expectedEmail = Email.builder()
                .content(emailDto.getContent())
                .subject(emailDto.getSubject())
                .email(emailDto.getEmail())
                .status(Status.SENT)
                .build();

        verify(emailRepository, times(1))
                .save(Mockito.refEq(expectedEmail, "id", "errorMessage", "lastAttempt", "attemptCount"));
    }

    @Test
    void testSendEmailFailedAndRetry() {
        EmailDto emailDto = EmailDto.builder()
                .email("test@example.com")
                .subject("Test Subject")
                .content("Test Content")
                .build();

        doThrow(new MailException("Failed to send email") {
        }).when(javaMailSender).send(any(SimpleMailMessage.class));

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);

        emailService.sendEmail(emailDto);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        verify(emailRepository, times(1)).save(emailCaptor.capture());
        Email savedEmail = emailCaptor.getValue();

        assertEquals(Status.FAILED, savedEmail.getStatus());
        assertThat(savedEmail.getErrorMessage()).contains("Failed to send email");
        assertEquals(1, savedEmail.getAttemptCount());
        assertNotNull(savedEmail.getLastAttempt());

        verify(emailRepository, times(1)).findAllByStatus(Status.FAILED);
    }
}