package passport.domain.models.email;

/**
 * Represents an email document, including metadata and the body content.
 */
public class EmailDocument {
    private final EmailMetadata meta;
    private final Html body;

    /**
     * Constructs a new EmailDocument with the specified metadata and body.
     *
     * @param meta the email metadata
     * @param body the HTML content of the email body
     */
    public EmailDocument(EmailMetadata meta, Html body) {
        this.meta = meta;
        this.body = body;
    }

    /**
     * Returns the body content of the email as a string.
     *
     * @return the body content as a string
     */
    public String body() { return body.toString(); }

    /**
     * Returns the metadata of the email.
     *
     * @return the email metadata
     */
    public EmailMetadata metadata() { return meta; }
}
