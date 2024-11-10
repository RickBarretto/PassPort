package passport.application.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import passport.application.desktop.ui.welcome.WelcomeWindow;
import passport.infra.DisabledEmailService;
import passport.infra.Session;
import passport.infra.virtual.EventsInMemory;
import passport.infra.virtual.UsersInMemory;

public class App extends Application {
    final Infra infra;
    
    public App() {
        infra = new Infra(
            new UsersInMemory(),
            new EventsInMemory(),
            new DisabledEmailService(),
            new Session()
        );
    }

    @Override
    public void start(Stage root) {
        WelcomeWindow welcomeWindow = new WelcomeWindow();
        Scene scene = new Scene(welcomeWindow, 1200, 700);

        setupStyle(scene);
        setupRoot(root, scene);
        root.show();
    }

    private void setupRoot(Stage root, Scene scene) {
        root.setTitle("PassPort");
        root.setScene(scene);
    }

    private void setupStyle(Scene scene) {
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