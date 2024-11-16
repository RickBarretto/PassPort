package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.ui.components.ProgressIndicator;

public class SignupForm extends VBox {
    private final PassPort app;
    private final Components ui;
    private final SignupStepPane stepManager;

    class Components {
        public final Label title = new Label();
        public final Button goToLogin = new Button();
        public final ProgressIndicator step;

        public Components(PassPort app) {
            this.step = new ProgressIndicator(app.translator());
            setup();
        }

        private void setup() {
            title.getStyleClass().add("title-1");
            goToLogin.getStyleClass().add("text");
        }
    }

    public SignupForm(PassPort app, Action toLogin) {
        this.app = app;
        this.ui = new Components(app);
        this.stepManager = new SignupStepPane(
                new CredentialsStep(app),
                new PersonalInfoStep(app, toLogin));

        setupUI();
        setupActions(toLogin);
        translate();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions(Action toLogin) {
        ui.goToLogin.setOnAction(_ -> toLogin.exec());
    }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    private void setupUI() {
        this.getStyleClass().add("form-container");

        this.setSpacing(15);
        this.setPadding(new Insets(50));
        this.setAlignment(Pos.CENTER);

        this.getChildren().addAll(
                ui.title,
                this.stepManager,
                new Separator(),
                ui.step,
                new Separator(),
                ui.goToLogin
        );

    }

    // =~=~=~=~= =~=~=~=~= SETUP TRANSLATIONS =~=~=~=~= =~=~=~=~=

    private void translate() {
        app.translator()
                .translateFrom(ui.title::setText, "login.title")
                .translateFrom(ui.goToLogin::setText, "logon.switch")
                .resourcesProp().addListener((_, _, _) -> translate());
    }
}
