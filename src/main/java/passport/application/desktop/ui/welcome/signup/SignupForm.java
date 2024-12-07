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

/**
 * Manages the signup form in the welcome window. Uses {@link CredentialsStep},
 * {@link PersonalInfoStep} for a multi-step signup process managed by
 * {@link SignupStepPane}.
 */
public class SignupForm extends VBox {
    private final PassPort app;
    private final Components ui;
    private final SignupStepPane stepManager;

    /**
     * Holds UI components for the signup form.
     */
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

    /**
     * @param app     The PassPort application instance.
     * @param toLogin Action to switch to the login form.
     */
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

    // Setup actions
    private void setupActions(Action toLogin) {
        ui.goToLogin.setOnAction(_ -> toLogin.exec());
    }

    // Setup user interface
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
                ui.goToLogin);

    }

    // Setup translations
    private void translate() {
        app.translator()
                .translateFrom(ui.title::setText, "login.title")
                .translateFrom(ui.title::setAccessibleText, "login.title")
                .translateFrom(ui.goToLogin::setText, "logon.switch")
                .translateFrom(ui.goToLogin::setAccessibleText, "logon.switch")
                .resourcesProp().addListener((_, _, _) -> translate());
    }
}
