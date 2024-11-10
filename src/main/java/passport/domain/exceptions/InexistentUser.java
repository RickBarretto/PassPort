package passport.domain.exceptions;

/**
 * Exception thrown when an operation is attempted on a user that does not exist.
 */
public class InexistentUser extends Exception {

    /**
     * Constructs a new InexistentUser exception with no detail message.
     */
    public InexistentUser() {
        super();
    }
}
