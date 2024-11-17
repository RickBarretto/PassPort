package passport.application.desktop.system;

import passport.domain.contexts.events.AvailableEventsListing;
import passport.domain.contexts.events.EvaluationListing;
import passport.domain.contexts.events.EventEvaluation;
import passport.domain.contexts.purchases.TicketBuying;
import passport.domain.contexts.user.SigningUp;
import passport.domain.contexts.user.SubscribedEventsListing;
import passport.domain.contexts.user.UserEditing;
import passport.domain.contexts.user.UserLogin;

public record Services(
        SigningUp signup,
        UserLogin login,
        AvailableEventsListing eventsListing,
        SubscribedEventsListing subscribedEvents,
        EvaluationListing evaluationListing,
        EventEvaluation evaluation,
        TicketBuying purchasing,
        UserEditing profileEditing) {}
