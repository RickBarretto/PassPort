package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Hero extends StackPane {
    public Hero() { setupUI(); }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    void setupUI() {
        setupCss();
        var appName = appName();
        addChildren(appName);
    }

    private void addChildren(Label appName) { getChildren().add(appName); }

    private void setupCss() {
        setAlignment(Pos.CENTER);
        getStyleClass().add("hero");
    }

    private Label appName() {
        Label appName = new Label("PassPort");
        appName.getStyleClass().add("app-name");
        return appName;
    }
}