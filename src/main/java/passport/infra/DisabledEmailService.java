package passport.infra;

import passport.domain.models.email.EmailDocument;
import passport.roles.EmailService;

/**
 * A mock implementation of the EmailService interface used for testing
 * purposes. This class captures the email document instead of sending it,
 * allowing tests to verify email content without actually sending emails.
 */
public class DisabledEmailService implements EmailService {
    private EmailDocument email;

    /**
     * Captures the email document instead of sending it.
     *
     * @param email the email document to capture
     */
    @Override
    public void send(EmailDocument email) { this.email = email; }

    /**
     * Returns the captured email document.
     *
     * @return the captured email document
     */
    public EmailDocument email() { return email; }
}
