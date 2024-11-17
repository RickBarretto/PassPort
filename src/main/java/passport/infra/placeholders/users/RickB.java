package passport.infra.placeholders.users;

import java.util.List;

import passport.domain.models.events.Ticket;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;
import passport.infra.placeholders.events.FromZero;

public class RickB {
    static UserId id = new UserId();

    public static UserId id() { return id; }

    public static String email() { return "rick@example.com"; }

    public static String password() { return "123456789"; }

    public static String name() { return "Rick B."; }

    public static String cpf() { return "000.000.000-00"; }

    public static User user() {
        final Login login = new Login(email(), password());
        final Person person = new Person(name(), cpf());
        final List<Ticket> tickets = List.of(FromZero.ticket());
        final boolean admin = true;
        return new User(id, login, person, admin, tickets);
    }

}
