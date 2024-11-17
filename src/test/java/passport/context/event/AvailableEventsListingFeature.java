package passport.context.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import passport.domain.contexts.events.AvailableEventsListing;
import passport.domain.models.events.Event;
import passport.domain.models.events.EventCategory;
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
            new Event(new Poster("Available", "...", LocalDate.of(2024, 10, 15), EventCategory.MUSIC)),
            new Event(new Poster("Available", "...", LocalDate.of(2024, 10, 16), EventCategory.THEATRE)),
            new Event(new Poster("Available", "...", LocalDate.of(2024, 11, 15), EventCategory.MUSIC)),
            new Event(new Poster("Available", "...", LocalDate.of(2024, 10, 15), EventCategory.CONFERENCE)),
            new Event(new Poster("Available", "...", LocalDate.of(2030, 1, 15), EventCategory.FESTIVAL)));
    }
                    
    List<Event> unavailableEvents() {
        return List.of(
            new Event(new Poster("Unavailable", "...", LocalDate.of(2023, 10, 15), EventCategory.MUSIC)),
            new Event(new Poster("Unavailable", "...", LocalDate.of(2024, 10, 14), EventCategory.THEATRE)),
            new Event(new Poster("Unavailable", "...", LocalDate.of(2024, 9, 16), EventCategory.MUSIC)),
            new Event(new Poster("Unavailable", "...", LocalDate.of(2022, 10, 15), EventCategory.CONFERENCE)));
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

    @Scenario("Filtering events by category")
    @Given("A group of events with different categories")
    @When("Filtering events by category MUSIC")
    @Then("Listed events should include only music events")
    @Test
    void shouldFilterByCategory() {
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .withCategory(EventCategory.MUSIC)
                        .availables();

        assertTrue(listedEvents.stream()
                .allMatch(event -> event.poster()
                        .category() == EventCategory.MUSIC));
    }

    @Scenario("Filtering events by date")
    @Given("A group of events with different dates")
    @When("Filtering events by a specific date")
    @Then("Listed events should include only events on that date")
    @Test
    void shouldFilterByDate() {
        LocalDate date = LocalDate.of(2024, 10, 15);
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .withDate(date)
                        .availables();

        assertTrue(listedEvents.stream()
                .allMatch(event -> event.poster().date().equals(date)));
    }

    @Scenario("Filtering events by date range")
    @Given("A group of events with different dates")
    @When("Filtering events by a date range")
    @Then("Listed events should include only events within that date range")
    @Test
    void shouldFilterByDateRange() {
        LocalDate startDate = LocalDate.of(2024, 10, 15);
        LocalDate endDate = LocalDate.of(2024, 11, 15);
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .intoInterval(startDate, endDate)
                        .availables();

        assertTrue(listedEvents.stream()
                .allMatch(event -> !event.poster().date().isBefore(startDate)
                        && !event.poster().date().isAfter(endDate)));
    }

    @Scenario("Searching events by partial name")
    @Given("A group of events with different titles")
    @When("Searching events by partial name")
    @Then("Listed events should include events matching the partial name")
    @Test
    void shouldSearchByPartialName() {
        String partialName = "Avail";
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .including(partialName)
                        .availables();

        assertTrue(listedEvents.stream()
                .allMatch(event -> event.poster().title().toLowerCase()
                        .contains(partialName.toLowerCase())));
    }

    @Scenario("Sorting events by title")
    @Given("A group of events with different titles")
    @When("Sorting events by title")
    @Then("Listed events should be sorted alphabetically by title")
    @Test
    void shouldSortByTitle() {
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .sortedByTitle()
                        .availables();

        List<String> titles = listedEvents.stream()
                .map(event -> event.poster().title())
                .collect(Collectors.toList());
        List<String> sortedTitles = titles.stream()
                .sorted()
                .collect(Collectors.toList());

        assertEquals(sortedTitles, titles);
    }

    @Scenario("Sorting events by date")
    @Given("A group of events with different dates")
    @When("Sorting events by date")
    @Then("Listed events should be sorted chronologically by date")
    @Test
    void shouldSortByDate() {
        listedEvents = new AvailableEventsListing(
                allEvents)
                        .beingToday(today)
                        .sortedByDate()
                        .availables();

        List<LocalDate> dates = listedEvents.stream()
                .map(event -> event.poster().date())
                .collect(Collectors.toList());
        List<LocalDate> sortedDates = dates.stream()
                .sorted()
                .collect(Collectors.toList());

        assertEquals(sortedDates, dates);
    }
}
