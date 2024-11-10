package passport.roles.repositories;

import java.util.List;
import java.util.Optional;

import passport.domain.models.users.Login;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;
import passport.domain.models.users.values.EmailAddress;

/**
 * Interface for managing User entities.
 */
public interface Users {

    /**
     * Registers a new user.
     *
     * @param user the user to be registered
     */
    void register(User user);

    /**
     * Updates an existing user.
     *
     * @param target  the user to be updated
     * @param newUser the new user information
     */
    void update(User target, User newUser);

    /**
     * Retrieves an user by their ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> byId(UserId id);

    /**
     * Retrieves an user by their email and password.
     *
     * @param login the login of the user
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> ownerOf(Login login);

    /**
     * Checks if an user exists by their email.
     *
     * @param email the email of the user
     * @return true if the user exists, false otherwise
     */
    boolean has(EmailAddress email);

    /**
     * Lists all users.
     *
     * @return a list of all users
     */
    List<User> list();
}
