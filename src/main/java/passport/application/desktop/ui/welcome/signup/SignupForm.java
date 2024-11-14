package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import passport.application.desktop.Action;
import passport.application.desktop.PassPort;
import passport.application.desktop.ui.components.ProgressIndicator;

public class SignupForm extends VBox {
    private final PassPort app;
    private final Components ui;
    private final SignupStepPane stepManager;
    
    class Components {
        public final Label title = new Label();
        public final Button switchToLogin = new Button();
        public final ProgressIndicator progressBar;

        public Components(PassPort app) {
            this.progressBar = new ProgressIndicator(app.translator());
        }
    }

    public SignupForm(PassPort app, Action toLogin) {
        this.app = app;
        this.ui = new Components(app);
        this.stepManager = new SignupStepPane(
                new CredentialsStep(app),
                new PersonalInfoStep(app, toLogin)
        );

        setupUI();
        setupActions(toLogin);
        translate();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions(Action toLogin) {
        ui.switchToLogin.setOnAction(_ -> toLogin.exec());
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
        ui.title.getStyleClass().add("title-1");
        ui.switchToLogin.getStyleClass().add("secondary-button");
        getStyleClass().add("form-container");
    }

    // =~=~=~=~= =~=~=~=~= SETUP TRANSLATIONS =~=~=~=~= =~=~=~=~=

    private void translate() {
        app.translator()
            .translateFrom(ui.title::setText, "login.title")
            .translateFrom(ui.switchToLogin::setText, "logon.switch")
            .resourcesProp().addListener((_, _, _) -> translate());
    }
}
