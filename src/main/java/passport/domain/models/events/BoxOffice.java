package passport.domain.models.events;

import passport.domain.exceptions.SoldOut;

/**
 * Represents a box office for selling and refunding event tickets.
 */
public class BoxOffice {
    private final Ticket sample;
    private final Integer capacity;
    private Integer sales;

    /**
     * Constructs a new BoxOffice with the specified ticket sample and default
     * capacity and sales.
     *
     * @param ticket the sample ticket
     */
    public BoxOffice(Ticket ticket) { this(ticket, 0, 0); }

    /**
     * Constructs a new BoxOffice with the specified ticket sample, capacity,
     * and sales.
     *
     * @param ticket   the sample ticket
     * @param capacity the maximum capacity of tickets
     * @param sales    the number of tickets sold
     */
    private BoxOffice(Ticket ticket, Integer capacity, Integer sales) {
        this.sample = ticket;
        this.capacity = capacity;
        this.sales = sales;
    }

    /**
     * Creates a BoxOffice with the specified capacity.
     *
     * @param amount the capacity of tickets
     * @return a new BoxOffice with the specified capacity
     */
    public BoxOffice ofCapacity(Integer amount) {
        assert amount >= 0;
        return new BoxOffice(sample, amount, sales);
    }

    /**
     * Creates a ticket with the specified amount of available seats.
     *
     * @param amount the number of available seats
     * @return a new Ticket with the specified amount
     */
    public Ticket ticket(Integer amount) {
        return sample.copy().packedFor(amount);
    }

    /**
     * Sells the specified ticket.
     *
     * @param ticket the ticket to sell
     * @throws SoldOut if the ticket sales exceed the capacity
     */
    public void sell(Ticket ticket) throws SoldOut {
        assert this.sales <= this.capacity;
        if ((this.sales + ticket.availableFor()) > this.capacity)
            throw new SoldOut();
        this.sales += ticket.availableFor();
    }

    /**
     * Refunds the specified ticket.
     *
     * @param ticket the ticket to refund
     */
    public void refund(Ticket ticket) {
        assert sales >= ticket.availableFor();
        assert ticket.sameEvent(sample);
        this.sales -= ticket.availableFor();
        assert sales >= 0;
    }

    /**
     * Returns the number of available tickets.
     *
     * @return the number of available tickets
     */
    public Integer available() {
        var result = this.capacity - this.sales;
        assert result >= 0;
        return result;
    }

    /**
     * Checks if the event is sold out.
     *
     * @return true if the event is sold out, false otherwise
     */
    public boolean isSoldOut() { return capacity == sales; }

    /**
     * Returns the number of tickets sold.
     *
     * @return the number of tickets sold
     */
    public Integer sales() { return sales; }

    /**
     * Returns the capacity of the event.
     *
     * @return the event capacity
     */
    public Integer capacity() { return capacity; }
}
