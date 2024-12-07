package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * "Hero" pane of the welcome window.
 */
public class Hero extends StackPane {

    /**
     * Initializes the component's interface.
     */
    public Hero() { setupUI(); }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    /**
     * Sets up the component's interface.
     */
    void setupUI() {
        setupCss();
        var appName = appName();
        addChildren(appName);
    }

    /**
     * Adds the child elements.
     *
     * @param appName The label with the application name to be added.
     */
    private void addChildren(Label appName) { getChildren().add(appName); }

    /**
     * Sets up the CSS style of the component.
     */
    private void setupCss() {
        setAlignment(Pos.CENTER);
        getStyleClass().add("hero");
    }

    /**
     * Returns a Label with the application name.
     *
     * @return A Label configured with the application name.
     */
    private Label appName() {
        Label appName = new Label("PassPort");
        appName.getStyleClass().add("app-name");
        return appName;
    }
}
