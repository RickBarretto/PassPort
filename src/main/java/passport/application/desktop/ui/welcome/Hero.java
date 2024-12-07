package passport.application.desktop.ui.welcome;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Pane "Hero" da janela de boas-vindas.
 */
public class Hero extends StackPane {

    /**
     * Inicializa a interface do componente.
     */
    public Hero() { setupUI(); }

    // =~=~=~=~= =~=~=~=~= SETUP UI =~=~=~=~= =~=~=~=~=

    /**
     * Configura a interface do componente
     */
    void setupUI() {
        setupCss();
        var appName = appName();
        addChildren(appName);
    }

    /**
     * Adiciona os elementos filhos.
     *
     * @param appName O rótulo com o nome do aplicativo a ser adicionado.
     */
    private void addChildren(Label appName) { getChildren().add(appName); }

    /**
     * Configura o estilo CSS do componente.
     */
    private void setupCss() {
        setAlignment(Pos.CENTER);
        getStyleClass().add("hero");
    }

    /**
     * Retorna um Label com o nome do aplicativo.
     *
     * @return Um Label configurado com o nome do aplicativo.
     */
    private Label appName() {
        Label appName = new Label("PassPort");
        appName.getStyleClass().add("app-name");
        return appName;
    }
}
