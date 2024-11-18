package passport.domain.models.events;

import java.time.LocalDate;

/**
 * Represents an event poster with a title, description, and schedule date.
 */
public class Poster {
    private final String title;
    private final String description;
    private final String schedule;
    private final EventCategory category;

    /**
     * Constructs a new Poster with the specified title, description, and
     * schedule date.
     *
     * @param title       the title of the event
     * @param description the description of the event
     * @param schedule    the date the event is scheduled for
     * @param music 
     */
    public Poster(String title, String description, LocalDate schedule,
            EventCategory category) {
        this.title = title;
        this.description = description;
        this.schedule = schedule.toString();
        this.category = category;
    }
    
    public Poster(String title, String description, LocalDate schedule) {
        this(title, description, schedule, EventCategory.NOT_PROVIDED);
    }

    /**
     * Returns the title of the event.
     *
     * @return the event title
     */
    public String title() { return title; }

    /**
     * Returns the description of the event.
     *
     * @return the event description
     */
    public String description() { return description; }

    /**
     * Returns the schedule date of the event.
     *
     * @return the event schedule date
     */
    public LocalDate date() { return LocalDate.parse(schedule); }
   
    /**
     * Returns the category of the event.
     *
     * @return the event's category
     */
    public EventCategory category() { return category; }
}
