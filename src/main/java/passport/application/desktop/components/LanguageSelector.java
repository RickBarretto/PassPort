package passport.application.desktop.components;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import passport.application.desktop.Language;
import passport.application.desktop.Translator;

public class LanguageSelector extends HBox {
    private final ComboBox<Language> languageComboBox;

    public LanguageSelector() {
        languageComboBox = new ComboBox<>();
        setupComboBox();
        setupContainer();
    }

    private void setupComboBox() {
        // @formatter:off
        languageComboBox.getItems().addAll(Language.values());
        languageComboBox.setValue(Translator.instance().language());
        languageComboBox.setOnAction(
            _ -> Translator.instance().language(languageComboBox.getValue())
        );
        languageComboBox.getStyleClass().add("language-selector");
        // @formatter:on
    }

    private void setupContainer() {
        getStyleClass().add("language-selector-container");
        setAlignment(Pos.CENTER_RIGHT);
        getChildren().add(languageComboBox);
    }

    public ComboBox<Language> widget() { return languageComboBox; }

    public void language(Language language) {
        languageComboBox.setValue(language);
        Translator.instance().language(language);
    }

    public Language language() { return languageComboBox.getValue(); }
}