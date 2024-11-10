package passport.domain.exceptions;

/**
 * Exception thrown when attempting to register an email that already exists in
 * the system.
 */
public class EmailAlreadyExists extends Exception {

    /**
     * Constructs a new EmailAlreadyExists exception with no detail message.
     */
    public EmailAlreadyExists() { super(); }
}
