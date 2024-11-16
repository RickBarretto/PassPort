package passport.application.desktop.ui.event;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import passport.domain.models.events.Event;

public class EventDetails extends VBox {
    private final Label titleLabel;
    private final TextArea descriptionArea;
    private final Label priceLabel;

    public EventDetails(Event event) {
        super(10);
        this.titleLabel = createTitle(event);
        this.descriptionArea = createDescription(event);
        this.priceLabel = createPrice(event);

        getChildren().addAll(titleLabel, descriptionArea, priceLabel);
    }

    private Label createTitle(Event event) {
        Label label = new Label(event.poster().title());
        label.getStyleClass().add("title-1");
        return label;
    }

    private TextArea createDescription(Event event) {
        TextArea area = new TextArea(event.poster().description());
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefRowCount(3);
        return area;
    }

    private Label createPrice(Event event) {
        Label label = new Label(String.format("Price: $%.2f",
                event.boxOffice().ticket().price()));
        label.getStyleClass().add("title-3");
        return label;
    }
}