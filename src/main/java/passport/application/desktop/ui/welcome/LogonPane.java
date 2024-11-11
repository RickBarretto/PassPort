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
        "^[A-Za-z0-9+_.-]+" // username
        + "@"               // at (@)
        + "(.+)$";          // anything
    private static final String CPF_PATTERN = 
            "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final Label title;
    private final TextField fullName;
    private final TextField cpf;
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
        fullName = new TextField();
        cpf = new TextField();
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
                fullName,
                cpf,
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
        return new Person(fullName.getText(), cpf.getText());
    }

    private boolean validateInputs() {
        // Full Name validation
        if (fullName.getText().trim().isEmpty()) {
            showError("validation.fullname.required");
            return false;
        }

        // CPF validation
        String cpfText = cpf.getText().trim();
        if (cpfText.isEmpty()) {
            showError("validation.cpf.required");
            return false;
        }
        if (!Pattern.compile(CPF_PATTERN).matcher(cpfText).matches()) {
            showError("validation.cpf.invalid.format");
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
                .translateFrom(fullName::setPromptText, "logon.fullname")
                .translateFrom(cpf::setPromptText, "logon.cpf")
                .translateFrom(email::setPromptText, "logon.email")
                .translateFrom(password::setPromptText, "logon.password")
                .translateFrom(confirmPassword::setPromptText,
                        "logon.confirmPassword")
                .translateFrom(signupButton::setText, "logon.button")
                .translateFrom(switchToLogin::setText, "logon.switch");
    }
}