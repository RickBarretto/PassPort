package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.application.desktop.ui.components.LanguageSelector;
import passport.application.desktop.ui.welcome.login.LoginForm;
import passport.application.desktop.ui.welcome.signup.SignupForm;

/**
 * Classe responsável por configurar a janela de boas-vindas da aplicação.
 */
public class WelcomeWindow extends HBox {
    private final Hero hero;
    private final LoginForm login;
    private final SignupForm signUpForm;
    private final LanguageSelector languageSelector;

    /**
     * Inicializa a janela de boas-vindas.
     *
     * @param app A instância da aplicação PassPort.
     */
    public WelcomeWindow(PassPort app) {
        hero = new Hero();
        login = new LoginForm(app, this::switchToLogon);
        signUpForm = new SignupForm(app, this::switchToLogin);
        languageSelector = new LanguageSelector(app);

        this.setupUI();
    }

    // =~=~=~=~= =~=~=~=~= SETUP ACTIONS =~=~=~=~= =~=~=~=~=

    /**
     * Substitui o conteúdo do painel direito pelo nó fornecido. Isso permite a
     * troca entre os painéis de Login e SignUp.
     *
     * @param node O nó a ser exibido no painel direito.
     */
    private void rightPaneWith(Node node) {
        var rightPane = (VBox) getChildren().get(1);
        var mainContent = (VBox) rightPane.getChildren().get(1);
        mainContent.getChildren().set(0, node);
    }

    /**
     * Alterna para o formulário de Signup.
     */
    public void switchToLogon() { this.rightPaneWith(this.signUpForm); }

    /**
     * Alterna para o formulário de Login.
     */
    public void switchToLogin() { this.rightPaneWith(this.login); }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    /**
     * Configura a interface do usuário da janela de boas-vindas.
     */
    private void setupUI() {
        var mainContent = mainContent();
        var rightPane = rightPane(mainContent);

        HBox.setHgrow(hero, Priority.ALWAYS);

        getChildren().addAll(hero, rightPane);
    }

    /**
     * Cria o painel direito com o seletor de idioma e o conteúdo principal.
     *
     * @param mainContent O conteúdo principal a ser exibido.
     * @return Um VBox configurado como painel direito.
     */
    private VBox rightPane(VBox mainContent) {
        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(languageSelector, mainContent);
        return rightPane;
    }

    /**
     * Cria o conteúdo principal da janela de boas-vindas.
     *
     * @return Um VBox configurado como conteúdo principal.
     */
    private VBox mainContent() {
        VBox mainContent = new VBox();

        VBox.setVgrow(mainContent, Priority.ALWAYS);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.getChildren().add(login);

        return mainContent;
    }
}
