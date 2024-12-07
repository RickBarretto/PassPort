package passport.application.desktop.ui.welcome.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import passport.domain.models.users.Login;

/**
 * Handles the login form in the welcome window.
 */
public class LoginForm extends VBox {
    private final Components ui;
    private final PassPort app;

    /**
     * Holds UI components for the login form.
     */
    class Components {
        public final Label title = new Label();
        public final TextField email = new TextField();
        public final PasswordField password = new PasswordField();
        public final Button loginButton = new Button();
        public final Button switchToLogon = new Button();

        public Components() {
            title.getStyleClass().add("title-1");
            loginButton.getStyleClass().addAll(
                    "text",
                    "accent",
                    "primary-button");
            switchToLogon.getStyleClass().add("text");
        }
    }

    /**
     * Constructs the login form.
     *
     * @param app          The PassPort application instance.
     * @param toSignupPane Action to switch to the signup form.
     */
    public LoginForm(PassPort app, Action toSignupPane) {
        this.app = app;
        this.ui = new Components();

        setupUI();
        setupActions(toSignupPane);
        translate();
    }

    /**
     * Sets up actions for the form components.
     *
     * @param toSignupPane Action to switch to the signup form.
     */
    private void setupActions(Action toSignupPane) {
        ui.switchToLogon.setOnAction(_ -> toSignupPane.exec());
        ui.loginButton.setOnAction(_ -> this.loginWithForms());
    }

    /**
     * Handles the login action using the form inputs.
     */
    private void loginWithForms() {
        try {
            app.services().login().logAs(new Login(
                    ui.email.getText(),
                    ui.password.getText()));
        }
        catch (Exception e) {
            // TODO: Future logs may be put here.
        }

        if (app.services().login().isLoggedAs(ui.email.getText())) {
            app.toMain();
        }
        else {
            clearFields();
            app.warn().error("login.invalid");
        }
    }

    /**
     * Clears the email and password fields.
     */
    private void clearFields() {
        ui.email.setText("");
        ui.password.setText("");
    }

    /**
     * Sets up the UI components and layout.
     */
    private void setupUI() {
        setupCssClasses();
        setupAlignment();
        applyChildren();
    }

    /**
     * Adds the UI components to the VBox.
     */
    private void applyChildren() {
        this.getChildren().addAll(
                ui.title,
                ui.email,
                ui.password,
                ui.loginButton,
                new Separator(),
                ui.switchToLogon);
    }

    /**
     * Sets CSS classes for the VBox.
     */
    private void setupCssClasses() {
        this.getStyleClass().add("form-container");
    }

    /**
     * Sets alignment and spacing for the VBox.
     */
    private void setupAlignment() {
        this.setSpacing(15);
        this.setPadding(new Insets(50));
        this.setAlignment(Pos.CENTER);
    }

    /**
     * Translates the UI components' text using the application translator.
     */
    private void translate() {
        app.translator()
                .translateFrom(ui.title::setText, "login.title")
                .translateFrom(ui.title::setAccessibleText, "login.title")
                .translateFrom(ui.email::setPromptText, "login.email")
                .translateFrom(ui.email::setAccessibleText, "login.email")
                .translateFrom(ui.password::setPromptText, "login.password")
                .translateFrom(ui.password::setAccessibleText, "login.password")
                .translateFrom(ui.loginButton::setText, "login.button")
                .translateFrom(ui.loginButton::setAccessibleText,
                        "login.button")
                .translateFrom(ui.switchToLogon::setText, "login.switch")
                .translateFrom(ui.switchToLogon::setAccessibleText,
                        "login.switch")
                .resourcesProp().addListener((_, _, _) -> translate());
    }
}
