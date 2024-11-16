package passport.application.desktop.ui.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import passport.domain.models.events.Event;

public class EventItem extends Button {
    final String title;
    final LocalDate date;

    private EventItem(Event event) {
        this.title = event.poster().title();
        this.date = event.poster().date();
        this.setup();
    }

    static EventItem of(Event event) { return new EventItem(event); }

    private void setup() {
        var title = new Label(this.title);
        title.getStyleClass().add("title-3");

        var date = new Label(
                DateTimeFormatter
                        .ofPattern("MMM d, yyyy")
                        .format(this.date));
        date.getStyleClass().add("text-small");
        
        var container = new VBox(title, date);
        this.setGraphic(container);

        this.setMinWidth(300);
        this.setPrefWidth(450);
    }

}
