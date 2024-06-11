package dev.profitsoft.email_sender.dto;

import dev.profitsoft.email_sender.entities.email.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


/**
 * Data Transfer Object (DTO) representing an email.
 */
@Getter
@Setter
@Builder
public class EmailDto {
    private String id;

    private String subject;

    private String content;

    private String email;

    private Status status;

    private String errorMessage;

    private int attemptCount;

    private Instant lastAttempt;
}

