package passport.infra.placeholders;

import java.util.List;

import passport.domain.models.users.User;
import passport.infra.placeholders.users.RickAdm;
import passport.infra.placeholders.users.RickB;

public class ColdUsers {
       public static final List<User> list = List.of(
            RickB.user(),
            RickAdm.user());
}
