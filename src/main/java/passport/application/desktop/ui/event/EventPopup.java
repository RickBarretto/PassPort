package passport.application.desktop.ui.event;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import passport.application.desktop.system.PassPort;
import passport.domain.models.events.Event;

public class EventPopup {
    private final PassPort app;
    private final Components ui;

    class Components {
        final EventDetails details;
        final Comments comments;

        public Components(Event event) {
            details = new EventDetails(event);
            comments = new Comments();
        }
    }

    public EventPopup(PassPort app, Event event) {
        this.app = app;
        this.ui = new Components(event);

        VBox root = root();
        this.setupStage(root);
    }

    private VBox root() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        root.getChildren().addAll(
                ui.details,
                new Separator(),
                ui.comments);

        return root;
    }

    private void setupStage(Region root) {
        Scene scene = new Scene(root, 800, 600);
        var stage = this.newStageFromCurrent();
        stage.setTitle("Event Information");
        stage.setScene(scene);
        stage.show();
    }

    private Stage newStageFromCurrent() {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(app.stage());
        return newStage;
    }

    public void comment(String username, String content) {
        ui.comments.addComment(username, content);
    }
}