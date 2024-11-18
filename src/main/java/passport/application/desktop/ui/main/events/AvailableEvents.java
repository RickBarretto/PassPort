package passport.application.desktop.ui.main.events;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.domain.models.events.Event;
import passport.domain.models.events.EventCategory;

public class AvailableEvents extends VBox {
    private final PassPort app;
    private final Components ui;
    private final ObservableList<Event> events;

    class Components {
        final Label eventsTitle = new Label();
        final TextField searchBar = new TextField();
        final ChoiceBox<String> categoryFilter = new ChoiceBox<>();
        final DatePicker startDatePicker = new DatePicker();
        final DatePicker endDatePicker = new DatePicker();
        final ChoiceBox<String> sortSelector = new ChoiceBox<>();
        final Button filterButton = new Button("Filter");

        public Components() { this.eventsTitle.getStyleClass().add("title-1"); }
    }

    public AvailableEvents(PassPort app) {
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
        this.setPadding(new Insets(10, 10, 20, 10));
        this.getChildren().addAll(ui.eventsTitle, filtersBox(), searchBox(),
                eventsBox());
    }

    private VBox searchBox() {
        ui.searchBar.setPromptText("Search...");
        ui.searchBar.setMinWidth(400);
        ui.searchBar.setPrefWidth(850);
        var searchBox = new VBox(ui.searchBar);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        return searchBox;
    }

    private HBox filtersBox() {
        var filtersBox = new HBox(10,
                ui.categoryFilter,
                ui.startDatePicker,
                ui.endDatePicker,
                ui.sortSelector,
                ui.filterButton);

        Stream.of(EventCategory.values())
                .map(category -> app.translator().translationOf(
                        "category." + category.name().toLowerCase()))
                .forEach(ui.categoryFilter.getItems()::add);

        ui.categoryFilter.setPrefWidth(150);
        ui.startDatePicker.setPrefWidth(150);
        ui.endDatePicker.setPrefWidth(150);
        ui.sortSelector.setPrefWidth(150);
        ui.filterButton.setPrefWidth(100);

        ui.sortSelector.setItems(FXCollections.observableArrayList(
                app.translator().translationOf("sort.title"),
                app.translator().translationOf("sort.date")));

        ui.filterButton.setOnAction(e -> filter());

        filtersBox.setAlignment(Pos.CENTER);
        filtersBox.setPadding(new Insets(10));
        filtersBox.setMaxWidth(850); // Make the filters box wider

        return filtersBox;
    }

    private void filter() {
        var listing = app.services()
                .eventsListing()
                .beingToday(LocalDate.now());

        if (!ui.searchBar.getText().isEmpty()) {
            listing = listing.including(ui.searchBar.getText());
        }

        if (ui.categoryFilter.getValue() != null) {
            EventCategory selectedCategory = EventCategory
                    .valueOf(ui.categoryFilter.getValue().toUpperCase());
            listing = listing.withCategory(selectedCategory);
        }

        if (ui.startDatePicker.getValue() != null
                && ui.endDatePicker.getValue() != null) {
            listing = listing.intoInterval(ui.startDatePicker.getValue(),
                    ui.endDatePicker.getValue());
        }

        String selectedSort = ui.sortSelector.getValue();
        if (selectedSort != null) {
            if (selectedSort
                    .equals(app.translator().translationOf("sort.name"))) {
                listing = listing.sortedByTitle();
            }
            else if (selectedSort
                    .equals(app.translator().translationOf("sort.date"))) {
                listing = listing.sortedByDate();
            }
        }

        updateEvents(listing.availables());
    }

    private VBox eventsBox() {
        var eventsContainer = eventsContainer();

        var box = new VBox(eventsScroll(eventsContainer));
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(20);

        return box;
    }

    private VBox eventsContainer() {
        var eventsContainer = new VBox(10);
        eventsContainer.setAlignment(Pos.TOP_CENTER);

        events.addListener(
                (ListChangeListener.Change<? extends Event> _) -> {
                    eventsContainer.getChildren().clear();
                    events.stream().forEach((event) -> eventsContainer
                            .getChildren()
                            .add(EventItem.of(app, event, this::openEvent)));
                });
        return eventsContainer;
    }

    private ScrollPane eventsScroll(VBox eventsContainer) {
        var scrollPane = new ScrollPane(eventsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
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
