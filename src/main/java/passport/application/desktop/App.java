package passport.application.desktop;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import passport.application.desktop.ui.main.MainWindow;
import passport.application.desktop.ui.welcome.WelcomeWindow;
import passport.domain.contexts.user.SigningUp;
import passport.domain.contexts.user.UserLogin;
import passport.infra.DisabledEmailService;
import passport.infra.Session;
import passport.infra.virtual.EventsInMemory;
import passport.infra.virtual.UsersInMemory;

public class App extends Application {
    final Infra infra;
    Stage root;

    public App() {
        infra = new Infra(
                new UsersInMemory(),
                new EventsInMemory(),
                new DisabledEmailService(),
                new Session());
    }

    @Override
    public void start(Stage root) {
        this.root = root;

        var signInUp = new SigningUp(infra.users());
        var logIn = new UserLogin(infra.session(), infra.users());

        WelcomeWindow welcomeWindow = new WelcomeWindow(this, signInUp, logIn);
        Scene scene = new Scene(welcomeWindow, 1200, 700);

        setupStyle(scene);
        setupRoot(root, scene);
        root.show();
    }

    public void openMainWindow() {
        Scene scene = new Scene(new MainWindow(infra), 1200, 700);
        setupStyle(scene);

        Stage currentStage = (Stage) root.getScene().getWindow();
        currentStage.setScene(scene);
    }

    private void setupRoot(Stage root, Scene scene) {
        root.setTitle("PassPort");
        root.setScene(scene);
    }

    private void setupStyle(Scene scene) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // @formatter:off
        var css = scene.getStylesheets();
        var mainCSS = getClass().getResource("/desktop/styles.css").toExternalForm();
        var languageSelectorCSS = getClass().getResource("/desktop/language-selector.css").toExternalForm();
        // @formatter:on

        css.add(mainCSS);
        css.add(languageSelectorCSS);
    }

    public static void main(String[] args) { launch(args); }
}