package passport.domain.contexts.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import passport.domain.models.events.Event;
import passport.domain.models.events.Ticket;
import passport.domain.models.users.User;
import passport.roles.Context;
import passport.roles.repositories.Events;

/**
 * Context for listing subscribed events by a user.
 * An event is only subscribed if the user owns the ticket of this event.
 */
public class SubscribedEventsListing implements Context {
    private Events events;

    /**
     * Private constructor for creating a SubscribedEventsListing context.
     *
     * @param events the Events' repository in question
     */
    public SubscribedEventsListing(Events events) { this.events = events; }

    /**
     * Retrieves the list of events subscribed by the user.
     *
     * @param customer the owner of the tickets's event
     * @return a list of tickets owned by the user
     */
    public List<Event> ownedBy(User customer) {
        return customer.tickets().stream()
                .map(Ticket::event)
                .map(events::byId)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
