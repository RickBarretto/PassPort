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
import javafx.geometry.Insets;
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
        final Double price;
        final String category;

        public Properties(Event event) {
            this.title = event.poster().title();
            this.date = event.poster().date();
            this.price = event.boxOffice().ticket().price();
            this.category = event.poster().category().toString();
        }
    }

    private EventItem(PassPort app, Event event, Consumer<Event> openEvent) {
        this.app = app;
        this.event = event;
        this.props = new Properties(event);
        this.openEvent = openEvent;

        this.setup();
        app.translator().resourcesProp()
                .addListener((_, _, _) -> this.translate());
        this.setOnAction(_ -> openEvent.accept(event));
        this.translate();
    }

    static EventItem of(PassPort app, Event event, Consumer<Event> openEvent) {
        return new EventItem(app, event, openEvent);
    }

    private void setup() {
        this.setContent(formattedPrice());
        this.setMinWidth(300);
        this.setPrefWidth(500);
    }

    private VBox eventInfo() {
        var title = titleLabel();
        var date = dateLabel();
        var category = category();

        var contentBox = new VBox(title, date, category);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        return contentBox;
    }

    private Label titleLabel() {
        var title = new Label(props.title);
        title.getStyleClass().add("title-3");

        return title;
    }

    private Label dateLabel() {
        final String dateFormat = DateTimeFormatter
                .ofPattern("MMM d, yyyy")
                .format(props.date);
        var date = new Label(dateFormat);
        date.setAccessibleText(dateFormat);
        date.getStyleClass().add("text-small");

        return date;
    }

    private Label category() {
        var category = new Label(app.translator().translationOf(
                props.category));
        category.setAccessibleText(app.translator().translationOf(
                props.category));
        category.getStyleClass().add("text-small");
        category.setPadding(new Insets(5, 0, 0, 0));

        return category;
    }

    private HBox container(VBox contentBox, VBox priceBox) {
        var container = new HBox(contentBox, priceBox);
        container.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        return container;
    }

    private String formattedPrice() {
        NumberFormat currency = NumberFormat
                .getCurrencyInstance(Locale.getDefault());
        return currency.format(props.price);
    }

    private void translate() {
        if (!event.boxOffice().isSoldOut()) {
            this.setContent(this.formattedPrice());
            this.setAccessibleText(this.formattedPrice());
        }
        else {
            this.setContent(app.translator().translationOf("events.sold-out"));
            this.setAccessibleText(
                    app.translator().translationOf("events.sold-out"));
        }
    }

    private void setContent(String priceContent) {
        var info = eventInfo();
        var price = new Label(priceContent);
        var container = container(info, priceBox(price));

        this.setGraphic(container);
    }

    private VBox priceBox(Label priceLabel) {
        var priceBox = new VBox(priceLabel);
        priceBox.setAlignment(Pos.CENTER);
        priceBox.setPadding(new Insets(0, 0, 0, 10));
        priceLabel.getStyleClass().add("title-3");

        return priceBox;
    }
}
