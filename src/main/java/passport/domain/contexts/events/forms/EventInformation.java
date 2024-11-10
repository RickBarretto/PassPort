package passport.domain.contexts.events.forms;

import java.time.LocalDate;
import java.util.Objects;

import passport.domain.models.events.Poster;

/**
 * Represents the information required for an event, including the title,
 * description, and schedule.
 */
public class EventInformation {
    private String title;
    private String description;
    private LocalDate schedule;

    /**
     * Sets the title of the event.
     *
     * @param title the title of the event
     * @return the updated EventInformation object
     */
    public EventInformation title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the description of the event.
     *
     * @param description the description of the event
     * @return the updated EventInformation object
     */
    public EventInformation description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the schedule date of the event.
     *
     * @param schedule the date the event is scheduled for
     * @return the updated EventInformation object
     */
    public EventInformation scheduledFor(LocalDate schedule) {
        this.schedule = schedule;
        return this;
    }

    /**
     * Submits the event information and returns a Poster object containing the
     * title, description, and schedule.
     *
     * @return a new Poster object with the event information
     * @throws NullPointerException if any of the required fields (title,
     *                                  description, schedule) are null
     */
    public Poster submit() {
        Objects.requireNonNull(title, "Title cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(schedule, "Schedule cannot be null");
        
        return new Poster(title, description, schedule);
    }
}
