package passport.application.desktop.ui.main;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import passport.application.desktop.Infra;

public class MainWindow extends VBox {
    final Infra infra;
    
    public MainWindow(Infra infra) {
        this.infra = infra;
        setupUI();
    }
    
    private void setupUI() {
        Label welcomeLabel = new Label("Just a test");

        getChildren().add(welcomeLabel);
        getStyleClass().add("main-window");
    }
}