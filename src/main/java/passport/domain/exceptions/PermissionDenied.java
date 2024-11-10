package passport.domain.exceptions;

/**
 * Exception thrown when a user attempts to perform an operation without the
 * necessary permissions.
 */
public class PermissionDenied extends Exception {

    /**
     * Constructs a new PermissionDenied exception with the specified detail
     * message.
     *
     * @param msg the detail message
     */
    public PermissionDenied(String msg) { super(msg); }
}
