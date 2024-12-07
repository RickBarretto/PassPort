package passport.application.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import passport.application.desktop.system.Language;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Responsible for managing string translations at runtime. Note:
 * {@link ObjectProperty} is used to allow dynamic content updates.
 */
public class Translator {
    private final ObjectProperty<ResourceBundle> resources;
    private final ObjectProperty<Language> currentLanguage;

    /**
     * Initializes with the English language by default.
     */
    public Translator() {
        currentLanguage = new SimpleObjectProperty<>(Language.ENGLISH);
        resources = new SimpleObjectProperty<>();
        language(Language.ENGLISH);
    }

    /**
     * Sets the current language and loads its respective ResourceBundle.
     *
     * @param language The language to set.
     */
    public void language(Language language) {
        final var path = "passport.application.desktop.resources.messages";

        var locale = Locale.of(language.code());
        var bundle = ResourceBundle.getBundle(path, locale);

        resources.set(bundle);
        currentLanguage.set(language);
    }

    /**
     * Sets up the translation of a text, using a consumer to apply the
     * translation.
     *
     * @param textSetter The consumer that sets the translated text.
     * @param property   The translation property to be used.
     * @return The current instance of Translator.
     */
    public Translator translateFrom(
            Consumer<String> textSetter,
            String property) {
        textSetter.accept(this.resources().getString(property));
        return this;
    }

    /**
     * Returns the translation of the specified property.
     *
     * @param property The translation property to be used.
     * @return The translated string.
     */
    public String translationOf(String property) {
        return resources().getString(property);
    }

    /**
     * Returns the current ResourceBundle.
     *
     * @return The current ResourceBundle.
     */
    public ResourceBundle resources() { return resources.get(); }

    /**
     * ResourceBundle as an ObjectProperty.
     *
     * @return The ResourceBundle property.
     */
    public ObjectProperty<ResourceBundle> resourcesProp() {
        return resources;
    }

    /**
     * Returns the current language.
     *
     * @return The current language.
     */
    public Language language() { return currentLanguage.get(); }

    /**
     * Current language as an ObjectProperty.
     *
     * @return The current language property.
     */
    public ObjectProperty<Language> languageProp() {
        return currentLanguage;
    }
}
