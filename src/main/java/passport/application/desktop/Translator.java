package passport.application.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import passport.application.desktop.system.Language;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Responsável por gerenciar a tradução de strings em tempo de execução.
 * Observação: {@link ObjectProperty} é usado para permitir a atualização
 * dinâmica do conteúdo.
 */
public class Translator {
    private final ObjectProperty<ResourceBundle> resources;
    private final ObjectProperty<Language> currentLanguage;

    /**
     * Inicializa com o idioma inglês por padrão.
     */
    public Translator() {
        currentLanguage = new SimpleObjectProperty<>(Language.ENGLISH);
        resources = new SimpleObjectProperty<>();
        language(Language.ENGLISH);
    }

    /**
     * Define o idioma atual e carrega seu respectivo ResourceBundle.
     *
     * @param language O idioma a ser definido.
     */
    public void language(Language language) {
        final var path = "passport.application.desktop.resources.messages";

        var locale = Locale.of(language.code());
        var bundle = ResourceBundle.getBundle(path, locale);

        resources.set(bundle);
        currentLanguage.set(language);
    }

    /**
     * Configura a tradução de um texto, utilizando um setter de consumidor para
     * aplicar a tradução.
     *
     * @param textSetter O consumidor que define o texto traduzido.
     * @param property   A propriedade de tradução a ser utilizada.
     * @return A instância atual do Translator.
     */
    public Translator translateFrom(
            Consumer<String> textSetter,
            String property) {
        textSetter.accept(this.resources().getString(property));
        return this;
    }

    /**
     * Retorna a tradução da propriedade especificada.
     *
     * @param property A propriedade de tradução a ser utilizada.
     * @return A string traduzida.
     */
    public String translationOf(String property) {
        return resources().getString(property);
    }

    /**
     * Retorna o ResourceBundle atual.
     *
     * @return O ResourceBundle atual.
     */
    public ResourceBundle resources() { return resources.get(); }

    /**
     * ResourceBundle como ObjectProperty.
     *
     * @return A propriedade do ResourceBundle.
     */
    public ObjectProperty<ResourceBundle> resourcesProp() {
        return resources;
    }

    /**
     * Idioma atual.
     *
     * @return O idioma atual.
     */
    public Language language() { return currentLanguage.get(); }

    /**
     * Idioma atual como um ObjectProperty.
     *
     * @return A propriedade do idioma atual.
     */
    public ObjectProperty<Language> languageProp() {
        return currentLanguage;
    }
}
