package passport.application.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import passport.application.desktop.ui.welcome.WelcomeWindow;

public class App extends Application {
    @Override
    public void start(Stage root) {
        WelcomeWindow welcomeWindow = new WelcomeWindow();
        Scene scene = new Scene(welcomeWindow, 1200, 700);

        // @formatter:off
        var css = scene.getStylesheets();
        var mainCSS = getClass().getResource("/desktop/styles.css").toExternalForm();
        var languageSelectorCSS = getClass().getResource("/desktop/language-selector.css").toExternalForm();
        // @formatter:on

        css.add(mainCSS);
        css.add(languageSelectorCSS);

        root.setTitle("PassPort");
        root.setScene(scene);
        root.show();
    }

    public static void main(String[] args) { launch(args); }
}