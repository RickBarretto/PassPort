package passport.application.desktop.ui.main;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainWindow extends VBox {
    
    public MainWindow() {
        setupUI();
    }
    
    private void setupUI() {
        Label welcomeLabel = new Label("Just a test");

        getChildren().add(welcomeLabel);
        getStyleClass().add("main-window");
    }
}