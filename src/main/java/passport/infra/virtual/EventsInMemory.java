package passport.infra.virtual;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.roles.repositories.Events;

/**
 * Represents an index for Events, enhancing lookup performance in the in-memory
 * database. This index allows efficient searching of events by their title and
 * date.
 */
class EventIndex {
    String title;
    LocalDate date;

    /**
     * Constructs a new EventIndex with the specified title and date.
     *
     * @param left  the title of the event
     * @param right the date of the event
     */
    public EventIndex(String left, LocalDate right) {
        this.title = left;
        this.date = right;
    }

    /**
     * Returns the title of the event.
     *
     * @return the event title
     */
    public String left() { return title; }

    /**
     * Returns the date of the event.
     *
     * @return the event date
     */
    public LocalDate right() { return date; }

    @Override
    public int hashCode() { return Objects.hash(title, date); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EventIndex))
            return false;
        EventIndex other = (EventIndex) obj;
        return Objects.equals(title, other.title)
                && Objects.equals(date, other.date);
    }
}

/**
 * In-memory implementation of the Events repository.
 */
public class EventsInMemory implements Events {
    private HashMap<EventId, Event> events = new HashMap<>();
    private HashMap<EventIndex, EventId> infoIndex = new HashMap<>();

    /**
     * Constructs a new EventsInMemory with an empty list of events.
     */
    public EventsInMemory() { this(List.of()); }

    /**
     * Constructs a new EventsInMemory with a list of events.
     *
     * @param eventsList the list of events
     */
    public EventsInMemory(List<Event> eventsList) {
        eventsList.forEach(event -> register(event));
    }

    @Override
    public void register(Event event) {
        var index = new EventIndex(event.poster().title(),
                event.poster().date());
        this.events.put(event.id(), event);
        this.infoIndex.put(index, event.id());
    }

    @Override
    public void update(Event event) { events.replace(event.id(), event); }

    @Override
    public List<Event> list() { return List.copyOf(events.values()); }

    @Override
    public List<Event> availableOn(LocalDate date) {
        return events.values().stream()
                .filter(event -> event.isAvailableFor(date)).toList();
    }

    @Override
    public Optional<Event> byId(EventId id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public Optional<Event> event(String title, LocalDate date) {
        var index = new EventIndex(title, date);
        return this.byId(infoIndex.get(index));
    }

    @Override
    public boolean has(EventId id) { return events.containsKey(id); }

    @Override
    public boolean has(String title, LocalDate inDate) {
        return infoIndex.containsKey(new EventIndex(title, inDate));
    }
}
