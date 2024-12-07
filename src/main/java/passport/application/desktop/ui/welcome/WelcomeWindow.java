package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.ui.components.LanguageSelector;
import passport.application.desktop.ui.welcome.login.LoginForm;
import passport.application.desktop.ui.welcome.signup.SignupForm;

/**
 * Class responsible for setting up the welcome window of the application.
 */
public class WelcomeWindow extends HBox {
    private final Hero hero;
    private final LoginForm login;
    private final SignupForm signUpForm;
    private final LanguageSelector languageSelector;

    /**
     * Initializes the welcome window.
     *
     * @param app The instance of the PassPort application.
     */
    public WelcomeWindow(PassPort app) {
        hero = new Hero();
        login = new LoginForm(app, this::switchToLogon);
        signUpForm = new SignupForm(app, this::switchToLogin);
        languageSelector = new LanguageSelector(app);

        this.setupUI();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    /**
     * Replaces the content of the right pane with the provided node. This
     * allows switching between the Login and SignUp panels.
     *
     * @param node The node to be displayed in the right pane.
     */
    private void rightPaneWith(Node node) {
        var rightPane = (VBox) getChildren().get(1);
        var mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, node);
    }

    /**
     * Switches to the Signup form.
     */
    public void switchToLogon() { this.rightPaneWith(this.signUpForm); }

    /**
     * Switches to the Login form.
     */
    public void switchToLogin() { this.rightPaneWith(this.login); }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    /**
     * Sets up the user interface of the welcome window.
     */
    private void setupUI() {
        var mainContent = mainContent();
        var rightPane = rightPane(mainContent);

        HBox.setHgrow(hero, Priority.ALWAYS);

        getChildren().addAll(hero, rightPane);
    }

    /**
     * Creates the right pane with the language selector and main content.
     *
     * @param mainContent The main content to be displayed.
     * @return A VBox configured as the right pane.
     */
    private VBox rightPane(VBox mainContent) {
        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(languageSelector, mainContent);
        return rightPane;
    }

    /**
     * Creates the main content of the welcome window.
     *
     * @return A VBox configured as the main content.
     */
    private VBox mainContent() {
        VBox mainContent = new VBox();

        VBox.setVgrow(mainContent, Priority.ALWAYS);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().add(login);

        return mainContent;
    }
}
