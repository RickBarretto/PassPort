package passport.application.desktop.ui.welcome.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;
import passport.application.desktop.ui.welcome.WelcomeWindow;

public class LoginForm extends VBox {
    private final Label title;
    private final TextField email;
    private final PasswordField password;
    private final Button loginButton;
    private final Button switchToLogon;

    private final WelcomeWindow parent;

    public LoginForm(WelcomeWindow parent) {
        this.parent = parent;

        title = new Label();
        email = new TextField();
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

        getChildren().addAll(title, email, password, loginButton,
                new Separator(), switchToLogon);
    }

    private void translate() {
        // @formatter:off
        Translator.instance()
            .translateFrom(title::setText, "login.title")
            .translateFrom(email::setPromptText, "login.email")
            .translateFrom(password::setPromptText, "login.password")
            .translateFrom(loginButton::setText, "login.button")
            .translateFrom(switchToLogon::setText, "login.switch");
        // @formatter:on
    }
}