package passport.application.desktop.ui.main;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.domain.models.events.Event;

public class Content extends VBox {
    private final PassPort app;
    private final ObservableList<Event> events;

    public Content(PassPort app) {
        this.app = app;
        this.events = FXCollections.observableArrayList();

        setupLayout();
        updateEvents(this.loadList());
    }

    private List<Event> loadList() {
        return app.services()
            .eventsListing()
            .beingToday(LocalDate.now())
            .availables();
    }

    private void setupLayout() {
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(eventsBox());
    }

    private VBox eventsBox() {
        var label = new Label("Available Events");
        label.getStyleClass().add("title-1");

        var eventsContainer = eventsContainer();

        var box = new VBox(label, eventsContainer);
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(20);

        return box;
    }

    private VBox eventsContainer() {
        var eventsContainer = new VBox(10);
        eventsContainer.setAlignment(Pos.TOP_CENTER);

        events.addListener(
                (ListChangeListener.Change<? extends Event> change) -> {
                    eventsContainer.getChildren().clear();
                    events.stream().forEach((event) -> eventsContainer
                            .getChildren()
                            .add(EventItem.of(event)));
                });
        return eventsContainer;
    }

    public void updateEvents(List<Event> newEvents) {
        events.setAll(newEvents);
    }
}
