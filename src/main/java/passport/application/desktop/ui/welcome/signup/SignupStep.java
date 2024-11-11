package passport.application.desktop.ui.welcome.signup;

import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import passport.application.desktop.Translator;

public abstract class SignupStep extends VBox {
    protected final SignupForm form;

    protected SignupStep(SignupForm form) {
        this.form = form;
        setSpacing(15);
    }

    protected abstract boolean validate();

    protected void showError(String messageKey) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(Translator.instance().translationOf(messageKey));
        alert.show();
    }

    protected void showSuccess(String messageKey) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(Translator.instance().translationOf(messageKey));
        alert.show();
    }
}