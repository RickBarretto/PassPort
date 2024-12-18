/**
 * Welcome to the "AvailableEvents" class, where code hygiene is a distant
 * dream. This class is a prime example of what happens when you ignore Uncle
 * Bob's advice. Proceed with caution, and may the refactoring gods have mercy
 * on your soul. Please, don't judge me, I'm not proud of my old self either.
 */

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

/**
 * Displays available events and provides filters to search and sort them.
 */
public class AvailableEvents extends VBox {
    private final PassPort app;
    private final Components ui;
    private final ObservableList<Event> events;

    /**
     * Holds UI components for the AvailableEvents view.
     */
    class Components {
        final Label eventsTitle = new Label();
        final TextField searchBar = new TextField();
        final ChoiceBox<String> categoryFilter = new ChoiceBox<>();
        final DatePicker startDatePicker = new DatePicker();
        final DatePicker endDatePicker = new DatePicker();
        final ChoiceBox<String> sortSelector = new ChoiceBox<>();
        final Button filterButton = new Button();

        public Components() { this.eventsTitle.getStyleClass().add("title-1"); }
    }

    /**
     * @param app The PassPort application instance.
     */
    public AvailableEvents(PassPort app) {
        this.app = app;
        this.ui = new Components();
        this.events = FXCollections.observableArrayList();

        this.setupLayout();
        this.translate();
        this.updateEvents(this.loadList());
    }

    /**
     * Opens the event purchase view for the given event.
     *
     * @param event The event to be purchased.
     */
    private void openEvent(Event event) { app.toEventPurchase(event); }

    /**
     * Loads the list of available events starting today.
     *
     * @return The list of available events.
     */
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

    /**
     * Creates the search Bar section
     *
     * @return A VBox containing the search bar.
     */
    private VBox searchBox() {
        ui.searchBar
                .setPromptText(app.translator().translationOf("prompt.search"));
        ui.searchBar.setAccessibleText(
                app.translator().translationOf("prompt.search"));
        ui.searchBar.setMinWidth(500);
        ui.searchBar.setPrefWidth(850);
        var searchBox = new VBox(ui.searchBar);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        return searchBox;
    }

    /**
     * Creates the filters sections for the events.
     *
     * @return An HBox containing the filters.
     */
    private HBox filtersBox() {
        var filtersBox = new HBox(10,
                ui.categoryFilter,
                ui.startDatePicker,
                ui.endDatePicker,
                ui.sortSelector,
                ui.filterButton);

        ui.categoryFilter.setPrefWidth(150);
        ui.startDatePicker.setPrefWidth(150);
        ui.endDatePicker.setPrefWidth(150);
        ui.sortSelector.setPrefWidth(150);
        ui.filterButton.setPrefWidth(100);

        ui.filterButton.setOnAction(e -> applyFilters());

        filtersBox.setAlignment(Pos.CENTER);
        filtersBox.setPadding(new Insets(10));
        filtersBox.setPrefWidth(850);

        return filtersBox;
    }

    /**
     * Applies the selected filters to the events list. Observation: I wouldn't
     * touch this if I was you... If it works, it works. I hope this works by
     * the way, because ohhhh God, that is unreadable.
     */
    private void applyFilters() {
        var listing = app.services()
                .eventsListing()
                .beingToday(LocalDate.now());

        if (!ui.searchBar.getText().isEmpty()) {
            listing = listing.including(ui.searchBar.getText());
        }

        if (ui.categoryFilter.getValue() != null
                && !ui.categoryFilter.getValue().equals(
                        app.translator().translationOf("category.all"))) {
            EventCategory selectedCategory = Stream.of(EventCategory.values())
                    .filter(category -> app.translator()
                            .translationOf(
                                    "category." + category.name().toLowerCase())
                            .equals(ui.categoryFilter.getValue()))
                    .findFirst()
                    .orElse(null);
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
                    .equals(app.translator().translationOf("sort.title"))) {
                listing = listing.sortedByTitle();
            }
            else if (selectedSort
                    .equals(app.translator().translationOf("sort.date"))) {
                listing = listing.sortedByDate();
            }
        }

        updateEvents(listing.availables());
    }

    /**
     * Creates the events scroll pane section.
     *
     * @return A VBox containing the events scroll pane.
     */
    private VBox eventsBox() {
        var eventsContainer = eventsContainer();

        var box = new VBox(eventsScroll(eventsContainer));
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(20);

        return box;
    }

    /**
     * Creates the events section.
     *
     * @return A VBox container for the events.
     */
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

    /**
     * Creates a ScrollPane for the events container.
     *
     * @param eventsContainer The VBox containing the events.
     * @return A ScrollPane for the events container.
     */
    private ScrollPane eventsScroll(VBox eventsContainer) {
        var scrollPane = new ScrollPane(eventsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFocusTraversable(true);

        return scrollPane;
    }

    /**
     * Updates the events list with new events.
     *
     * @param newEvents The new events to be displayed.
     */
    public void updateEvents(List<Event> newEvents) {
        events.setAll(newEvents);
    }

    /**
     * Translates the UI components' text using the application translator.
     * Another "What the heck was I doing it when I wrote this???", now, only
     * God knows about how this piece of code works.
     */
    private void translate() {
        // Setup Category Filter
        ui.categoryFilter.getItems().clear();
        ui.categoryFilter
                .getItems()
                .add(
                        app.translator()
                                .translationOf("category.all"));

        Stream.of(EventCategory.values())
                .map(category -> app.translator().translationOf(
                        "category." + category.name().toLowerCase()))
                .forEach(ui.categoryFilter.getItems()::add);

        // Setup sort selector
        ui.sortSelector.setItems(FXCollections.observableArrayList(
                app.translator().translationOf("sort.title"),
                app.translator().translationOf("sort.date")));

        app.translator()
                .translateFrom(ui.eventsTitle::setText, "main.events.title")
                .translateFrom(ui.eventsTitle::setAccessibleText,
                        "main.events.title")
                .translateFrom(ui.searchBar::setPromptText, "prompt.search")
                .translateFrom(ui.searchBar::setAccessibleText, "prompt.search")
                .translateFrom(ui.filterButton::setText, "button.filter")
                .translateFrom(ui.filterButton::setAccessibleText, "button.filter")
                .translateFrom(ui.categoryFilter::setValue, "category.all")
                .translateFrom(ui.categoryFilter::setAccessibleText, "category.all")
                .translateFrom(ui.startDatePicker::setPromptText, "prompt.start_date")
                .translateFrom(ui.startDatePicker::setAccessibleText, "prompt.start_date")
                .translateFrom(ui.endDatePicker::setPromptText, "prompt.end_date")
                .translateFrom(ui.endDatePicker::setAccessibleText, "prompt.end_date")
                .translateFrom(ui.sortSelector::setValue, "sort.date")
                .translateFrom(ui.sortSelector::setAccessibleText, "sort.date")
                .resourcesProp().addListener((_, _, _) -> this.translate());
    }
}
