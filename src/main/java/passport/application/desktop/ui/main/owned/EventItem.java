package passport.application.desktop.ui.main.owned;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import passport.application.desktop.system.PassPort;
import passport.domain.models.events.Event;

/**
 * Represents an event item button in the user's owned events list.
 */
public class EventItem extends Button {
    final PassPort app;
    final Event event;
    final Consumer<Event> openEvent;
    final Properties props;

    /**
     * Holds properties for the event item, including title and date.
     */
    class Properties {
        final String title;
        final LocalDate date;

        /**
         * Initializes the properties for the event item.
         *
         * @param event The event associated with this item.
         */
        public Properties(Event event) {
            this.title = event.poster().title();
            this.date = event.poster().date();
        }
    }

    /**
     * Private constructor for EventItem.
     *
     * @param app       The PassPort application instance.
     * @param event     The event associated with this item.
     * @param openEvent The action to open the event details.
     */
    private EventItem(PassPort app, Event event, Consumer<Event> openEvent) {
        this.app = app;
        this.event = event;
        this.props = new Properties(event);
        this.openEvent = openEvent;

        this.setup();
        this.setOnAction(_ -> openEvent.accept(event));
    }

    /**
     * Creates a new EventItem instance.
     *
     * @param app       The PassPort application instance.
     * @param event     The event associated with this item.
     * @param openEvent The action to open the event details.
     * @return A new EventItem instance.
     */
    static EventItem of(PassPort app, Event event, Consumer<Event> openEvent) {
        return new EventItem(app, event, openEvent);
    }

    /**
     * Sets up the event item.
     */
    private void setup() {
        this.setContent();
        this.setMinWidth(300);
        this.setPrefWidth(500);
    }

    /**
     * Creates a VBox containing the event information.
     *
     * @return A VBox containing the event information.
     */
    private VBox eventInfo() {
        var title = titleLabel();
        var date = dateLabel();

        var contentBox = new VBox(title, date);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        return contentBox;
    }

    /**
     * Creates a Label with the event title.
     *
     * @return A Label with the event title.
     */
    private Label titleLabel() {
        var title = new Label(props.title);
        title.getStyleClass().add("title-3");

        return title;
    }

    /**
     * Creates a Label with the event date.
     *
     * @return A Label with the event date.
     */
    private Label dateLabel() {
        final String dateContent = DateTimeFormatter
                .ofPattern("MMM d, yyyy")
                .format(props.date);
        var date = new Label(dateContent);
        date.setAccessibleText(dateContent);
        date.getStyleClass().add("text-small");

        return date;
    }

    /**
     * Creates an HBox container for the content.
     *
     * @param contentBox The content to be added to the container.
     * @return An HBox container.
     */
    private HBox container(VBox contentBox) {
        var container = new HBox(contentBox);
        container.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        return container;
    }

    /**
     * Sets the content of the event item.
     */
    private void setContent() {
        var info = eventInfo();
        var container = container(info);
        this.setGraphic(container);
    }

}
