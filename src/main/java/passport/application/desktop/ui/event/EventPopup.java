package passport.application.desktop.ui.event;

import java.time.LocalDate;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import passport.application.desktop.system.PassPort;
import passport.domain.exceptions.TryingToEvaluateActiveEvent;
import passport.domain.models.events.Event;

/**
 * Manages the popup window for event details, comments, and purchasing.
 */
public class EventPopup {
    private final PassPort app;
    private final Event event;
    private final Components ui;

    /**
     * Holds UI components for the event popup.
     */
    class Components {
        final VBox root;
        final EventDetails details;
        final Comments comments;
        final Button buy;

        /**
         * @param app        The PassPort application instance.
         * @param event      The event being displayed.
         * @param addComment Consumer for adding comments to the event.
         */
        public Components(
                PassPort app,
                Event event,
                Consumer<String> addComment) {
            details = new EventDetails(app, event);
            this.comments = newComments(app, event, addComment);
            this.buy = buyButton(app, event);
            root = root();
        }

        private VBox root() {
            var root = new VBox(10);
            root.setPadding(new Insets(10));

            root.getChildren().add(details);
            return root;
        }

        private Button buyButton(PassPort app, Event event) {
            var btn = new Button(app.translator().translationOf("events.buy"));
            btn.setAccessibleText(app.translator().translationOf("events.buy"));

            if (event.boxOffice().isSoldOut()) {
                btn.setDisable(true);
            }
            else {
                btn.getStyleClass().add("accent");
                btn.setOnAction(_ -> app.toPurchaseOf(event));
            }

            return btn;
        }

        private Comments newComments(PassPort app, Event event,
                Consumer<String> addComment) {
            var comments = new Comments(app, event, addComment);
            if (!event.isAvailableFor(LocalDate.now())) {
                comments = comments.withForms();
            }
            return comments;
        }
    }

    /**
     * @param app   The PassPort application instance.
     * @param event The event being displayed.
     */
    public EventPopup(PassPort app, Event event) {
        this.app = app;
        this.event = event;
        this.ui = new Components(app, event, this::comment);
    }

    /**
     * Sets up the UI for purchasing tickets.
     *
     * @return The updated EventPopup instance.
     */
    public EventPopup forPurchasing() {
        this.add(ui.buy);
        this.setupStage(ui.root);
        return this;
    }

    /**
     * Sets up the UI for allowing reviewing.
     *
     * @return The updated EventPopup instance.
     */
    public EventPopup forReviewing() {
        this.add(ui.comments);
        this.setupStage(ui.root);
        return this;
    }

    private void add(Region child) { ui.root.getChildren().add(child); }

    private void setupStage(Region root) {
        Scene scene = new Scene(root, 800, 600);
        var stage = this.newStageFromCurrent();
        stage.setTitle(app.translator().translationOf("events.title"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a new stage with modal properties. It uses
     * {@link Modality#APPLICATION_MODAL} to avoid side-effects.
     *
     * @return The newly created modal stage.
     */
    private Stage newStageFromCurrent() {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(app.stage());
        return newStage;
    }

    /**
     * Initiates the purchase process for the event.
     */
    public void buy() { app.toPurchaseOf(event); }

    /**
     * Adds a comment to the event.
     *
     * @param content The content of the comment.
     */
    public void comment(String content) {
        var author = app.services()
                .login()
                .current()
                .get();

        try {
            app.services().evaluation()
                    .of(event.id())
                    .by(author.id())
                    .evaluateWith(content);

            ui.comments.add(author.person().name(), content);
        }
        catch (TryingToEvaluateActiveEvent e) {
            e.printStackTrace();
        }
    }
}
