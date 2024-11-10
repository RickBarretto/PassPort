package passport.domain.models.email;

import passport.domain.models.users.values.EmailAddress;

/**
 * Represents the metadata of an email, including sender, recipient, and
 * subject.
 */
public class EmailMetadata {
    private final EmailAddress senderEmail;
    private final EmailAddress recipientEmail;
    private final String subject;

    /**
     * Constructs a new EmailMetadata with the specified sender, recipient, and
     * subject.
     *
     * @param senderEmail    the email address of the sender
     * @param recipientEmail the email address of the recipient
     * @param subject        the subject of the email
     */
    public EmailMetadata(
            EmailAddress senderEmail, 
            EmailAddress recipientEmail,
            String subject) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
    }

    /**
     * Returns the sender's email address.
     *
     * @return the sender's email address
     */
    public EmailAddress sender() { return senderEmail; }

    /**
     * Returns the recipient's email address.
     *
     * @return the recipient's email address
     */
    public EmailAddress recipient() { return recipientEmail; }

    /**
     * Returns the subject of the email.
     *
     * @return the subject of the email
     */
    public String subject() { return subject; }
}
