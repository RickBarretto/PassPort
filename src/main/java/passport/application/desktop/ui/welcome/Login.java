package passport.application.desktop.ui.welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;

public class Login extends VBox {
    private final Label title;
    private final TextField username;
    private final PasswordField password;
    private final Button loginButton;
    private final Button switchToLogon;

    private final WelcomeWindow parent;

    public Login(WelcomeWindow parent) {
        this.parent = parent;

        title = new Label();
        username = new TextField();
        password = new PasswordField();
        loginButton = new Button();
        switchToLogon = new Button();

        Translator translator = Translator.instance();
        translator.resourcesProp()
                .addListener((_, _, _) -> translate());

        setupUI();
        translate();
    }

    private void setupUI() {
        title.getStyleClass().add("form-title");
        loginButton.getStyleClass().add("primary-button");
        switchToLogon.getStyleClass().add("secondary-button");

        switchToLogon.setOnAction(_ -> parent.switchToLogon());

        setSpacing(15);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
        getStyleClass().add("form-container");

        getChildren().addAll(title, username, password, loginButton, new Separator(), switchToLogon);
    }

    private void translate() {
        // @formatter:off
        Translator.instance()
            .translateFrom(title::setText, "login.title")
            .translateFrom(username::setPromptText, "login.username")
            .translateFrom(password::setPromptText, "login.password")
            .translateFrom(loginButton::setText, "login.button")
            .translateFrom(switchToLogon::setText, "login.switch");
        // @formatter:on
    }
}