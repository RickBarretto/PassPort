package passport.domain.contexts.user;

import java.util.List;

import passport.domain.models.events.Ticket;
import passport.domain.models.users.User;
import passport.roles.Context;

/**
 * Context for listing tickets owned by a user.
 */
public class ListTickets implements Context {
    private User customer;

    /**
     * Private constructor for creating a ListTickets context with the specified
     * user.
     *
     * @param customer the user whose tickets are to be listed
     */
    private ListTickets(User customer) { this.customer = customer; }

    /**
     * Static factory method for creating a ListTickets context for a specific
     * user.
     *
     * @param customer the user whose tickets are to be listed
     * @return a new ListTickets context for the specified user
     */
    public static ListTickets ownedBy(User customer) {
        return new ListTickets(customer);
    }

    /**
     * Retrieves the list of tickets owned by the user.
     *
     * @return a list of tickets owned by the user
     */
    public List<Ticket> list() { return customer.tickets(); }
}
