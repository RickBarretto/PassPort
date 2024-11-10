package passport.application.desktop.ui.welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;
import passport.domain.contexts.user.UserRegistering;
import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.models.users.Person;

public class Logon extends VBox {
    private final Label title;
    private final TextField username;
    private final TextField email;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button signupButton;
    private final Button switchToLogin;
    private final WelcomeWindow parent;
    private final UserRegistering userRegistering;

    public Logon(WelcomeWindow parent, UserRegistering userRegistering) {
        this.parent = parent;
        this.userRegistering = userRegistering;

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
        setupActions();
        translate();
    }

    private void setupUI() {
        title.getStyleClass().add("form-title");
        signupButton.getStyleClass().add("primary-button");
        switchToLogin.getStyleClass().add("secondary-button");

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

    private void setupActions() {
        switchToLogin.setOnAction(_ -> parent.switchToLogin());
        signupButton.setOnAction(_ -> handleRegistration());
    }

    private void handleRegistration() {
        if (!validateInputs()) {
            return;
        }

        try {
            userRegistering
                    .login(createLogin())
                    .person(createPerson())
                    .register();

            showSuccess("Registration successful!");
            parent.switchToLogin();
        }
        catch (EmailAlreadyExists e) {
            showError("Email already registered");
        }
        catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
        }
    }

    private passport.domain.models.users.Login createLogin() {
        return new passport.domain.models.users.Login(
                email.getText(),
                password.getText());
    }

    private Person createPerson() { return new Person(username.getText(), ""); }

    private boolean validateInputs() {
        if (username.getText().trim().isEmpty()) {
            showError("Username is required");
            return false;
        }

        if (!password.getText().equals(confirmPassword.getText())) {
            showError("Passwords don't match");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void showSuccess(String message) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }

    private void translate() {
        Translator.instance()
                .translateFrom(title::setText, "login.title")
                .translateFrom(username::setPromptText, "logon.username")
                .translateFrom(email::setPromptText, "logon.email")
                .translateFrom(password::setPromptText, "logon.password")
                .translateFrom(confirmPassword::setPromptText,
                        "logon.confirmPassword")
                .translateFrom(signupButton::setText, "logon.button")
                .translateFrom(switchToLogin::setText, "logon.switch");
    }
}