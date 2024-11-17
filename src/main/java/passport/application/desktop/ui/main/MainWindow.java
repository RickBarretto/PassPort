package passport.application.desktop.ui.main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.ui.components.LanguageSelector;
import passport.application.desktop.ui.main.events.AvailableEvents;
import passport.application.desktop.ui.main.owned.MyEvents;

public class MainWindow extends HBox {
    private final PassPort app;
    private final Components ui;

    class Components {
        final Drawer drawer;
        final LanguageSelector language;

        public Components(Action openAllEvents, Action openMyEvents) {
            this.drawer = new Drawer(app, openAllEvents, openMyEvents);
            this.language = new LanguageSelector(app);
        }
    }

    public MainWindow(PassPort app) {
        this.app = app;
        this.ui = new Components(this::openAllEvents, this::openMyEvents);
        setup();
    }

    private void setup() {
        VBox main = main(new AvailableEvents(app));
        this.getChildren().addAll(
                ui.drawer,
                main);

        HBox.setHgrow(ui.drawer, Priority.NEVER);
        HBox.setHgrow(main, Priority.ALWAYS);
    }

    private VBox main(VBox content) {
        VBox.setVgrow(content, Priority.ALWAYS);
        return new VBox(ui.language, content);
    }

    private void openAllEvents() {
        VBox right = (VBox) this.getChildren().get(1);
        right.getChildren().set(1, new AvailableEvents(app));
    }

    private void openMyEvents() {
        VBox right = (VBox) this.getChildren().get(1);
        right.getChildren().set(1, new MyEvents(app));
    }

}
