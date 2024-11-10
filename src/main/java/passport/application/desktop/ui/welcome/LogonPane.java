package passport.application.desktop.ui.welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;
import passport.domain.contexts.user.UserRegistering;
import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import java.util.regex.Pattern;

public class LogonPane extends VBox {
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final int MIN_PASSWORD_LENGTH = 8;
    
    private final Label title;
    private final TextField username;
    private final TextField email;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button signupButton;
    private final Button switchToLogin;
    private final WelcomeWindow parent;
    private final UserRegistering userRegistering;

    public LogonPane(WelcomeWindow parent, UserRegistering userRegistering) {
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

            showSuccess("validation.registration.success");
            parent.switchToLogin();
        }
        catch (EmailAlreadyExists e) {
            showError("validation.email.exists");
        }
        catch (Exception e) {
            showError("validation.registration.failed");
        }
    }

    private Login createLogin() {
        return new Login(
                email.getText(),
                password.getText());
    }

    private Person createPerson() { 
        return new Person(username.getText(), ""); 
    }

    private boolean validateInputs() {
        // Username validation
        if (username.getText().trim().isEmpty()) {
            showError("validation.username.required");
            return false;
        }

        // Email validation
        String emailText = email.getText().trim();
        if (emailText.isEmpty()) {
            showError("validation.email.required");
            return false;
        }
        if (!Pattern.compile(EMAIL_PATTERN).matcher(emailText).matches()) {
            showError("validation.email.invalid");
            return false;
        }

        // Password validation
        String passwordText = password.getText();
        if (passwordText.length() < MIN_PASSWORD_LENGTH) {
            showError("validation.password.length");
            return false;
        }

        if (!passwordText.equals(confirmPassword.getText())) {
            showError("validation.password.mismatch");
            return false;
        }

        return true;
    }

    private void showError(String messageKey) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(Translator.instance().translationOf(messageKey));
        alert.show();
    }

    private void showSuccess(String messageKey) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(Translator.instance().translationOf(messageKey));
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