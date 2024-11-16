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
    private PassPort self;

    public App() {
        var infra = new Infra(
                new UsersInMemory(),
                new EventsInMemory(),
                new DisabledEmailService(),
                new Session());

        var services = new Services(
                new SigningUp(infra.users()),
                new UserLogin(infra.session(), infra.users()));

        self = new PassPort(services);
    }

    @Override
    public void start(Stage root) {
        this.self = self.withStage(root);

        Application.setUserAgentStylesheet(
                new PrimerDark().getUserAgentStylesheet());

        WelcomeWindow welcomeWindow = new WelcomeWindow(self);
        Scene scene = new Scene(welcomeWindow, 1200, 700);

        setupRoot(root, scene);
        root.show();
    }

    private void setupRoot(Stage root, Scene scene) {
        root.setTitle("PassPort");
        root.setScene(scene);
        self.toScene(scene);
    }

    private void setupStyle(Scene scene) {
        Application.setUserAgentStylesheet(
                new PrimerDark().getUserAgentStylesheet());

        // @formatter:off
        var css = scene.getStylesheets();

        var main = getClass()
            .getResource("/desktop/styles/styles.css")
            .toExternalForm();
        
        var langSelector = getClass()
            .getResource("/desktop/styles/language-selector.css")
            .toExternalForm();
            
        var WelcomesHero = getClass()
            .getResource("/desktop/styles/hero.css")
            .toExternalForm();

        
        // @formatter:on

        css.add(main);
        css.add(langSelector);
        css.add(WelcomesHero);
    }

    public static void main(String[] args) { launch(args); }
}