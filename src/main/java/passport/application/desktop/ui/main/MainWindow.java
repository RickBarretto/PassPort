package passport.application.desktop.ui.main;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.ui.components.LanguageSelector;
import passport.application.desktop.ui.main.events.AvailableEvents;

public class MainWindow extends HBox {
    private final PassPort app;
    private final Components ui;

    class Components {
        public final Drawer drawer;
        public final AvailableEvents content;
        final LanguageSelector language;

        public Components() {
            this.drawer = new Drawer(app);
            this.content = new AvailableEvents(app);
            this.language = new LanguageSelector(app);
        }
    }

    public MainWindow(PassPort app) {
        this.app = app;
        this.ui = new Components();
        setup();
    }

    private void setup() {
        var right = rightPane(ui.content);
        this.getChildren().addAll(
                ui.drawer,
                right);

        HBox.setHgrow(ui.drawer, Priority.NEVER);
        HBox.setHgrow(right, Priority.ALWAYS);
    }

    private VBox rightPane(VBox mainContent) {
        VBox rightPane = new VBox();
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        rightPane.getChildren().addAll(ui.language, mainContent);

        return rightPane;
    }

}
