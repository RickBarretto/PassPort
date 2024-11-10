package passport.domain.contexts.user;

import passport.domain.exceptions.PermissionDenied;
import passport.domain.models.users.Login;
import passport.infra.Session;
import passport.roles.Context;
import passport.roles.repositories.Users;

/**
 * Context for managing user login operations.
 */
public class UserLogin implements Context {
    private Session session;
    private Users users;

    /**
     * Constructs a UserLogin context with the specified user repository.
     *
     * @param users the repository of users
     */
    public UserLogin(Users users) { this.users = users; }

    /**
     * Associates the current login context with a session.
     *
     * @param session the session to be associated
     * @return the current UserLogin context
     */
    public UserLogin withSession(Session session) {
        this.session = session;
        return this;
    }

    /**
     * Logs in the user with the specified email and password.
     *
     * @param login the login of the user
     * @throws PermissionDenied if the login credentials are incorrect
     */
    public void logAs(Login login) throws PermissionDenied {
        session.logInAs(users.ownerOf(login).orElseThrow(
                () -> new PermissionDenied("Wrong login credentials")));
    }

    /**
     * Logs out the current session.
     */
    public void logOut() { session.logOut(); }
}
