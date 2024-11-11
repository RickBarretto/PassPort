package passport.application.desktop.ui.welcome;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import passport.application.desktop.Translator;
import passport.domain.contexts.user.UserRegistering;
import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import java.util.regex.Pattern;

public class LogonPane extends VBox {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+" // username
            + "@" // at (@)
            + "(.+)$"; // anything
    private static final String CPF_PATTERN = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
    private static final int MIN_PASSWORD_LENGTH = 8;

    // Step tracking
    private int currentStep = 1;
    private final int TOTAL_STEPS = 2;

    // UI Components
    private final Label title;
    private final TextField fullName;
    private final TextField cpf;
    private final TextField email;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button nextButton;
    private final Button backButton;
    private final Button signupButton;
    private final Button switchToLogin;
    private final VBox step1Content;
    private final VBox step2Content;
    private final ProgressBar progressBar;
    private final Label progressLabel;
    private final WelcomeWindow parent;
    private final UserRegistering userRegistering;

    public LogonPane(WelcomeWindow parent, UserRegistering userRegistering) {
        this.parent = parent;
        this.userRegistering = userRegistering;

        // Initialize components
        title = new Label();
        fullName = new TextField();
        cpf = new TextField();
        email = new TextField();
        password = new PasswordField();
        confirmPassword = new PasswordField();
        nextButton = new Button();
        backButton = new Button();
        signupButton = new Button();
        switchToLogin = new Button();
        progressBar = new ProgressBar();
        progressLabel = new Label();

        step1Content = new VBox(15);
        step2Content = new VBox(15);

        var translator = Translator.instance();
        translator.resourcesProp().addListener((_, _, _) -> translate());

        setupUI();
        setupActions();
        translate();
        updateProgress();
    }

    private void setupUI() {
        title.getStyleClass().add("form-title");
        nextButton.getStyleClass().add("primary-button");
        nextButton.getStyleClass().add("next-button");
        backButton.getStyleClass().add("secondary-button");
        signupButton.getStyleClass().add("primary-button");
        switchToLogin.getStyleClass().add("secondary-button");

        progressBar.setMinWidth(250);
        progressBar.setMaxWidth(250);
        progressBar.setMinHeight(20);
        progressBar.setStyle(
                "-fx-accent: #2196F3; -fx-background-radius: 5px; -fx-border-radius: 5px;");

        HBox nextButtonContainer = new HBox(nextButton);
        nextButtonContainer.setAlignment(Pos.CENTER);

        HBox signupBackButtonContainer = new HBox(10, signupButton, backButton);
        signupBackButtonContainer.setAlignment(Pos.CENTER);

        step1Content.getChildren().addAll(
                email,
                password,
                confirmPassword,
                nextButtonContainer);
        step1Content.setAlignment(Pos.CENTER);

        step2Content.getChildren().addAll(
                fullName,
                cpf,
                signupBackButtonContainer);
        step2Content.setAlignment(Pos.CENTER);
        step2Content.setVisible(false);

        // Progress container with matching width
        VBox progressContainer = new VBox(5);
        progressContainer.setAlignment(Pos.CENTER);
        progressContainer.setMaxWidth(100);
        progressContainer.getChildren().addAll(progressLabel, progressBar);

        setSpacing(15);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
        getStyleClass().add("form-container");

        getChildren().addAll(
                title,
                step1Content,
                step2Content,
                new Separator(),
                progressContainer,
                new Separator(),
                switchToLogin);
    }

    private void setupActions() {
        switchToLogin.setOnAction(_ -> parent.switchToLogin());
        nextButton.setOnAction(_ -> handleNextStep());
        backButton.setOnAction(_ -> handleBackStep());
        signupButton.setOnAction(_ -> handleRegistration());
    }

    private void handleNextStep() {
        if (validateStep1()) {
            currentStep = 2;
            step1Content.setVisible(false);
            step2Content.setVisible(true);
            updateProgress();
        }
    }

    private void handleBackStep() {
        currentStep = 1;
        step1Content.setVisible(true);
        step2Content.setVisible(false);
        updateProgress();
    }

    private void updateProgress() {
        double progress = (double) currentStep / TOTAL_STEPS;
        progressBar.setProgress(progress);
        progressLabel.setText(String.format(
                Translator.instance().translationOf("progress.step"),
                currentStep, TOTAL_STEPS));
    }

    private boolean validateStep1() {
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

    private boolean validateStep2() {
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

        return true;
    }

    private void handleRegistration() {
        if (!validateStep2()) {
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
        return new Login(email.getText(), password.getText());
    }

    private Person createPerson() {
        return new Person(fullName.getText(), cpf.getText());
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
                .translateFrom(nextButton::setText, "logon.next")
                .translateFrom(backButton::setText, "logon.back")
                .translateFrom(signupButton::setText, "logon.button")
                .translateFrom(switchToLogin::setText, "logon.switch");
    }
}