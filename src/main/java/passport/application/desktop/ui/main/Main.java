package passport.application.desktop.ui.main;

import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.PassPort;
import passport.application.desktop.ui.components.LanguageSelector;

public class Main extends HBox {
    private final PassPort app;
    private final Components ui;

    class Components {
        public final Drawer drawer = new Drawer();
        public final Content content = new Content();
        final LanguageSelector language;

        public Components(PassPort app) {
            this.language = new LanguageSelector(app);
        }
    }

    public Main(PassPort app) {
        this.app = app;
        this.ui = new Components(app);
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
