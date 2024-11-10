package passport.infra;

import java.util.Optional;

import passport.domain.models.users.User;

/**
 * Manages the session state for a logged-in user. This class is placed in the
 * infra folder because it handles session management and in-memory persistence,
 * which are concerns related to the application's infrastructure.
 */
public class Session {
    private Optional<User> loggedUser = Optional.empty();

    /**
     * Constructs an empty session.
     */
    public Session() { this(Optional.empty()); }

    /**
     * Private constructor for initializing a session with an optional user.
     *
     * @param user the optional user
     */
    private Session(Optional<User> user) { this.loggedUser = user; }

    /**
     * Creates a session logged in as the specified user.
     *
     * @param user the user to log in as
     * @return a new session with the user logged in
     */
    public static Session loggedAs(User user) {
        return new Session(Optional.of(user));
    }

    /**
     * Logs in as the specified user.
     *
     * @param user the user to log in as
     */
    public void logInAs(User user) { this.loggedUser = Optional.of(user); }

    /**
     * Logs out the current user.
     */
    public void logOut() { this.loggedUser = Optional.empty(); }

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is logged in, false otherwise
     */
    public boolean isActive() { return loggedUser.isPresent(); }

    /**
     * Returns the currently logged-in user.
     *
     * @return an optional containing the logged-in user, or empty if no user is
     *         logged in
     */
    public Optional<User> loggedUser() { return loggedUser; }
}
