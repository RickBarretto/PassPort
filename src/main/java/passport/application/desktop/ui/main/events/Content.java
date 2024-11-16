package passport.application.desktop.ui.main.events;

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
    private final Components ui;
    private final ObservableList<Event> events;

    class Components {
        final Label eventsTitle = new Label();

        public Components() { this.eventsTitle.getStyleClass().add("title-1"); }
    }

    public Content(PassPort app) {
        this.app = app;
        this.ui = new Components();
        this.events = FXCollections.observableArrayList();

        this.setupLayout();
        this.translate();
        this.updateEvents(this.loadList());
    }

    private void openEvent(Event event) { app.toEventPurchase(event); }

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
        var eventsContainer = eventsContainer();

        var box = new VBox(ui.eventsTitle, eventsContainer);
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
                            .add(EventItem.of(event, this::openEvent)));
                });
        return eventsContainer;
    }

    public void updateEvents(List<Event> newEvents) {
        events.setAll(newEvents);
    }

    private void translate() {
        app.translator()
                .translateFrom(ui.eventsTitle::setText, "main.events.title")
                .resourcesProp().addListener((_, _, _) -> this.translate());
    }
}