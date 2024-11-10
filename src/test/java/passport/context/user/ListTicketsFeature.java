package passport.context.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passport.domain.contexts.user.ListTickets;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Ticket;
import passport.domain.models.users.User;
import passport.resources.bdd.*;
import passport.resources.entities.ConcreteUsers;

@Feature("Listing User Tickets")
public class ListTicketsFeature {

    private List<Ticket> tickets;
    private User customer;

    @BeforeEach
    void setUp() {
        tickets = List.of(new Ticket(new EventId()), new Ticket(new EventId()));
        customer = ConcreteUsers.JohnDoe().withTickets(tickets);
    }

    @Given("A customer with two tickets")
    @Assume("The customer should have two tickets")
    @When("Listing the customer's tickets")
    @Then("Should return the same tickets")
    @Test
    void shouldReturnTheSameTickets() {
        assumeTrue(2 == customer.tickets().size());
        assertEquals(tickets, ListTickets.ownedBy(customer).list());
    }
}
