package dev.profitsoft.email_sender.repositories;

import dev.profitsoft.email_sender.entities.email.Email;
import dev.profitsoft.email_sender.entities.email.Status;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Email entities.
 */
@Repository
public interface EmailRepository extends ElasticsearchRepository<Email, String> {
    /**
     * Finds all emails with the specified status.
     *
     * @param status The status of the emails to find.
     * @return A list of emails with the specified status.
     */
    List<Email> findAllByStatus(Status status);
}
