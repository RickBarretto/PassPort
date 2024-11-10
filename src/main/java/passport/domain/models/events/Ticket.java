package passport.domain.models.events;

import java.util.Objects;

/**
 * Represents a ticket for an event, including event ID, price, and
 * availability.
 */
public class Ticket {
    private final EventId event;
    private final Double price;
    private final Integer availableFor;

    /**
     * Constructs a new Ticket with the specified event ID and price.
     *
     * @param event the event ID
     * @param price the price of the ticket
     */
    public Ticket(EventId event, Double price) { this(event, price, 1); }

    /**
     * Constructs a new Ticket with the specified event ID and a default price
     * of 0.0.
     *
     * @param event the event ID
     */
    public Ticket(EventId event) { this(event, 0.0, 1); }

    /**
     * Constructs a new Ticket with the specified event ID, price, and
     * availability.
     *
     * @param event        the event ID
     * @param price        the price of the ticket
     * @param availableFor the number of persons the ticket is available for
     */
    private Ticket(EventId event, Double price, Integer availableFor) {
        this.event = event;
        this.price = price;
        this.availableFor = availableFor;
    }

    /**
     * Creates a copy of the current ticket.
     *
     * @return a new Ticket object that is a copy of the current ticket
     */
    public Ticket copy() { return new Ticket(event, price, availableFor); }

    /**
     * Creates a ticket for the specified number of persons.
     *
     * @param persons the number of persons
     * @return a new Ticket object with the specified availability
     */
    public Ticket packedFor(Integer persons) {
        assert persons > 0;
        return new Ticket(event, price, persons);
    }

    /**
     * Returns the event ID associated with the ticket.
     *
     * @return the event ID
     */
    public EventId event() { return event; }

    /**
     * Returns the total price of the ticket based on availability.
     *
     * @return the total price
     */
    public Double price() { return price * availableFor; }

    /**
     * Returns the number of persons the ticket is available for.
     *
     * @return the number of available persons
     */
    public Integer availableFor() { return availableFor; }

    /**
     * Checks if the ticket is for the same event as another ticket.
     *
     * @param other the other ticket to compare
     * @return true if the tickets are for the same event, false otherwise
     */
    public boolean sameEvent(Ticket other) {
        return Objects.equals(event, other.event);
    }
}
