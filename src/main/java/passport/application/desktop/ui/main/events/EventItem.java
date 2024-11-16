package passport.application.desktop.ui.main.events;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import passport.domain.models.events.Event;

public class EventItem extends Button {
    final String title;
    final LocalDate date;
    final Double price;

    private EventItem(Event event) {
        this.title = event.poster().title();
        this.date = event.poster().date();
        this.price = event.boxOffice().ticket().price();
        this.setup();
    }

    static EventItem of(Event event) { return new EventItem(event); }

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
        var title = new Label(this.title);
        title.getStyleClass().add("title-3");
        return title;
    }

    private Label dateLabel() {
        var date = new Label(
                DateTimeFormatter
                        .ofPattern("MMM d, yyyy")
                        .format(this.date));
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
        var priceLabel = new Label(currencyFormatter.format(this.price));
        return priceLabel;
    }

    private VBox priceBox(Label priceLabel) {
        var priceBox = new VBox(priceLabel);
        priceBox.setAlignment(Pos.CENTER);
        priceBox.setPadding(new Insets(0, 0, 0, 10));
        priceLabel.getStyleClass().add("title-3");
        return priceBox;
    }
}