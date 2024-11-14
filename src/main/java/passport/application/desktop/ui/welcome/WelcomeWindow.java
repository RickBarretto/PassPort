package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.App;
import passport.application.desktop.PassPort;
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

    public WelcomeWindow(App application, SigningUp signUpContext, UserLogin logInContext, PassPort app) {
        hero = new Hero();
        login = new LoginForm(app, this::switchToLogon);
        signUpForm = new SignupForm(app, this::switchToLogin);
        languageSelector = new LanguageSelector(app);

        this.setupUI();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    private void rightPaneWith(Node node) {
        var rightPane = (VBox) getChildren().get(1);
        var mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, node);
    }

    public void switchToLogon() {
        this.rightPaneWith(this.signUpForm);
    }

    public void switchToLogin() {
        this.rightPaneWith(this.login);
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