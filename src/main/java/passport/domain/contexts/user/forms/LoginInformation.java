package passport.domain.contexts.user.forms;

import java.util.Objects;

import passport.domain.models.users.Login;

/**
 * Represents the login information for a user, including email and password.
 */
public class LoginInformation {
    private String email;
    private String password;

    /**
     * Sets the email for the login information.
     *
     * @param email the user's email
     * @return the updated LoginInformation object
     */
    public LoginInformation email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Sets the password for the login information.
     *
     * @param password the user's password
     * @return the updated LoginInformation object
     */
    public LoginInformation password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Submits the login information and returns a Login object.
     *
     * @return a new Login object with the provided email and password
     * @throws NullPointerException if the email or password is null
     */
    public Login submit() {
        Objects.requireNonNull(this.email, "Email cannot be null");
        Objects.requireNonNull(this.password, "Password cannot be null");
        return new Login(email, password);
    }
}
