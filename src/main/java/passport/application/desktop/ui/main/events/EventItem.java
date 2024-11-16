package passport.application.desktop.ui.main.events;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.event.EventType;
import javafx.geometry.Insets;
import passport.application.desktop.system.PassPort;
import passport.domain.models.events.Event;

public class EventItem extends Button {
    final Consumer<Event> openEvent;
    final Properties props;

    class Properties {
        final String title;
        final LocalDate date;
        final Double price;

        public Properties(Event event) {
            this.title = event.poster().title();
            this.date = event.poster().date();
            this.price = event.boxOffice().ticket().price();
        }
    }

    private EventItem(Event event, Consumer<Event> openEvent) {
        this.props = new Properties(event);
        this.openEvent = openEvent;
        this.setup();

        this.setOnAction(_ -> openEvent.accept(event));
    }

    static EventItem of(Event event, Consumer<Event> openEvent) {
        return new EventItem(event, openEvent);
    }

    private void setup() {
        var info = eventInfo();
        var container = container(info, priceBox(priceLabel()));

        this.setGraphic(container);
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
        var date = new Label(DateTimeFormatter
                .ofPattern("MMM d, yyyy")
                .format(props.date));
        date.getStyleClass().add("text-small");

        return date;
    }

    private HBox container(VBox contentBox, VBox priceBox) {
        var container = new HBox(contentBox, priceBox);
        container.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        return container;
    }

    private Label priceLabel() {
        NumberFormat currencyFormatter = NumberFormat
                .getCurrencyInstance(Locale.getDefault());

        return new Label(currencyFormatter.format(props.price));
    }

    private VBox priceBox(Label priceLabel) {
        var priceBox = new VBox(priceLabel);
        priceBox.setAlignment(Pos.CENTER);
        priceBox.setPadding(new Insets(0, 0, 0, 10));
        priceLabel.getStyleClass().add("title-3");

        return priceBox;
    }
}