package main.domain.contexts.events;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import main.domain.models.events.Event;
import main.roles.Context;
import main.roles.repositories.Events;

/**
 * Context for listing available events starting from today.
 */
public class AvailableEventsListing implements Context {
    private LocalDate today;
    private Events events;

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
     * Sets the events repository for the context.
     *
     * @param events the events repository
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing from(Events events) {
        this.events = events;
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
