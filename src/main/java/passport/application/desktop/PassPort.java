package passport.application.desktop;

import java.util.Optional;

import javafx.stage.Stage;

public record PassPort(Stage stage, Services services) {

    public PassPort(Services services) { this(null, services); }
    
    public PassPort withStage(Stage stage) {
        return new PassPort(stage, services);
    }
    
    public Stage stage() {
        assert stage != null;
        return stage;
    }

}
