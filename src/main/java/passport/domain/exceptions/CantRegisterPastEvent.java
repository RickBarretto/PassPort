package passport.domain.exceptions;

/**
 * Exception thrown when attempting to register an event scheduled for a past
 * date.
 */
public class CantRegisterPastEvent extends Exception {

    /**
     * Constructs a new CantRegisterPastEvent with no detail message.
     */
    public CantRegisterPastEvent() { super(); }
}
