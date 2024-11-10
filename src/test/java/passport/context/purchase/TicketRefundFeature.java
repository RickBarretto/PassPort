package passport.context.purchase;

import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passport.domain.contexts.purchases.TicketBuying;
import passport.domain.contexts.purchases.TicketRefund;
import passport.domain.exceptions.InvalidRefundDueToInactiveEvent;
import passport.domain.exceptions.PurchaseForInactiveEvent;
import passport.domain.exceptions.SoldOut;
import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.events.Poster;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;
import passport.domain.models.users.values.EmailAddress;
import passport.domain.models.users.values.Password;
import passport.infra.virtual.EventsInMemory;
import passport.infra.virtual.UsersInMemory;
import passport.resources.bdd.And;
import passport.resources.bdd.Assume;
import passport.resources.bdd.Feature;
import passport.resources.bdd.Given;
import passport.resources.bdd.Scenario;
import passport.resources.bdd.Then;
import passport.resources.bdd.When;
import passport.roles.repositories.Events;
import passport.roles.repositories.Users;

@Feature("Ticket Refund")
public class TicketRefundFeature {
    private final LocalDate activeDate = LocalDate.of(2024, 10, 15);
    private final LocalDate inactiveDate = LocalDate.of(2024, 10, 16);
    private Events events;
    private Users users;

    @BeforeEach
    void loadRepositories() {
        var event = new Event(new Poster("From Zero", "A LP show",
                LocalDate.of(2024, 10, 15)));
        event.addCapacity(2);
        events = new EventsInMemory(List.of(event));
        users = new UsersInMemory(
                List.of(new User(
                        new Login(new EmailAddress("john.doe@example.com"),
                                new Password(
                                        "123456")),
                        new Person("John Doe", "000.000.000-00"))));
    }

    EventId targetEvent() { return events.list().get(0).id(); }

    UserId targetUser() { return users.list().get(0).id(); }

    void sellTickets(Integer amount, LocalDate currentDay)
            throws SoldOut, PurchaseForInactiveEvent {
        new TicketBuying(events, users,
                currentDay)
                        .of(targetEvent())
                        .by(targetUser())
                        .via(new PaymentMethod("...", "..."))
                        .buy(amount);
    }

    void refundFirstTicket(LocalDate currentDay)
            throws InvalidRefundDueToInactiveEvent {
        new TicketRefund(events, users,
                currentDay)
                        .to(targetUser())
                        .via(new PaymentMethod("...", "..."))
                        .owning(users.byId(targetUser()).get().tickets().get(0))
                        .refund();
    }

    @Scenario("Refunding Tickets")
    @Given("One couple tickets sold")
    @And("The Event still active")
    @Assume("Two tickets have been sold")
    @When("Refunding one ticket for couples")
    @Then("Event should have 2 available tickets and user 0 tickets")
    @Test
    void shouldBeEmptyForCouple() {
        try {
            sellTickets(2, activeDate);
        }
        catch (Exception e) {
            assumeNoException(e);
        }

        // When
        assertDoesNotThrow(() -> refundFirstTicket(activeDate));

        // Then
        assertTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 2);
        assertTrue(users.byId(targetUser()).get().tickets().isEmpty());
    }

    @Scenario("Refunding Individual Tickets")
    @Given("Two individual tickets have been sold")
    @And("The Event still active")
    @Assume("Two tickets have been sold")
    @When("Refunding one ticket of two individual ones")
    @Then("Event should have 1 available ticket and user 1 ticket")
    @Test
    void shouldBeEmptyForIndividual() {
        try {
            sellTickets(1, activeDate);
            sellTickets(1, activeDate);
        }
        catch (Exception e) {
            assumeNoException(e);
        }

        // When
        assertDoesNotThrow(() -> refundFirstTicket(activeDate));

        // Then
        assertTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 1);
        assertTrue(users.byId(targetUser()).get().tickets().size() == 1);
    }

    @Scenario("Denied to refunding Tickets")
    @Given("Tickets sold")
    @And("The Event is inactive")
    @Assume("Two tickets have been sold")
    @When("Refunding one ticket for couples")
    @Then("Should throw InvalidRefundDueToInactiveEvent")
    @And("Event should have the same amount of tickets")
    @Test
    void shouldNotRefund() {
        try {
            sellTickets(2, activeDate);
        }
        catch (Exception e) {
            assumeNoException(e);
        }

        // When
        assertThrows(InvalidRefundDueToInactiveEvent.class,
                () -> refundFirstTicket(inactiveDate));

        // Then
        assertTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 0);
        assertTrue(!users.byId(targetUser()).get().tickets().isEmpty());
    }
}
