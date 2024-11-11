package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Hero extends StackPane {
    public Hero() { setupUI(); }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    void setupUI() {
        Label appName = new Label("PassPort");
        
        appName.getStyleClass().add("app-name");

        setAlignment(Pos.CENTER);
        getStyleClass().add("hero");
        getChildren().add(appName);
    }
}