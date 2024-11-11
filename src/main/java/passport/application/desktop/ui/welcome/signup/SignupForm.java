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
    private final SignupStepPane stepManager;
    private final Components ui;
    
    class Components {
        public final Label title = new Label();
        public final Button switchToLogin = new Button();
        public final ProgressIndicator progressBar = new ProgressIndicator();
    }

    public SignupForm(WelcomeWindow parent, SigningUp context) {
        this.parent = parent;
        this.context = context;

        this.ui = new Components();
        this.stepManager = new SignupStepPane(
                new CredentialsStep(this),
                new PersonalInfoStep(this)
        );

        setupUI();
        setupActions();
        translate();
    }

    public SigningUp registering() { return context; }

    public WelcomeWindow parent() { return parent; }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions() {
        ui.switchToLogin.setOnAction(_ -> parent.switchToLogin());
    }
    
    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    private void setupUI() {
        setupCSS();
        setupAlignment();
        applyChildren();
    }

    private void applyChildren() {
        getChildren().addAll(
                ui.title,
                stepManager,
                new Separator(),
                ui.progressBar,
                new Separator(),
                ui.switchToLogin);
    }

    private void setupAlignment() {
        setSpacing(15);
        setPadding(new Insets(50));
        setAlignment(Pos.CENTER);
    }

    private void setupCSS() {
        ui.title.getStyleClass().add("form-title");
        ui.switchToLogin.getStyleClass().add("secondary-button");
        getStyleClass().add("form-container");
    }

    // =~=~=~=~= =~=~=~=~= SETUP TRANSLATIONS =~=~=~=~= =~=~=~=~=

    private void translate() {
        Translator.instance()
            .translateFrom(ui.title::setText, "login.title")
            .translateFrom(ui.switchToLogin::setText, "logon.switch")
            .resourcesProp().addListener((_, _, _) -> translate());
    }
}
