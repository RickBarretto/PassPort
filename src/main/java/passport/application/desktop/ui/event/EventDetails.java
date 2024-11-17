package passport.application.desktop.ui.event;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.domain.models.events.Event;

public class EventDetails extends VBox {
    private final PassPort app;
    private final Label titleLabel;
    private final TextArea descriptionArea;
    private final Label priceLabel;

    public EventDetails(PassPort app, Event event) {
        super(10);

        this.app = app;
        this.titleLabel = title(event);
        this.descriptionArea = description(event);
        this.priceLabel = price(event);

        getChildren().addAll(titleLabel, descriptionArea, priceLabel);
    }

    private Label title(Event event) {
        Label title = new Label(event.poster().title());
        title.getStyleClass().add("title-1");
        return title;
    }

    private TextArea description(Event event) {
        TextArea description = new TextArea(event.poster().description());
        description.setWrapText(true);
        description.setEditable(false);
        description.setPrefRowCount(10);
        description.setFocusTraversable(false);
        return description;
    }

    private Label price(Event event) {
        final boolean isSoldOut = event.boxOffice().isSoldOut();
        
        String priceContent = app.translator().translationOf(
                isSoldOut ? "events.sold-out"
                        : "events.price");

        if (!isSoldOut) {
            priceContent = String.format(
                    priceContent, event.boxOffice().ticket().price());
        }

        var price = new Label(priceContent);
        price.getStyleClass().add("title-3");
        return price;
    }
}