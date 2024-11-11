package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import passport.application.desktop.Translator;
import java.util.regex.Pattern;

public class CredentialsStep extends SignupStep {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final TextField email;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    private final Button nextButton;

    public CredentialsStep(SignupForm form) {
        super(form);

        email = new TextField();
        password = new PasswordField();
        confirmPassword = new PasswordField();
        nextButton = new Button();

        setupUI();
        setupActions();
        translate();
    }

    private void setupUI() {
        nextButton.getStyleClass().addAll("primary-button", "next-button");

        HBox buttonContainer = new HBox(nextButton);
        buttonContainer.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
        getChildren().addAll(
                email,
                password,
                confirmPassword,
                buttonContainer);
    }

    private void setupActions() {
        nextButton.setOnAction(_ -> {
            if (validate()) {
                ((SignupStepManager) getParent()).next();
            }
        });
    }

    @Override
    protected boolean validate() {
        String emailText = email.getText().trim();
        if (emailText.isEmpty()) {
            showError("validation.email.required");
            return false;
        }
        if (!Pattern.compile(EMAIL_PATTERN).matcher(emailText).matches()) {
            showError("validation.email.invalid");
            return false;
        }

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

    private void translate() {
        Translator translator = Translator.instance();
        translator.translateFrom(email::setPromptText, "logon.email");
        translator.translateFrom(password::setPromptText, "logon.password");
        translator.translateFrom(confirmPassword::setPromptText,
                "logon.confirmPassword");
        translator.translateFrom(nextButton::setText, "logon.next");
    }

    public String email() { return email.getText(); }

    public String getPassword() { return password.getText(); }
}