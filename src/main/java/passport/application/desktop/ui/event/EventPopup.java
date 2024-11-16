package passport.application.desktop.ui.event;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import passport.domain.models.events.Event;

public class EventPopup {
    private final Stage stage;
    private final Components ui;

    class Components {
        final EventDetails details;
        final Comments comments;

        public Components(Event event) {
            details = new EventDetails(event);
            comments = new Comments();
        }
    }

    public EventPopup(Stage parentStage, Event event) {
        this.ui = new Components(event);
        this.stage = setupStage(parentStage);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        root.getChildren().addAll(
                ui.details,
                new Separator(),
                ui.comments);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Event Information");
        stage.setScene(scene);
    }

    private Stage setupStage(Stage parentStage) {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(parentStage);
        return newStage;
    }

    public void comment(String username, String content) {
        ui.comments.addComment(username, content);
    }

    public void show() { stage.show(); }
}