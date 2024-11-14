package passport.application.desktop.ui.welcome.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.Action;
import passport.application.desktop.PassPort;
import passport.domain.models.users.Login;

public class LoginForm extends VBox {
    private final Components ui;
    private final PassPort app;

    class Components {
        public final Label title = new Label();
        public final TextField email = new TextField();
        public final PasswordField password = new PasswordField();
        public final Button loginButton = new Button();
        public final Button switchToLogon = new Button();
    }

    public LoginForm(PassPort app, Action toSignupPane) {
        this.app = app;
        this.ui = new Components();

        setupUI();
        setupActions(toSignupPane);
        translate();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions(Action toSignupPane) {
        ui.switchToLogon.setOnAction(_ -> toSignupPane.exec());
        ui.loginButton.setOnAction(_ -> this.loginWithForms());
    }

    private void loginWithForms() {
        try {
            app.services().login().logAs(new Login(
                    ui.email.getText(),
                    ui.password.getText()));
        }
        catch (Exception e) {
        }

        if (app.services().login().isLoggedAs(ui.email.getText())) {
            app.toMain();
        }
        else {
            clearFields();
            app.warn().error("login.invalid");
        }
    }

    private void clearFields() {
        ui.email.setText("");
        ui.password.setText("");
    }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    private void setupUI() {
        setupCssClasses();
        setupAlignment();
        applyChildren();
    }

    private void applyChildren() {
        this.getChildren().addAll(
                ui.title,
                ui.email,
                ui.password,
                ui.loginButton,
                new Separator(),
                ui.switchToLogon);
    }

    private void setupCssClasses() {
        ui.title.getStyleClass().add("title-1");
        ui.loginButton.getStyleClass().add("primary-button");
        ui.switchToLogon.getStyleClass().add("secondary-button");
        this.getStyleClass().add("form-container");
    }

    private void setupAlignment() {
        this.setSpacing(15);
        this.setPadding(new Insets(50));
        this.setAlignment(Pos.CENTER);
    }

    // =~=~=~=~= =~=~=~=~= SETUP TRANSLATION =~=~=~=~= =~=~=~=~=

    private void translate() {
        // @formatter:off
        app.translator()
            .translateFrom(ui.title::setText, "login.title")
            .translateFrom(ui.email::setPromptText, "login.email")
            .translateFrom(ui.password::setPromptText, "login.password")
            .translateFrom(ui.loginButton::setText, "login.button")
            .translateFrom(ui.switchToLogon::setText, "login.switch")
            .resourcesProp().addListener((_, _, _) -> translate());
        // @formatter:on
    }

}