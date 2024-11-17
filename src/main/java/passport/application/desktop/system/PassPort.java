package passport.application.desktop.system;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import passport.application.desktop.Translator;
import passport.application.desktop.ui.components.Notification;
import passport.application.desktop.ui.event.EventPopup;
import passport.application.desktop.ui.main.Main;
import passport.application.desktop.ui.profile.AccountEditor;
import passport.application.desktop.ui.profile.EditProfile;
import passport.application.desktop.ui.purchase.PurchaseWindow;
import passport.domain.models.events.Event;

public record PassPort(Stage stage, Services services, Translator translator) {

    public PassPort(Services services) {
        this(null, services, new Translator());
    }

    public PassPort withStage(Stage stage) {
        return new PassPort(stage, services, translator);
    }

    public Stage stage() {
        assert stage != null;
        return stage;
    }

    public void toMain() { toScene(new Scene(new Main(this), 1200, 700)); }

    public void toPurchaseOf(Event event) {
        new PurchaseWindow(
                this,
                event,
                this.services().login().current().get());
    }

    public void toEventPurchase(Event event) {
        new EventPopup(this, event).forPurchasing();
    }

    public void toEventReview(Event event) {
        new EventPopup(this, event).forReviewing();
    }

    public void toProfileEditing() {
        new EditProfile(this);
    }

    public void toScene(Scene scene) {
        Stage currentStage = (Stage) stage.getScene().getWindow();
        currentStage.setScene(scene);
        loadCss(scene);
    }

    private void loadCss(Scene scene) {
        this.addCSS(scene, "styles");
        this.addCSS(scene, "language-selector");
        this.addCSS(scene, "hero");
    }

    private void addCSS(Scene scene, String file) {
        var css = scene.getStylesheets();
        var module = this.getClass()
                .getResource("/desktop/styles/" + file + ".css")
                .toExternalForm();
        css.add(module);
    }

    public class Warning {
        public void error(String messageKey) {
            show(messageKey, Alert.AlertType.ERROR);
        }

        public void success(String messageKey) {
            show(messageKey, Alert.AlertType.INFORMATION);
        }

        public void notify(String title, String message) {
            new Notification(title, message);
        }

        private void show(String messageKey, Alert.AlertType type) {
            var alert = new Alert(type);
            alert.setContentText(translator.translationOf(messageKey));
            alert.show();
        }
    }

    public Warning warn() { return new Warning(); }
}
