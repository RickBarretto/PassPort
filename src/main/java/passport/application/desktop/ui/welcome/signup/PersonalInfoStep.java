package passport.application.desktop.ui.welcome.signup;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import passport.application.desktop.Translator;
import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.models.users.Login;
import passport.domain.models.users.Person;
import java.util.regex.Pattern;

public class PersonalInfoStep extends SignupStep {
    private static final String CPF_PATTERN = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

    private final TextField fullName;
    private final TextField cpf;
    private final Button signupButton;
    private final Button backButton;

    public PersonalInfoStep(SignupForm form) {
        super(form);

        fullName = new TextField();
        cpf = new TextField();
        signupButton = new Button();
        backButton = new Button();

        setupUI();
        setupActions();
        translate();
    }

    private void setupUI() {
        signupButton.getStyleClass().add("primary-button");
        backButton.getStyleClass().add("secondary-button");

        HBox buttonContainer = new HBox(10, signupButton, backButton);
        buttonContainer.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
        getChildren().addAll(
                fullName,
                cpf,
                buttonContainer);
    }

    private void setupActions() {
        backButton.setOnAction(
                _ -> ((SignupStepManager) getParent()).prev());
        signupButton.setOnAction(_ -> handleRegistration());
    }

    @Override
    protected boolean validate() {
        if (fullName.getText().trim().isEmpty()) {
            showError("validation.fullname.required");
            return false;
        }

        String cpfText = cpf.getText().trim();
        if (cpfText.isEmpty()) {
            showError("validation.cpf.required");
            return false;
        }
        if (!Pattern.compile(CPF_PATTERN).matcher(cpfText).matches()) {
            showError("validation.cpf.invalid.format");
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

            form.registering()
                    .login(new Login(credentialsStep.email(),
                            credentialsStep.getPassword()))
                    .person(new Person(fullName.getText(), cpf.getText()))
                    .register();

            showSuccess("validation.registration.success");
            form.parent().switchToLogin();
        }
        catch (EmailAlreadyExists e) {
            showError("validation.email.exists");
        }
        catch (Exception e) {
            showError("validation.registration.failed");
        }
    }

    private void translate() {
        Translator translator = Translator.instance();
        translator.translateFrom(fullName::setPromptText, "logon.fullname");
        translator.translateFrom(cpf::setPromptText, "logon.cpf");
        translator.translateFrom(signupButton::setText, "logon.button");
        translator.translateFrom(backButton::setText, "logon.back");
    }
}