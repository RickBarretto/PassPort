package passport.application.desktop.ui.welcome.signup;

import javafx.scene.layout.StackPane;

/**
 * Manages and navigates multiple signup steps.
 */
public class SignupStepPane extends StackPane {
    private final SignupStep[] steps;
    private int currentStep = 0;

    /**
     * @param steps The signup steps.
     */
    public SignupStepPane(SignupStep... steps) {
        this.steps = steps;
        getChildren().addAll(steps);
        showCurrentStep();
    }

    /** Advances to the next step. */
    public void next() {
        if (currentStep >= steps.length - 1)
            return;

        currentStep++;
        showCurrentStep();
    }

    /** Returns to the previous step. */
    public void prev() {
        if (currentStep <= 0)
            return;

        currentStep--;
        showCurrentStep();
    }

    /** Shows the current step, hides others. */
    private void showCurrentStep() {
        for (var step : steps)
            step.setVisible(false);
        steps[currentStep].setVisible(true);
    }
}
