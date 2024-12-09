package passport.domain.contexts.events;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import passport.domain.models.events.Event;
import passport.domain.models.events.EventCategory;
import passport.roles.Context;
import passport.roles.repositories.Events;

/**
 * Context for listing available events starting from today.
 */
public class AvailableEventsListing implements Context {
    private Predicate<Event> filter;
    private Comparator<Event> sorter;
    private LocalDate today;
    private Events events;
    private LocalDate eventDate;

    static class DefaultModifiers {
        static final Predicate<Event> acceptAll = _ -> true;
        static final Comparator<Event> byDate = Comparator.comparing(
                (event) -> event.poster().date());
    }

    /**
     * Constructor with the specified events repository.
     *
     * @param events the repository of events used for listing
     */
    public AvailableEventsListing(Events events) {
        setDefaultModifiers();
        this.events = events;
    }

    private void setDefaultModifiers() {
        this.filter = DefaultModifiers.acceptAll;
        this.sorter = DefaultModifiers.byDate;
    }

    /**
     * Sets the current date for the context.
     *
     * @param today the current date
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing beingToday(LocalDate today) {
        this.today = today;
        return this;
    }

    /**
     * Sets the filter by exact name.
     *
     * @param sample the name to filter by
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing including(String sample) {
        this.filter = this.filter.and(
                event -> {
                    final String caselessTitle = event.poster().title()
                            .toLowerCase();
                    final String caselessSample = sample.toLowerCase();
                    return caselessTitle.contains(caselessSample);
                });
        return this;
    }

    /**
     * Sets the filter by category.
     *
     * @param category the category to filter by
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing withCategory(EventCategory category) {
        this.filter = this.filter
                .and(event -> event.poster().category().equals(category));
        return this;
    }

    /**
     * Sets the filter by date.
     *
     * @param date the date to filter by
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing withDate(LocalDate date) {
        this.filter = this.filter.and(event -> {
            final LocalDate eventDate = event.poster().date();
            return eventDate.equals(date);
        });
        return this;
    }

    /**
     * Sets the filter by date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing intoInterval(LocalDate startDate,
            LocalDate endDate) {
        this.filter = this.filter
                .and(event -> {
                    eventDate = event.poster().date();
                    return !eventDate.isBefore(startDate)
                            && !event.poster().date().isAfter(endDate);
                });
        return this;
    }

    /**
     * Sets the sorter by name.
     *
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing sortedByTitle() {
        this.sorter = Comparator.comparing(event -> event.poster().title());
        return this;
    }

    /**
     * Sets the sorter by date.
     *
     * @return the updated AvailableEventsListing object
     */
    public AvailableEventsListing sortedByDate() {
        this.sorter = Comparator.comparing(event -> event.poster().date());
        return this;
    }

    /**
     * Retrieves the list of available events starting from the set date.
     *
     * @return a list of available events
     * @throws NullPointerException if the current date or events repository is
     *                                  not set
     */
    public List<Event> availables() {
        Objects.requireNonNull(today, "Today date cannot be null");
        Objects.requireNonNull(events, "Events repository cannot be null");
        var filteredEvents = events.availableOn(today).stream()
                .filter(filter)
                .sorted(sorter)
                .collect(Collectors.toList());

        this.setDefaultModifiers();
        return filteredEvents;
    }
}
