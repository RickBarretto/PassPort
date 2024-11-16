package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import passport.application.desktop.system.PassPort;

import java.util.regex.Pattern;

public class CredentialsStep extends SignupStep {
    private final PassPort app;
    private final Components ui;

    static class Validation {
        private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
        private static final int MIN_PASSWORD_LENGTH = 8;
    }

    class Components {
        public final TextField email = new TextField();
        public final PasswordField password = new PasswordField();
        public final PasswordField confirmPassword = new PasswordField();
        public final Button nextButton = new Button();
    }

    public CredentialsStep(PassPort app) {
        super();
        this.app = app;
        this.ui = new Components();

        setupUI();
        setupActions();
        translate();
    }

    public String email() { return ui.email.getText(); }

    public String getPassword() { return ui.password.getText(); }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions() {
        ui.nextButton.setOnAction(_ -> {
            if (validate()) {
                ((SignupStepPane) getParent()).next();
            }
        });
    }

    @Override
    protected boolean validate() {
        String emailText = ui.email.getText().trim();
        if (emailText.isEmpty()) {
            app.warn().error("validation.email.required");
            return false;
        }
        if (!Pattern.compile(
                Validation.EMAIL_PATTERN).matcher(emailText).matches()) {
            app.warn().error("validation.email.invalid");
            return false;
        }

        String passwordText = ui.password.getText();
        if (passwordText.length() < Validation.MIN_PASSWORD_LENGTH) {
            app.warn().error("validation.password.length");
            return false;
        }

        if (!passwordText.equals(ui.confirmPassword.getText())) {
            app.warn().error("validation.password.mismatch");
            return false;
        }

        return true;
    }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    private void setupUI() {
        ui.nextButton.getStyleClass()
                .addAll(
                        "text",
                        "primary-button",
                        "accent");

        HBox buttonContainer = new HBox(ui.nextButton);
        buttonContainer.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
        getChildren().addAll(
                ui.email,
                ui.password,
                ui.confirmPassword,
                buttonContainer);
    }

    // =~=~=~=~= =~=~=~=~= SETUP TRANSLATION =~=~=~=~= =~=~=~=~=

    private void translate() {
        app.translator()
                .translateFrom(ui.email::setPromptText, "logon.email")
                .translateFrom(ui.password::setPromptText, "logon.password")
                .translateFrom(ui.confirmPassword::setPromptText,
                        "logon.confirmPassword")
                .translateFrom(ui.nextButton::setText, "logon.next")
                .resourcesProp().addListener((_, _, _) -> translate());
    }

}