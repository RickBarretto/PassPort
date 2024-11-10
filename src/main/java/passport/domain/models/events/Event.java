package passport.domain.models.events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import passport.domain.models.evaluations.Evaluation;
import passport.roles.Entity;

/**
 * Represents an event with its poster, box office details, and evaluations.
 */
public class Event implements Entity<EventId> {
    private final EventId id = new EventId();
    private final Poster poster;
    private BoxOffice boxOffice;
    private ArrayList<Evaluation> evaluations;

    /**
     * Constructs a new Event with the specified poster and a default ticket
     * price.
     *
     * @param poster the event poster
     */
    public Event(Poster poster) { this(poster, 0.0); }

    /**
     * Constructs a new Event with the specified poster and ticket price.
     *
     * @param poster the event poster
     * @param price  the ticket price
     */
    public Event(Poster poster, Double price) {
        this.poster = poster;
        this.boxOffice = new BoxOffice(new Ticket(id, price));
        this.evaluations = new ArrayList<>();
    }

    /**
     * Receives an evaluation and adds it to the event.
     *
     * @param evaluation the evaluation to add
     */
    public void receiveEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
    }

    /**
     * Adds capacity to the event's box office.
     *
     * @param capacity the capacity to add
     */
    public void addCapacity(Integer capacity) {
        this.boxOffice = this.boxOffice.ofCapacity(capacity);
    }

    /**
     * Checks if the event is available on the specified date.
     *
     * @param date the date to check
     * @return true if the event is available on the date, false otherwise
     */
    public boolean isAvailableFor(LocalDate date) {
        var schedule = poster.date();
        return !schedule.isBefore(date);
    }

    /**
     * Returns the event ID.
     *
     * @return the event ID
     */
    public EventId id() { return id; }

    /**
     * Returns the event poster.
     *
     * @return the event poster
     */
    public Poster poster() { return poster; }

    /**
     * Returns the box office details of the event.
     *
     * @return the box office details
     */
    public BoxOffice boxOffice() { return boxOffice; }

    /**
     * Returns the list of evaluations for the event.
     *
     * @return an unmodifiable list of evaluations
     */
    public List<Evaluation> evaluations() {
        return List.copyOf(evaluations);
    }
}
