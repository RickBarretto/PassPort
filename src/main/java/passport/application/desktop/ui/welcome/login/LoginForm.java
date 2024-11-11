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
    private final Label title;
    private final TextField email;
    private final PasswordField password;
    private final Button loginButton;
    private final Button switchToLogon;

    private final App application;
    private final WelcomeWindow parent;
    private final UserLogin login;

    public LoginForm(App application, WelcomeWindow parent, UserLogin login) {
        this.application = application;
        this.parent = parent;
        this.login = login;

        title = new Label();
        email = new TextField();
        password = new PasswordField();
        loginButton = new Button();
        switchToLogon = new Button();

        setupUI();
        setupActions();
        setupTranslation();
        translate();
    }

    private void setupTranslation() {
        Translator translator = Translator.instance();
        translator.resourcesProp()
                .addListener((_, _, _) -> translate());
    }

    private void setupUI() {
        title.getStyleClass().add("form-title");
        loginButton.getStyleClass().add("primary-button");
        switchToLogon.getStyleClass().add("secondary-button");

        setSpacing(15);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
        getStyleClass().add("form-container");

        getChildren().addAll(title, email, password, loginButton,
                new Separator(), switchToLogon);
    }

    private void setupActions() {
        switchToLogon.setOnAction(_ -> parent.switchToLogon());
        loginButton.setOnAction(_ -> this.loginWithForms());
    }

    private void loginWithForms() {
        try {
            login.logAs(new Login(email.getText(), password.getText()));
        }
        catch (Exception e) {
        }

        if (login.isLoggedAs(email.getText())) {
            application.openMainWindow();
        }
        else {
            clearFields();
            showError("login.invalid");
        }
    }

    private void clearFields() {
        this.email.setText("");
        this.password.setText("");
    }

    protected void showError(String messageKey) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(Translator.instance().translationOf(messageKey));
        alert.show();
    }

    private void translate() {
        // @formatter:off
        Translator.instance()
            .translateFrom(title::setText, "login.title")
            .translateFrom(email::setPromptText, "login.email")
            .translateFrom(password::setPromptText, "login.password")
            .translateFrom(loginButton::setText, "login.button")
            .translateFrom(switchToLogon::setText, "login.switch");
        // @formatter:on
    }
}