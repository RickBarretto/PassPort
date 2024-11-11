package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.ui.components.LanguageSelector;
import passport.application.desktop.ui.welcome.login.LoginForm;
import passport.application.desktop.ui.welcome.signup.SignupForm;
import passport.domain.contexts.user.SigningUp;
import passport.domain.contexts.user.UserLogin;

public class WelcomeWindow extends HBox {
    private final Hero hero;
    private final LoginForm login;
    private final SignupForm signUpForm;
    private final LanguageSelector languageSelector;

    public WelcomeWindow(SigningUp signUpContext, UserLogin logInContext) {
        hero = new Hero();
        login = new LoginForm(this, logInContext);
        signUpForm = new SignupForm(this, signUpContext);
        languageSelector = new LanguageSelector();

        this.setupUI();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    public void switchToLogon() {
        VBox rightPane = (VBox) getChildren().get(1);
        VBox mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, signUpForm);
    }

    public void switchToLogin() {
        VBox rightPane = (VBox) getChildren().get(1);
        VBox mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, login);
    }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    private void setupUI() {
        var mainContent = mainContent();
        var rightPane = rightPane(mainContent);

        HBox.setHgrow(hero, Priority.ALWAYS);

        getChildren().addAll(hero, rightPane);
        getStyleClass().add("welcome-window");
    }

    private VBox rightPane(VBox mainContent) {
        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(languageSelector, mainContent);
        return rightPane;
    }

    private VBox mainContent() {
        VBox mainContent = new VBox();

        VBox.setVgrow(mainContent, Priority.ALWAYS);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().add(login);
        
        return mainContent;
    }
}