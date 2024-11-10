package passport.application.desktop;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class Translator {
    private static Optional<Translator> instance = Optional.empty();
    private final ObjectProperty<ResourceBundle> resources;
    private final ObjectProperty<Language> currentLanguage;

    private Translator() {
        currentLanguage = new SimpleObjectProperty<>(Language.ENGLISH);
        resources = new SimpleObjectProperty<>();
        language(Language.ENGLISH);
    }

    public static Translator instance() {
        if (instance.isEmpty())
            instance = Optional.of(new Translator());
        return instance.get();
    }

    public void language(Language language) {
        final var path = "passport.application.desktop.resources.messages";

        var locale = Locale.of(language.code());
        var bundle = ResourceBundle.getBundle(path, locale);

        resources.set(bundle);
        currentLanguage.set(language);
    }

    public Translator translateFrom(
            Consumer<String> textSetter,
            String property) {
        textSetter.accept(this.resources().getString(property));
        return this;
    }

    public String translationOf(String property) {
        return resources().getString(property);
    }

    public ResourceBundle resources() { return resources.get(); }

    public ObjectProperty<ResourceBundle> resourcesProp() { return resources; }

    public Language language() { return currentLanguage.get(); }

    public ObjectProperty<Language> languageProp() { return currentLanguage; }
}