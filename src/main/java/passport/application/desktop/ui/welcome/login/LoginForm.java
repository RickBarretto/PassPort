package passport.application.desktop.ui.welcome.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.App;
import passport.application.desktop.Translator;
import passport.application.desktop.ui.welcome.WelcomeWindow;
import passport.domain.contexts.user.UserLogin;
import passport.domain.models.users.Login;

public class LoginForm extends VBox {
    private final Components ui;
    private final App application;
    private final WelcomeWindow parent;
    private final UserLogin login;

    class Components {
        public final Label title = new Label();
        public final TextField email = new TextField();
        public final PasswordField password = new PasswordField();
        public final Button loginButton = new Button();
        public final Button switchToLogon = new Button();
    }

    public LoginForm(App application, WelcomeWindow parent, UserLogin login) {
        this.ui = new Components();
        this.application = application;
        this.parent = parent;
        this.login = login;

        setupUI();
        setupActions();
        setupTranslation();
        translate();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions() {
        ui.switchToLogon.setOnAction(_ -> parent.switchToLogon());
        ui.loginButton.setOnAction(_ -> this.loginWithForms());
    }

    private void loginWithForms() {
        try {
            login.logAs(new Login(
                    ui.email.getText(),
                    ui.password.getText()));
        }
        catch (Exception e) {
        }

        if (login.isLoggedAs(ui.email.getText())) {
            application.openMainWindow();
        }
        else {
            clearFields();
            showError("login.invalid");
        }
    }

    private void clearFields() {
        ui.email.setText("");
        ui.password.setText("");
    }

    protected void showError(String messageKey) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(Translator.instance().translationOf(messageKey));
        alert.show();
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
        ui.title.getStyleClass().add("form-title");
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

    private void setupTranslation() {
        Translator translator = Translator.instance();
        translator.resourcesProp()
                .addListener((_, _, _) -> translate());
    }

    private void translate() {
        // @formatter:off
        Translator.instance()
            .translateFrom(ui.title::setText, "login.title")
            .translateFrom(ui.email::setPromptText, "login.email")
            .translateFrom(ui.password::setPromptText, "login.password")
            .translateFrom(ui.loginButton::setText, "login.button")
            .translateFrom(ui.switchToLogon::setText, "login.switch");
        // @formatter:on
    }

}