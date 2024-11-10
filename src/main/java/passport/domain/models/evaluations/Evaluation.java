package passport.domain.models.evaluations;

import passport.domain.models.events.EventId;
import passport.domain.models.users.UserId;
import passport.roles.Entity;

/**
 * Represents an evaluation of an event, including the event ID, author ID, and
 * comment.
 */
public class Evaluation implements Entity<EvaluationId> {
    private final EvaluationId id;
    private final EventId event;
    private final UserId author;
    private final String comment;

    /**
     * Constructs a new Evaluation with the specified event ID, author ID, and
     * comment.
     *
     * @param event   the event ID being evaluated
     * @param author  the user ID of the author of the evaluation
     * @param comment the evaluation comment
     */
    public Evaluation(EventId event, UserId author, String comment) {
        this(new EvaluationId(), event, author, comment);
    }

    /**
     * Constructs a new Evaluation with the specified evaluation ID, event ID,
     * author ID, and comment.
     *
     * @param id      the evaluation ID
     * @param event   the event ID being evaluated
     * @param author  the user ID of the author of the evaluation
     * @param comment the evaluation comment
     */
    public Evaluation(EvaluationId id, EventId event, UserId author,
            String comment) {
        this.id = id;
        this.author = author;
        this.event = event;
        this.comment = comment;
    }

    /**
     * Returns the evaluation ID.
     *
     * @return the evaluation ID
     */
    public EvaluationId id() { return id; }

    /**
     * Returns the user ID of the author of the evaluation.
     *
     * @return the author ID
     */
    public UserId author() { return author; }

    /**
     * Returns the event ID being evaluated.
     *
     * @return the event ID
     */
    public EventId event() { return event; }

    /**
     * Returns the evaluation comment.
     *
     * @return the evaluation comment
     */
    public String comment() { return comment; }
}
