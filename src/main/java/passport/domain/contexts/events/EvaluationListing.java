package passport.domain.contexts.events;

import java.util.List;
import java.util.stream.Collectors;

import passport.domain.models.events.Event;
import passport.roles.Context;
import passport.roles.repositories.Users;

/**
 * Context for evaluating an event.
 */
public class EvaluationListing implements Context {
    private Users users;

    public record Comment(String name, String content) {
        @Override
        public String toString() { return name + ": " + content; }
    }

    /**
     * Constructor with the specified users repository.
     *
     * @param users the repository of users
     */
    public EvaluationListing(Users users) { this.users = users; }

    /**
     * Sets the event ID for the evaluation.
     *
     * @param event the event
     * @return the updated EvaluationListing object
     */
    public List<Comment> of(Event event) {
        var evaluations = event.evaluations();
        return evaluations.stream()
                .map(eval -> {
                    final String username = users.byId(eval.author()).get()
                            .person()
                            .name();

                    return new Comment(username, eval.comment());
                })
                .collect(Collectors.toList());
    }

}
