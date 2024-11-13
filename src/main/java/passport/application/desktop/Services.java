package passport.application.desktop;

import passport.domain.contexts.user.SigningUp;
import passport.domain.contexts.user.UserLogin;

public record Services(
        SigningUp signup,
        UserLogin login) {}
