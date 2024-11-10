package passport.context.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passport.domain.contexts.events.EventRegistering;
import passport.domain.contexts.events.forms.EventInformation;
import passport.domain.exceptions.CantRegisterPastEvent;
import passport.domain.exceptions.EventAlreadyRegistered;
import passport.domain.exceptions.PermissionDenied;
import passport.domain.models.events.Event;
import passport.domain.models.events.Poster;
import passport.domain.models.users.User;
import passport.infra.virtual.EventsInMemory;
import passport.resources.bdd.And;
import passport.resources.bdd.Feature;
import passport.resources.bdd.Given;
import passport.resources.bdd.Scenario;
import passport.resources.bdd.Then;
import passport.resources.bdd.When;
import passport.resources.entities.ConcreteUsers;
import passport.roles.repositories.Events;

@Feature("Registering an Event")
public class EventRegisteringFeature {

    private Events repository;

    @BeforeEach
    void emptyRepository() { repository = new EventsInMemory(); }

    User someCustomer() { return ConcreteUsers.JohnDoe(); }

    User someAdmin() { return someCustomer().asAdmin(); }

    Poster somePoster() {
        return new EventInformation()
                .title("From Zero")
                .description("Linkin Park's show")
                .scheduledFor(LocalDate.of(2024, 10, 15))
                .submit();
    }

    Optional<Event> actualEvent(Events repository) {
        return repository.event("From Zero", LocalDate.of(2024, 10, 15));
    }

    @Scenario("Successfully registering an Event")
    @Given("Some Post, an Admin user, and the Current Day")
    @When("Registering a new future Event as an Admin")
    @Then("Should register if User is Admin")
    @And("Is a future Event")
    @And("Event's title and date is Available")
    @Test
    void shouldRegister() {
        Supplier<Boolean> isRegistered = () -> repository.has("From Zero",
                LocalDate.of(2024, 10, 15));

        // Precondition
        assumeFalse(isRegistered.get());

        // Do
        assertDoesNotThrow(() -> {
            var poster = somePoster();
            var user = someAdmin();
            var currentDay = LocalDate.of(2024, 10, 1);

            new EventRegistering(
                    repository)
                            .poster(poster)
                            .by(user)
                            .on(currentDay)
                            .register();

        });

        // Assertions
        var event = actualEvent(repository);
        assertTrue(isRegistered.get());
        assertTrue(event.isPresent());
    }

    @Scenario("Successfully registering an Event")
    @Given("Some Post, an Admin user, and the Current Day")
    @When("Registering a new future Event as an Admin")
    @Then("Registered event is the same")
    @Test
    void shouldBeTheSame() {

        // Do
        assertDoesNotThrow(() -> {
            var poster = somePoster();
            var user = someAdmin();
            var currentDay = LocalDate.of(2024, 10, 1);

            new EventRegistering(
                    repository)
                            .poster(poster).by(user)
                            .on(currentDay)
                            .register();
        });

        // Assertions
        var event = actualEvent(repository).get();
        assertEquals("From Zero", event.poster().title());
        assertEquals("Linkin Park's show", event.poster().description());
        assertEquals(LocalDate.of(2024, 10, 15), event.poster().date());
    }

    @Scenario("Customer trying to register an Event")
    @Given("Some Post, a Customer User, and the Current Day")
    @When("Trying to register a new future Event as a Customer")
    @Then("Should not register and throw PermissionDenied")
    @Test
    void shouldNotAllow() {
        assertThrows(PermissionDenied.class, () -> {
            var poster = somePoster();
            var user = someCustomer();
            var currentDay = LocalDate.of(2024, 10, 1);

            new EventRegistering(
                    repository)
                            .poster(poster)
                            .by(user)
                            .on(currentDay)
                            .register();

        });
        assertFalse(repository.has("From Zero", LocalDate.of(2024, 10, 15)));
    }

    @Scenario("Admin trying to re-register an Event")
    @Given("Some Post, an Admin User, and the Current Day")
    @When("Trying to register an already registered Event")
    @Then("Should not re-register and throw EventAlreadyRegistered")
    @Test
    void shouldNotAllowReRegister() {
        assertThrows(EventAlreadyRegistered.class, () -> {
            var poster = somePoster();
            var user = someAdmin();
            var currentDay = LocalDate.of(2024, 10, 1);
            var context = new EventRegistering(
                    repository)
                            .poster(poster)
                            .by(user)
                            .on(currentDay);

            context.register();
            context.register();
        });
        assertTrue(repository.has("From Zero", LocalDate.of(2024, 10, 15)));
    }

    @Scenario("An Admin trying to register a past Event")
    @Given("Some Post, an Admin User, and the Current Day")
    @When("Trying to register a past Event")
    @Then("Should not register on the same day")
    @Test
    void shouldNotRegisterForSameDate() {
        // Pre-Condition
        var today = LocalDate.of(2024, 10, 15);
        assumeTrue(somePoster().date().isEqual(today));
        // Do
        assertThrows(CantRegisterPastEvent.class, () -> {
            var poster = somePoster();
            var user = someAdmin();

            new EventRegistering(
                    repository)
                            .poster(poster)
                            .by(user)
                            .on(today)
                            .register();
        });

        // Post-Condition
        assertFalse(repository.has("From Zero", LocalDate.of(2024, 10, 15)));
    }

    @Scenario("An Admin trying to register a past Event")
    @Given("Some Post, an Admin User, and the Current Day")
    @When("Trying to register a past Event")
    @Then("Should not register for past days")
    @Test
    void shouldNotRegisterForPastDay() {
        // Pre-Condition
        var today = LocalDate.of(2024, 10, 16);
        assumeTrue(somePoster().date().isBefore(today));
        // Do
        assertThrows(CantRegisterPastEvent.class, () -> {
            var poster = somePoster();
            var user = someAdmin();

            new EventRegistering(
                    repository)
                            .poster(poster)
                            .by(user)
                            .on(today)
                            .register();
        });

        // Post-Condition
        assertFalse(repository.has("From Zero", LocalDate.of(2024, 10, 15)));
    }
}
