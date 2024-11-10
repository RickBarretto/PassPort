package passport.application.desktop;

import passport.infra.Session;
import passport.roles.EmailService;
import passport.roles.repositories.Events;
import passport.roles.repositories.Users;

public record Infra(
        Users users,
        Events events,
        EmailService emailService,
        Session session) {
}
