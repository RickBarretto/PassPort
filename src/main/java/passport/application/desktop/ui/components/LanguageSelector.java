package passport.application.desktop.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import passport.application.desktop.Language;
import passport.application.desktop.PassPort;

public class LanguageSelector extends HBox {
    private final PassPort app;
    private final ComboBox<Language> languageComboBox;

    public LanguageSelector(PassPort app) {
        this.app = app;
        this.languageComboBox = new ComboBox<>();
        setupComboBox();
        setupContainer();
    }

    private void setupComboBox() {
        // @formatter:off
        languageComboBox.getItems().addAll(Language.values());
        languageComboBox.setValue(app.translator().language());
        languageComboBox.setOnAction(
            _ -> app.translator().language(languageComboBox.getValue())
        );
        languageComboBox.getStyleClass().add("text");
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
        app.translator().language(language);
    }

    public Language language() { return languageComboBox.getValue(); }
}