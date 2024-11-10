package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.ui.components.LanguageSelector;

public class WelcomeWindow extends HBox {
    private final Hero hero;
    private final Login login;
    private final Logon logon;
    private final LanguageSelector languageSelector;

    public WelcomeWindow() {
        hero = new Hero();
        login = new Login(this);
        logon = new Logon(this);
        languageSelector = new LanguageSelector();

        VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().add(login);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(languageSelector, mainContent);
        HBox.setHgrow(hero, Priority.ALWAYS);

        getChildren().addAll(hero, rightPane);
        getStyleClass().add("welcome-window");
    }

    public void switchToLogon() {
        VBox rightPane = (VBox) getChildren().get(1);
        VBox mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, logon);
    }

    public void switchToLogin() {
        VBox rightPane = (VBox) getChildren().get(1);
        VBox mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, login);
    }
}