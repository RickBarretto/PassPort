package passport.application.desktop.ui.main;

import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import passport.domain.models.events.Event;
import passport.domain.models.events.Poster;

public class Content extends VBox {

    public Content() {
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(events());
    }

    private VBox events() {
        var label = new Label("Available Events");
        label.getStyleClass().add("title-1");

        var events = new VBox(10);
        events.setAlignment(Pos.TOP_CENTER);

        events.getChildren().addAll(
                EventItem.of(new Event(new Poster("From Zero", "LP show",
                        LocalDate.of(2024, 11, 15)))));
        var box = new VBox(label, events);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(20);

        return box;
    }

}
