package passport.roles;

import passport.domain.models.email.EmailDocument;

/**
 * Interface for sending emails within the application. This allows different
 * implementations of the email sending functionality to be swapped in and out
 * without affecting the core business logic, crucial for testing purposes.
 */
public interface EmailService {

    /**
     * Sends an email document.
     *
     * @param email the email document to send
     */
    void send(EmailDocument email);
}
