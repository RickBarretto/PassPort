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

public class EventItem extends Button {
    final PassPort app;
    final Event event;
    final Consumer<Event> openEvent;
    final Properties props;

    class Properties {
        final String title;
        final LocalDate date;
        
        public Properties(Event event) {
            this.title = event.poster().title();
            this.date = event.poster().date();
        }
    }

    private EventItem(PassPort app, Event event, Consumer<Event> openEvent) {
        this.app = app;
        this.event = event;
        this.props = new Properties(event);
        this.openEvent = openEvent;

        this.setup();
        this.setOnAction(_ -> openEvent.accept(event));
    }

    static EventItem of(PassPort app, Event event, Consumer<Event> openEvent) {
        return new EventItem(app, event, openEvent);
    }

    private void setup() {
        this.setContent();
        this.setMinWidth(300);
        this.setPrefWidth(500);
    }

    private VBox eventInfo() {
        var title = titleLabel();
        var date = dateLabel();

        var contentBox = new VBox(title, date);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        return contentBox;
    }

    private Label titleLabel() {
        var title = new Label(props.title);
        title.getStyleClass().add("title-3");

        return title;
    }

    private Label dateLabel() {
        final String dateContent = DateTimeFormatter
                .ofPattern("MMM d, yyyy")
                .format(props.date);
        var date = new Label(dateContent);
        date.setAccessibleText(dateContent);
        date.getStyleClass().add("text-small");

        return date;
    }

    private HBox container(VBox contentBox) {
        var container = new HBox(contentBox);
        container.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        return container;
    }

    private void setContent() {
        var info = eventInfo();
        var container = container(info);
        this.setGraphic(container);
    }

}