package passport.application.desktop.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;

public class ProgressIndicator extends VBox {
    private final ProgressBar progressBar;
    private final Label progressLabel;
    private int currentStep;
    private final int totalSteps;

    private ProgressIndicator(int from, int to) {
        this.currentStep = from;
        this.totalSteps = to;
        progressBar = new ProgressBar();
        progressLabel = new Label();

        setupUI();
        updateProgress();
    }

    public ProgressIndicator() { this(1, 2); }
    
    static public ProgressIndicator until(int step) {
        return new ProgressIndicator(1, step);
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

    private void updateProgress() {
        progressBar.setProgress((double) currentStep / totalSteps);
        progressLabel.setText(String.format(
                Translator.instance().translationOf("progress.step"),
                currentStep, totalSteps));
    }
}
