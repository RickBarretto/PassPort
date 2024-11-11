package passport.application.desktop.ui.welcome.signup;

import javafx.scene.layout.StackPane;

public class SignupStepPane extends StackPane {
    private final SignupStep[] steps;
    private int currentStep = 0;

    public SignupStepPane(SignupStep... steps) {
        this.steps = steps;
        getChildren().addAll(steps);
        showCurrentStep();
    }

    public void next() {
        if (currentStep >= steps.length - 1)
            return;

        currentStep++;
        showCurrentStep();
    }

    public void prev() {
        if (currentStep <= 0)
            return;
            
        currentStep--;
        showCurrentStep();
    }

    private void showCurrentStep() {
        for (var step : steps)
            step.setVisible(false);
        steps[currentStep].setVisible(true);
    }
}