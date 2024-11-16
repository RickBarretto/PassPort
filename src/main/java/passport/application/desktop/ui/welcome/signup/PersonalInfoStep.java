package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import java.util.regex.Pattern;

public class PersonalInfoStep extends SignupStep {
    private static final String CPF_PATTERN = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
    final PassPort app;
    final Action toLogin;
    final Components ui;

    class Components {
        public final TextField fullName = new TextField();
        public final TextField cpf = new TextField();
        public final Button signupButton = new Button();
        public final Button backButton = new Button();
        public final HBox actions;

        public Components() {
            signupButton.getStyleClass().addAll("text", "accent");
            backButton.getStyleClass().add("text");

            this.actions = new HBox(signupButton, backButton);
            HBox.setHgrow(signupButton, Priority.ALWAYS);
            
            actions.setAlignment(Pos.BASELINE_CENTER);
            signupButton.setMinWidth(200);
            
            SignupStep.setVgrow(actions, Priority.ALWAYS);
        }
    }

    public PersonalInfoStep(PassPort app, Action toLogin) {
        super();
    
        this.app = app;
        this.toLogin = toLogin;
        this.ui = new Components();

        setupUI();
        setupActions();
        translate();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void setupActions() {
        ui.backButton.setOnAction(
                _ -> ((SignupStepPane) getParent()).prev());
        ui.signupButton.setOnAction(_ -> handleRegistration());
    }

    @Override
    protected boolean validate() {
        if (ui.fullName.getText().trim().isEmpty()) {
            app.warn().error("validation.fullname.required");
            return false;
        }

        String cpfText = ui.cpf.getText().trim();
        if (cpfText.isEmpty()) {
            app.warn().error("validation.cpf.required");
            return false;
        }
        if (!Pattern.compile(CPF_PATTERN).matcher(cpfText).matches()) {
            app.warn().error("validation.cpf.invalid.format");
            return false;
        }

        return true;
    }

    private void handleRegistration() {
        if (!validate()) {
            return;
        }

        try {
            CredentialsStep credentialsStep = (CredentialsStep) getParent()
                    .getChildrenUnmodifiable().get(0);

            final var login = new Login(
                    credentialsStep.email(),
                    credentialsStep.getPassword());

            final var person = new Person(
                    ui.fullName.getText(),
                    ui.cpf.getText());

            app.services().signup()
                    .login(login)
                    .person(person)
                    .register();

            app.warn().success("validation.registration.success");
            toLogin.exec();
        }
        catch (EmailAlreadyExists e) {
            app.warn().error("validation.email.exists");
        }
        catch (Exception e) {
            app.warn().error("validation.registration.failed");
        }
    }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    private void setupUI() {
        setAlignment(Pos.CENTER);
        getChildren().addAll(
                ui.fullName,
                ui.cpf,
                ui.actions);
    }

    // =~=~=~=~= =~=~=~=~= SETUP TRANSLATIONS =~=~=~=~= =~=~=~=~=

    private void translate() {
        app.translator()
            .translateFrom(ui.fullName::setPromptText, "logon.fullname")
            .translateFrom(ui.cpf::setPromptText, "logon.cpf")
            .translateFrom(ui.signupButton::setText, "logon.button")
            .translateFrom(ui.backButton::setText, "logon.back")
            .resourcesProp().addListener((_, _, _) -> translate());
    }
}