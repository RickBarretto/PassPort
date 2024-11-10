package passport.context.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passport.domain.contexts.events.AvailableEventsListing;
import passport.domain.models.events.Event;
import passport.domain.models.events.Poster;
import passport.infra.virtual.EventsInMemory;
import passport.resources.bdd.Feature;
import passport.resources.bdd.Given;
import passport.resources.bdd.Scenario;
import passport.resources.bdd.Then;
import passport.resources.bdd.When;
import passport.roles.repositories.Events;

@Feature("Listing available events")
public class AvailableEventsListingFeature {

    private LocalDate today;
    private Events allEvents;
    private List<Event> listedEvents;
    private List<Event> available;
    private List<Event> unavailable;

    @BeforeEach
    void init() {
        today = LocalDate.of(2024, 10, 15);
        available = availableEvents();
        unavailable = unavailableEvents();
        allEvents = new EventsInMemory();
        available.forEach(allEvents::register);
        unavailable.forEach(allEvents::register);
    }

    // @formatter:off
    List<Event> availableEvents() {
        return List.of(
            new Event(new Poster("Available", "...", LocalDate.of(2024, 10, 15))),
            new Event(new Poster("Available", "...", LocalDate.of(2024, 10, 16))),
            new Event(new Poster("Available", "...", LocalDate.of(2024, 11, 15))),
            new Event(new Poster("Available", "...", LocalDate.of(2024, 10, 15))),
            new Event(new Poster("Available", "...", LocalDate.of(2030, 1, 15))));
    }
                    
    List<Event> unavailableEvents() {
        return List.of(
            new Event(new Poster("Unavailable", "...", LocalDate.of(2023, 10, 15))),
            new Event(new Poster("Unavailable", "...", LocalDate.of(2024, 10, 14))),
            new Event(new Poster("Unavailable", "...", LocalDate.of(2024, 9, 16))),
            new Event(new Poster("Unavailable", "...", LocalDate.of(2022, 10, 15))));
    }
    // @formatter:on

    @Scenario("Listing available events")
    @Given("A group of available and not available Events")
    @When("Listing available events")
    @Then("Listed Events should include all available ones")
    @Test
    void shouldIncludeAvailables() {
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .availables();

        assertTrue(listedEvents.stream()
                .allMatch(available::contains));
    }

    @Scenario("Listing available events")
    @Given("A group of available and not available Events")
    @When("Listing available events")
    @Then("Listed Events should ignore all unavailable ones")
    @Test
    void shouldIgnorePastEvents() {
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .availables();

        assertFalse(listedEvents.stream()
                .anyMatch(unavailable::contains));
    }
}
