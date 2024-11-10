package passport.roles.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;

/**
 * Interface for managing Event entities.
 */
public interface Events {

    /**
     * Registers a new event.
     *
     * @param event the event to be registered
     */
    void register(Event event);

    /**
     * Updates an existing event.
     *
     * @param event the event to be updated
     */
    void update(Event event);

    /**
     * Retrieves an event by its ID.
     *
     * @param id the ID of the event
     * @return an Optional containing the event if found, or empty if not found
     */
    Optional<Event> byId(EventId id);

    /**
     * Retrieves an event by its title and date.
     *
     * @param title the title of the event
     * @param date  the date of the event
     * @return an Optional containing the event if found, or empty if not found
     */
    Optional<Event> event(String title, LocalDate date);

    /**
     * Lists all events.
     *
     * @return a list of all events
     */
    List<Event> list();

    /**
     * Lists all events available on a specific date.
     *
     * @param date the date to check for available events
     * @return a list of events available on the specified date
     */
    List<Event> availableOn(LocalDate date);

    /**
     * Checks if an event exists by its ID.
     *
     * @param id the ID of the event
     * @return true if the event exists, false otherwise
     */
    boolean has(EventId id);

    /**
     * Checks if an event exists by its title and date.
     *
     * @param title  the title of the event
     * @param inDate the date of the event
     * @return true if the event exists, false otherwise
     */
    boolean has(String title, LocalDate inDate);
}
