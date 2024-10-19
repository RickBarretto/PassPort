package main.infra;

import java.util.Optional;

import main.domain.models.users.User;

public class Session {
    private Optional<User> loggedUser = Optional.empty();

    public Session() { this(Optional.empty()); }

    private Session(Optional<User> user) { this.loggedUser = user; }

    public static Session loggedAs(User user) { return new Session(Optional.of(user)); }

    public void logInAs(User user) { this.loggedUser = Optional.of(user); }

    public void logOut() { this.loggedUser = Optional.empty(); }

    public boolean isActive() { return loggedUser.isPresent(); }

    public Optional<User> loggedUser() { return loggedUser; }

}
