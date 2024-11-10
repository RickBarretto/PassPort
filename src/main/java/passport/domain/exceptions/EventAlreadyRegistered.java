package passport.domain.exceptions;

/**
 * Exception thrown when attempting to register an event that is already
 * registered.
 */
public class EventAlreadyRegistered extends Exception {

    /**
     * Constructs a new EventAlreadyRegistered exception with no detail message.
     */
    public EventAlreadyRegistered() { super(); }
}
