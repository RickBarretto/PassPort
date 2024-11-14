package passport.application.desktop.ui.welcome.signup;

import javafx.scene.layout.VBox;

public abstract class SignupStep extends VBox {

    protected SignupStep() {
        setSpacing(15);
    }

    protected abstract boolean validate();
}