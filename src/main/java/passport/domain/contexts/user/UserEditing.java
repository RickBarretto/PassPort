package passport.domain.contexts.user;

import java.util.Objects;

import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.exceptions.InexistentUser;
import passport.domain.models.users.User;
import passport.domain.models.users.values.EmailAddress;
import passport.domain.models.users.values.Password;
import passport.roles.Context;
import passport.roles.repositories.Users;

/**
 * Context for editing user details.
 */
public class UserEditing implements Context {
    private Users users;
    private User user;

    /**
     * Constructs a UserEditing context with the specified user repository.
     *
     * @param repository the repository of users
     */
    public UserEditing(Users repository) { this.users = repository; }

    /**
     * Specifies the user to be edited.
     *
     * @param user the user to be edited
     * @return the current UserEditing context
     */
    public UserEditing of(User user) {
        this.user = user;
        return this;
    }

    /**
     * Prepares to change the specified user.
     *
     * @return an EditingWithTarget instance to apply changes
     * @throws InexistentUser if the user does not exist in the repository
     */
    public EditingWithTarget changing() throws InexistentUser {
        Objects.requireNonNull(users);
        Objects.requireNonNull(user);
        if (!users.has(user.login().email()))
            throw new InexistentUser();
        return new EditingWithTarget(users, user);
    }

    /**
     * Inner class representing the editing operations for a specific user.
     */
    public class EditingWithTarget {
        private Users repository;
        private User target;
        private User updated;

        /**
         * Constructs an EditingWithTarget instance with the specified
         * repository and user.
         *
         * @param repository the repository of users
         * @param target     the user to be edited
         */
        public EditingWithTarget(Users repository, User target) {
            this.repository = repository;
            this.target = target;
            this.updated = target.copy();
        }

        /**
         * Updates the user's name.
         *
         * @param name the new name
         * @return the current EditingWithTarget instance
         */
        public EditingWithTarget name(String name) {
            this.updated = updated.with(updated.person().withName(name));
            return this;
        }

        /**
         * Updates the user's CPF.
         *
         * @param cpf the new CPF
         * @return the current EditingWithTarget instance
         */
        public EditingWithTarget cpf(String cpf) {
            this.updated = updated.with(updated.person().withCpf(cpf));
            return this;
        }

        /**
         * Updates the user's email.
         *
         * @param rawEmail the new email as a stirng
         * @return the current EditingWithTarget instance
         * @throws EmailAlreadyExists if the email already exists in the
         *                                repository
         */
        public EditingWithTarget email(String rawEmail) throws EmailAlreadyExists {
            shouldBeAvailable(rawEmail);
            this.updated = updated.with(updated.login().with(new EmailAddress(rawEmail)));
            return this;
        }

        /**
         * Updates the user's password.
         *
         * @param password the new password
         * @return the current EditingWithTarget instance
         */
        public EditingWithTarget password(String password) {
            this.updated = updated.with(updated.login().with(new Password(
                    password)));
            return this;
        }

        /**
         * Checks if the new email is available.
         *
         * @param email the new email
         * @throws EmailAlreadyExists if the email already exists in the
         *                                repository
         */
        private void shouldBeAvailable(String email) throws EmailAlreadyExists {
            if (target.login().email().equals(email))
                return;
            if (repository.has(new EmailAddress(email)))
                throw new EmailAlreadyExists();
        }

        /**
         * Commits the changes to the user.
         */
        public void edit() {
            Objects.requireNonNull(this.repository);
            Objects.requireNonNull(this.target);
            Objects.requireNonNull(this.updated);
            this.repository.update(target, updated);
        }
    }
}
