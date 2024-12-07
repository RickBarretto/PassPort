package passport.application.desktop.ui.main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.ui.components.LanguageSelector;
import passport.application.desktop.ui.main.events.AvailableEvents;
import passport.application.desktop.ui.main.owned.MyEvents;

/**
 * Represents the main window of the application, providing access to available
 * events and owned events.
 */
public class MainWindow extends HBox {
    private final PassPort app;
    private final Components ui;

    /**
     * Holds UI components for the main window.
     */
    class Components {
        final Drawer drawer;
        final LanguageSelector language;

        /**
         * @param openAllEvents Action to open the available events view.
         * @param openMyEvents  Action to open the owned events view.
         */
        public Components(Action openAllEvents, Action openMyEvents) {
            this.drawer = new Drawer(app, openAllEvents, openMyEvents);
            this.language = new LanguageSelector(app);
        }
    }

    /**
     * Constructs the main window with the given application instance.
     *
     * @param app The PassPort application instance.
     */
    public MainWindow(PassPort app) {
        this.app = app;
        this.ui = new Components(this::openAllEvents, this::openMyEvents);
        setup();
    }

    /**
     * Sets up the main window layout and components.
     */
    private void setup() {
        VBox main = main(new AvailableEvents(app));
        this.getChildren().addAll(
                ui.drawer,
                main);

        HBox.setHgrow(ui.drawer, Priority.NEVER);
        HBox.setHgrow(main, Priority.ALWAYS);
    }

    /**
     * Creates the main content area with the language selector.
     *
     * @param content The main content to be displayed.
     * @return A VBox containing the language selector and main content.
     */
    private VBox main(VBox content) {
        VBox.setVgrow(content, Priority.ALWAYS);
        return new VBox(ui.language, content);
    }

    /**
     * Opens the available events view.
     */
    private void openAllEvents() {
        VBox right = (VBox) this.getChildren().get(1);
        right.getChildren().set(1, new AvailableEvents(app));
    }

    /**
     * Opens the owned events view.
     */
    private void openMyEvents() {
        VBox right = (VBox) this.getChildren().get(1);
        right.getChildren().set(1, new MyEvents(app));
    }

}
