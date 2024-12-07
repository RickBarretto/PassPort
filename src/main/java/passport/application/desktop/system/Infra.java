package passport.application.desktop.system;

import passport.infra.Session;
import passport.roles.EmailService;
import passport.roles.repositories.Events;
import passport.roles.repositories.Users;

/**
 * Represents the infrastructure layer of the PassPort application.
 * 
 * @param users        The repository of users.
 * @param events       The repository of events.
 * @param emailService The email service used for sending emails.
 * @param session      The session management for the application.
 */
public record Infra(
        Users users,
        Events events,
        EmailService emailService,
        Session session) {}
