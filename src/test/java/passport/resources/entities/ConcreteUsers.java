package passport.resources.entities;

import java.util.List;

import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import passport.domain.models.users.User;
import passport.domain.models.users.values.EmailAddress;
import passport.domain.models.users.values.Password;
import passport.infra.virtual.UsersInMemory;

public class ConcreteUsers {

    private static User johnDoe = new User(
            new Login(new EmailAddress("john.doe@example.com"),
                    new Password("123456")),
            new Person("John Doe", "000.000.000-00"));
    private static User janeDoe = new User(
            new Login(new EmailAddress("jane.doe@example.com"),
                    new Password("789123")),
            new Person("Jane Doe", "111.111.111-11"));

    static public User JohnDoe() { return johnDoe; }

    static public User JaneDoe() { return janeDoe; }

    static public UsersInMemory empty() { return new UsersInMemory(); }

    static public UsersInMemory withJohn() {
        return new UsersInMemory(List.of(JohnDoe()));
    }

    static public UsersInMemory withJohnAndJane() {
        return new UsersInMemory(List.of(JohnDoe(), JaneDoe()));
    }

}
