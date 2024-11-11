package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import passport.application.desktop.Translator;
import passport.application.desktop.ui.components.ProgressIndicator;
import passport.application.desktop.ui.welcome.WelcomeWindow;
import passport.domain.contexts.user.SigningUp;

public class SignupForm extends VBox {
    private final WelcomeWindow parent;
    private final SigningUp context;
    private final SignupStepManager stepManager;
    private final ProgressIndicator progressBar;
    private final Label title;
    private final Button switchToLogin;

    public SignupForm(WelcomeWindow parent, SigningUp context) {
        this.parent = parent;
        this.context = context;

        this.title = new Label();
        this.switchToLogin = new Button();
        this.progressBar = new ProgressIndicator();
        this.stepManager = new SignupStepManager(
                new CredentialsStep(this),
                new PersonalInfoStep(this));

        setupUI();
        setupActions();
        translate();
    }

    private void setupUI() {
        title.getStyleClass().add("form-title");
        switchToLogin.getStyleClass().add("secondary-button");

        setSpacing(15);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
        getStyleClass().add("form-container");

        getChildren().addAll(
                title,
                stepManager,
                new Separator(),
                progressBar,
                new Separator(),
                switchToLogin);
    }

    private void setupActions() {
        switchToLogin.setOnAction(_ -> parent.switchToLogin());
    }

    private void translate() {
        Translator translator = Translator.instance();
        translator.translateFrom(title::setText, "login.title");
        translator.translateFrom(switchToLogin::setText, "logon.switch");
        translator.resourcesProp().addListener((_, _, _) -> translate());
    }

    public SigningUp registering() { return context; }

    public WelcomeWindow parent() { return parent; }
}
