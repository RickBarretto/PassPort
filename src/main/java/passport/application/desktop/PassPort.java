package passport.application.desktop;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

    class Warning {
        public void error(String messageKey) {
            show(messageKey, Alert.AlertType.ERROR);
        } 
        
        public void success(String messageKey) {
            show(messageKey, Alert.AlertType.INFORMATION);
        }

        private void show(String messageKey, Alert.AlertType type) {
            var alert = new Alert(type);
            alert.setContentText(Translator.instance().translationOf(messageKey));
            alert.show();
        }
    }

    public Warning warn() {
        return new Warning();
    }
}
