package passport.application.desktop.welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;

public class Logon extends VBox {
    private final Label title;
    private final TextField username;
    private final TextField email;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button signupButton;
    private final Button switchToLogin;

    private final WelcomeWindow parent;

    public Logon(WelcomeWindow parent) {
        this.parent = parent;

        title = new Label();
        username = new TextField();
        email = new TextField();
        password = new PasswordField();
        confirmPassword = new PasswordField();
        signupButton = new Button();
        switchToLogin = new Button();

        var translator = Translator.instance();
        translator.resourcesProp().addListener((_, _, _) -> translate());

        setupUI();
        translate();
    }

    private void setupUI() {
        username.setPromptText("Username");
        email.setPromptText("Email");
        password.setPromptText("Password");
        confirmPassword.setPromptText("Confirm Password");

        title.getStyleClass().add("form-title");
        signupButton.getStyleClass().add("primary-button");
        switchToLogin.getStyleClass().add("secondary-button");

        switchToLogin.setOnAction(_ -> parent.switchToLogin());

        setSpacing(15);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
        getStyleClass().add("form-container");

        getChildren().addAll(
                title,
                username,
                email,
                password,
                confirmPassword,
                signupButton,
                new Separator(),
                switchToLogin);
    }

    private void translate() {
        // @formatter:off
        Translator.instance()
            .translateFrom(title::setText, "login.title")
            .translateFrom(username::setPromptText, "logon.username")
            .translateFrom(email::setPromptText, "logon.email")
            .translateFrom(password::setPromptText, "logon.password")
            .translateFrom(confirmPassword::setPromptText, "logon.confirmPassword")
            .translateFrom(signupButton::setText, "logon.button")
            .translateFrom(switchToLogin::setText, "logon.switch");
        // @formatter:on
    }

}