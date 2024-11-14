package passport.application.desktop.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;

public class ProgressIndicator extends VBox {
    private final Translator translator;
    private final ProgressBar progressBar;
    private final Label progressLabel;
    private int currentStep;
    private final int totalSteps;

    private ProgressIndicator(Translator translator, int currentStep,
            int finalStep) {
        this.translator = translator;
        this.currentStep = currentStep;
        this.totalSteps = finalStep;
        progressBar = new ProgressBar();
        progressLabel = new Label();

        setupUI();
        updateProgress(translator);
    }

    public ProgressIndicator(Translator translator) { this(translator, 1, 2); }

    public ProgressIndicator current(int first) {
        return new ProgressIndicator(translator, first, totalSteps);
    }

    public ProgressIndicator of(int end) {
        return new ProgressIndicator(translator, currentStep, end);
    }

    private void setupUI() {
        progressBar.setMinWidth(250);
        progressBar.setMaxWidth(250);
        progressBar.setMinHeight(20);
        progressBar.setStyle(
                "-fx-accent: #2196F3; -fx-background-radius: 5px; -fx-border-radius: 5px;");

        setAlignment(Pos.CENTER);
        setMaxWidth(100);
        setSpacing(5);
        getChildren().addAll(progressLabel, progressBar);
    }

    private void updateProgress(Translator translator) {
        progressBar.setProgress((double) currentStep / totalSteps);
        this.translate();
    }

    private void translateLabel(String template) {
        progressLabel.setText(
                String.format(template, currentStep, totalSteps));
    }

    private void translate() {
        translator
                .translateFrom(this::translateLabel, "progress.step")
                .resourcesProp().addListener((_, _, _) -> translate());
    }
}
