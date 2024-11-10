package passport.domain.contexts.user;

import java.util.Objects;

import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import passport.domain.models.users.User;
import passport.roles.Context;
import passport.roles.repositories.Users;

/**
 * Stores account information for a user, including login details and personal
 * details.
 */
class AccountInformation {
    public Login login;
    public Person person;

    /**
     * Ensures that all required fields are initialized.
     *
     * @throws NullPointerException if any required field is null
     */
    public void shouldBeInitialized() {
        Objects.requireNonNull(login, "Login cannot be null");
        Objects.requireNonNull(person, "Person cannot be null");
    }
}

/**
 * Allows registering a User into a Repository.
 */
public class UserRegistering implements Context {
    private AccountInformation account = new AccountInformation();
    private Users repository;

    /**
     * Constructor that initializes the repository.
     *
     * @param repository the users repository
     */
    public UserRegistering(Users repository) {
        this.repository = repository;
    }

    /**
     * Sets the login information for the user.
     *
     * @param login the login information
     * @return the updated UserRegistering object
     */
    public UserRegistering login(Login login) {
        account.login = login;
        return this;
    }

    /**
     * Sets the personal information for the user.
     *
     * @param person the personal information
     * @return the updated UserRegistering object
     */
    public UserRegistering person(Person person) {
        account.person = person;
        return this;
    }

    /**
     * Registers the user in the repository.
     *
     * @throws EmailAlreadyExists if the email is already registered
     */
    public void register() throws EmailAlreadyExists {
        account.shouldBeInitialized();
        emailShouldBeUnregistered();
        this.repository.register(new User(account.login, account.person));
    }

    /**
     * Checks if the email is already registered in the repository.
     *
     * @throws EmailAlreadyExists if the email is already registered
     */
    private void emailShouldBeUnregistered() throws EmailAlreadyExists {
        if (this.repository.has(account.login.email())) {
            throw new EmailAlreadyExists();
        }
    }
}
