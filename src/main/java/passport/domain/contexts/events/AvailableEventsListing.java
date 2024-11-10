package passport.domain.contexts.events;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import passport.domain.models.events.Event;
import passport.roles.Context;
import passport.roles.repositories.Events;

/**
 * Context for listing available events starting from today.
 */
public class AvailableEventsListing implements Context {
    private LocalDate today;
    private Events events;

    /**
     * Constructor with the specified events repository.
     *
     * @param events the repository of events used for listing
     */
    public AvailableEventsListing(Events events) {
        this.events = events;
    }

    /**
     * Sets the current date for the context.
     *
     * @param today the current date
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing beingToday(LocalDate today) {
        this.today = today;
        return this;
    }

    /**
     * Retrieves the list of available events starting from the set date.
     *
     * @return a list of available events
     * @throws NullPointerException if the current date or events repository is
     *                                  not set
     */
    public List<Event> availables() {
        Objects.requireNonNull(today, "Today date cannot be null");
        Objects.requireNonNull(events, "Events repository cannot be null");
        return events.availableOn(today);
    }
}
