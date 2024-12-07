package passport.application.desktop.system;

import passport.domain.contexts.events.AvailableEventsListing;
import passport.domain.contexts.events.EvaluationListing;
import passport.domain.contexts.events.EventEvaluation;
import passport.domain.contexts.purchases.TicketBuying;
import passport.domain.contexts.user.SigningUp;
import passport.domain.contexts.user.SubscribedEventsListing;
import passport.domain.contexts.user.UserEditing;
import passport.domain.contexts.user.UserLogin;

/**
 * Provides access to various services within the PassPort application. These
 * services encapsulate the business logic and interactions with different
 * domains.
 * 
 * @param signup            The service for user sign-up.
 * @param login             The service for user login.
 * @param eventsListing     The service for listing available events.
 * @param subscribedEvents  The service for listing events subscribed.
 * @param evaluationListing The service for listing event evaluations.
 * @param evaluation        The service for evaluating events.
 * @param purchasing        The service for buying tickets.
 * @param profileEditing    The service for editing user profiles.
 */
public record Services(
        SigningUp signup,
        UserLogin login,
        AvailableEventsListing eventsListing,
        SubscribedEventsListing subscribedEvents,
        EvaluationListing evaluationListing,
        EventEvaluation evaluation,
        TicketBuying purchasing,
        UserEditing profileEditing) {}
