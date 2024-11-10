package passport.domain.contexts.purchases.internal;

import java.util.Objects;

import passport.domain.exceptions.SoldOut;
import passport.domain.models.events.Event;
import passport.domain.models.events.Ticket;
import passport.domain.models.users.User;

/**
 * Represents a purchase transaction, including buying and refunding tickets.
 */
public class Purchase {
    public User buyer;
    public Ticket ticket;
    public Event event;

    /**
     * Makes a purchase of a specified amount of tickets.
     *
     * @param amount the amount of tickets to purchase
     * @throws SoldOut if the event is sold out
     */
    public void makeOf(Integer amount) throws SoldOut {
        this.shouldBeSellable();
        ticket = new Ticket(event.id()).packedFor(amount);
        event.boxOffice().sell(ticket);
        buyer.buyTicket(ticket);
        this.shouldBeSold();
    }

    /**
     * Refunds the purchased ticket.
     */
    public void refund() {
        this.shouldBeSold();
        this.shouldOwnTicket();
        event.boxOffice().refund(ticket);
        buyer.returnTicket(ticket);
    }

    /**
     * Ensures that the ticket has been sold.
     */
    public void shouldBeSold() {
        this.shouldBeSellable();
        Objects.requireNonNull(ticket, "Ticket must not be null");
    }

    /**
     * Ensures that the purchase is sellable.
     */
    public void shouldBeSellable() {
        Objects.requireNonNull(buyer, "Buyer must not be null");
        Objects.requireNonNull(event, "Event must not be null");
    }

    /**
     * Ensures that the buyer owns the ticket.
     */
    public void shouldOwnTicket() {
        assert buyer.tickets().contains(ticket) : "Buyer must own the ticket";
    }
}
