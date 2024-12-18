package passport.application.desktop.ui.profile;

import java.util.regex.Pattern;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import passport.domain.contexts.user.UserEditing.EditingWithTarget;
import passport.domain.exceptions.EmailAlreadyExists;
import passport.domain.exceptions.InexistentUser;
import passport.domain.models.users.values.Password;

/**
 * Manages the profile editing window.
 */
public class EditProfile {
    final PassPort app;
    final Components ui;
    private Stage stage;

    /**
     * Validation patterns and constants.
     */
    static class Validation {
        private static final String CPF_PATTERN = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
        private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
        private static final int MIN_PASSWORD_LENGTH = 8;
    }

    /**
     * Holds UI components for the profile editing form.
     */
    class Components {
        TextField email = new TextField();
        PasswordField password = new PasswordField();
        TextField name = new TextField();
        TextField cpf = new TextField();
        PasswordField confirmation = new PasswordField();
        Button save = new Button();

        /**
         * @param saveOnClick Action to save the profile changes.
         */
        public Components(Action saveOnClick) {
            confirmation.setMaxWidth(250);
            confirmation.setPrefWidth(250);

            save.setMaxWidth(250);
            save.setPrefWidth(250);
            this.save.getStyleClass().add("accent");
            this.save.setOnAction((_) -> saveOnClick.exec());
            translate();
        }

        private void translate() {
            app.translator().translateFrom(save::setText, "profile.save");
            app.translator().translateFrom(save::setAccessibleText,
                    "profile.save");
        }
    }

    /**
     * @param app The PassPort application instance.
     */
    public EditProfile(PassPort app) {
        this.app = app;
        this.ui = new Components(this::save);
        setup();
    }

    private void save() {
        if (!isValid()) {
            return;
        }

        var user = app.services()
                .login()
                .current()
                .get();

        try {
            this.changeFromFields(app.services().profileEditing()
                    .of(user)
                    .changing())
                    .edit();

            var translator = app.translator();
            this.close();
            app.toWelcome();
            app.warn().notify(
                    translator.translationOf("profile.notify.title"),
                    translator.translationOf("profile.notify.msg"));
        }
        catch (EmailAlreadyExists e) {
            app.warn().error("validation.email.exists");
        }
        catch (InexistentUser e) {
            System.err.println("User must exists. This should never happen.");
        }
    }

    /**
     * Verifies if all fields are vaid. If a field is empty, this will be
     * ignored. All rules are described internally by comments.
     * 
     * @return if all fields are valid.
     */
    private boolean isValid() {

        // Confirmation field is mandatory

        if (ui.confirmation.getText().isEmpty()) {
            app.warn().error("profile.confirmation.missing");
            return false;
        }

        // Password & Password Confirmation should be the same

        var user = app.services().login().current().get();
        var samePassword = user.isOwnerOf(
                user.login().with(new Password(ui.confirmation.getText())));

        if (!samePassword) {
            app.warn().error("profile.confirmation.wrong");
            return false;
        }

        // Email should follow valid pattern: email@example.com

        var email = ui.email.getText();
        var filledEmail = !email.isEmpty();
        var invalidEmailPattern = !this.hasPattern(ui.email,
                Validation.EMAIL_PATTERN);
        if (filledEmail && invalidEmailPattern) {
            app.warn().error("validation.email.invalid");
            return false;
        }

        // CPF should follow valid pattern: DDD.DDD.DDD-DD

        var cpf = ui.cpf.getText();
        var filledCPF = !cpf.isEmpty();
        var invalidCpfPattern = !this.hasPattern(ui.cpf,
                Validation.CPF_PATTERN);
        if (filledCPF && invalidCpfPattern) {
            app.warn().error("validation.cpf.invalid.format");
            return false;
        }

        // Passwords must have at least 8 characters

        var password = ui.password.getText();
        var filledPassword = !password.isEmpty();
        var isPasswordTooSmall = password
                .length() < Validation.MIN_PASSWORD_LENGTH;
        if (filledPassword && isPasswordTooSmall) {
            app.warn().error("validation.password.length");
            return false;
        }

        // At least one field should be filled

        if (email.isEmpty() && cpf.isEmpty() && password.isEmpty()
                && ui.name.getText().isEmpty()) {
            app.warn().error("profile.missing-fields");
            return false;
        }

        return true;
    }

    private boolean hasPattern(TextField field, String pattern) {
        return Pattern.matches(pattern, field.getText());
    }

    private EditingWithTarget changeFromFields(EditingWithTarget change)
            throws EmailAlreadyExists {
        if (this.isFilled(ui.email))
            change = change.email(ui.email.getText());
        if (this.isFilled(ui.password))
            change = change.password(ui.password.getText());
        if (this.isFilled(ui.name))
            change = change.name(ui.name.getText());
        if (this.isFilled(ui.cpf))
            change = change.cpf(ui.cpf.getText());
        return change;
    }

    private boolean isFilled(TextField field) {
        return !field.getText().isEmpty();
    }

    private void setup() {
        var top = $("profile.edit");

        var middle = new HBox(5,
                new VBox(10,
                        $("logon.email"), ui.email,
                        $("logon.password"), ui.password),
                vsplit(),
                new VBox(10,
                        $("logon.fullname"), ui.name,
                        $("logon.cpf"), ui.cpf));

        var bottom = new VBox(10,
                $("profile.current-password"),
                ui.confirmation,
                ui.save);

        var main = new VBox(20,
                top,
                middle,
                bottom);

        top.getStyleClass().add("title-1");
        middle.setAlignment(Pos.CENTER);
        bottom.setFillWidth(true);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        main.setPadding(new Insets(20));
        main.setAlignment(Pos.CENTER);

        openStage(main);
    }

    private Label $(String key) {
        final var text = app.translator().translationOf(key);
        var label = new Label(text);
        label.setAccessibleText(text);

        return label;
    }

    private Separator vsplit() {
        var widget = new Separator();
        widget.setOrientation(Orientation.VERTICAL);
        return widget;
    }

    /**
     * Opens the profile editing stage. This is set as
     * {@link Modality#APPLICATION_MODAL}, so this ensures the User won't do
     * anything else without updating their credentials or close this window,
     * avoiding undesirable side-effects.
     *
     * @param root The root region of the scene.
     */
    void openStage(Region root) {
        var editProfile = new Stage();
        editProfile.initModality(Modality.APPLICATION_MODAL);
        editProfile.initOwner(app.stage());
        editProfile.setTitle(app.translator().translationOf("profile.edit"));
        var scene = new Scene(root, 500, 400);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE)
                close();
        });
        editProfile.setScene(scene);
        this.stage = editProfile;
        editProfile.show();
    }

    /**
     * Closes the profile editing window.
     */
    void close() { stage.close(); }
}
