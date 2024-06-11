package dev.profitsoft.email_sender.entities.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

/**
 * Entity representing an email to be sent.
 */
@Getter
@Setter
@Builder
@Document(indexName = "emails")
public class Email {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Keyword)
    private Status status;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Integer)
    private int attemptCount;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private Instant lastAttempt;
}


