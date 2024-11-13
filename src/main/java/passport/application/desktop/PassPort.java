package passport.application.desktop;

import javafx.scene.Scene;
import javafx.stage.Stage;
import passport.application.desktop.ui.main.MainWindow;

public record PassPort(Stage stage, Services services) {

    public PassPort(Services services) { this(null, services); }

    public PassPort withStage(Stage stage) {
        return new PassPort(stage, services);
    }

    public Stage stage() {
        assert stage != null;
        return stage;
    }

    public void toMain() { toScene(new Scene(new MainWindow(), 1200, 700)); }

    private void toScene(Scene scene) {
        Stage currentStage = (Stage) stage.getScene().getWindow();
        currentStage.setScene(scene);
    }
}
