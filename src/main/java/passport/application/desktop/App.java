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
    final Services services;
    Stage root;

    public App() {
        infra = new Infra(
                new UsersInMemory(),
                new EventsInMemory(),
                new DisabledEmailService(),
                new Session());

        services = new Services(
                new SigningUp(infra.users()),
                new UserLogin(infra.session(), infra.users()));
    }

    @Override
    public void start(Stage root) {
        this.root = root;

        WelcomeWindow welcomeWindow = new WelcomeWindow(this, services.signup(), services.login());
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
        Application.setUserAgentStylesheet(
                new PrimerDark().getUserAgentStylesheet());

        // @formatter:off
        var css = scene.getStylesheets();

        var main = getClass()
            .getResource("/desktop/styles.css")
            .toExternalForm();
        
        var langSelector = getClass()
            .getResource("/desktop/language-selector.css")
            .toExternalForm();
            
        var WelcomesHero = getClass()
            .getResource("/desktop/ui/welcome/hero.css")
            .toExternalForm();

        
        // @formatter:on

        css.add(main);
        css.add(langSelector);
        css.add(WelcomesHero);
    }

    public static void main(String[] args) { launch(args); }
}