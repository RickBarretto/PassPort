package passport.infra.placeholders.users;

import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;

public class RickAdm {
    static UserId id = new UserId();

    public static UserId id() { return id; }

    public static String email() { return "rick.adm@passport.com"; }

    public static String password() { return "123456789"; }

    public static String name() { return "Rick B. (Adm)"; }

    public static String cpf() { return "000.000.000-00"; }

    public static User user() {
        return new User(
                id,
                new Login(email(), password()),
                new Person(name(), cpf())).asAdmin();
    }

}
