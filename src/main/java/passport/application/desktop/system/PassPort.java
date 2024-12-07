package passport.application.desktop.system;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import passport.application.desktop.Translator;
import passport.application.desktop.ui.components.Notification;
import passport.application.desktop.ui.event.EventPopup;
import passport.application.desktop.ui.main.MainWindow;
import passport.application.desktop.ui.profile.EditProfile;
import passport.application.desktop.ui.purchase.PurchaseWindow;
import passport.application.desktop.ui.welcome.WelcomeWindow;
import passport.domain.models.events.Event;

/**
 * The PassPort class acts as a controller in the MVC architecture. It manages
 * the navigation and interaction between different views and the application's
 * services. The controller is passed to the view (instead of getting the view),
 * allowing the view to invoke controller methods for various actions.
 *
 * @param stage      The primary stage of the application.
 * @param services   The services provided by the application.
 * @param translator The translator for handling multilingual support.
 */
public record PassPort(Stage stage, Services services, Translator translator) {

    /**
     * Constructs a PassPort controller with the given services.
     *
     * @param services The services provided by the application.
     */
    public PassPort(Services services) {
        this(null, services, new Translator());
    }

    /**
     * Returns a new PassPort instance with the specified stage.
     *
     * @param stage The primary stage of the application.
     * @return A new PassPort instance with the specified stage.
     */
    public PassPort withStage(Stage stage) {
        return new PassPort(stage, services, translator);
    }

    /**
     * Gets the current stage of the application.
     *
     * @return The current stage.
     * @throws AssertionError if the stage is null.
     */
    public Stage stage() {
        assert stage != null;
        return stage;
    }

    /**
     * Navigates to the main window.
     */
    public void toMain() {
        toScene(new Scene(new MainWindow(this), 1200, 700));
    }

    /**
     * Opens the purchase window for the specified event.
     *
     * @param event The event to purchase tickets for.
     */
    public void toPurchaseOf(Event event) {
        new PurchaseWindow(
                this,
                event,
                this.services().login().current().get());
    }

    /**
     * Opens the event purchase popup for the specified event.
     *
     * @param event The event to purchase tickets for.
     */
    public void toEventPurchase(Event event) {
        new EventPopup(this, event).forPurchasing();
    }

    /**
     * Opens the event review popup for the specified event.
     *
     * @param event The event to review.
     */
    public void toEventReview(Event event) {
        new EventPopup(this, event).forReviewing();
    }

    /**
     * Opens the profile editing window.
     */
    public void toProfileEditing() { new EditProfile(this); }

    /**
     * Navigates to the welcome window.
     */
    public void toWelcome() {
        toScene(new Scene(new WelcomeWindow(this), 1200, 700));
    }

    /**
     * Sets the specified scene on the current stage.
     *
     * @param scene The scene to set.
     */
    public void toScene(Scene scene) {
        Stage currentStage = (Stage) stage.getScene().getWindow();
        currentStage.setScene(scene);
        loadCss(scene);
    }

    /**
     * Loads the CSS stylesheets for the specified scene.
     *
     * @param scene The scene to load stylesheets for.
     */
    private void loadCss(Scene scene) {
        this.addCSS(scene, "styles");
        this.addCSS(scene, "language-selector");
        this.addCSS(scene, "hero");
    }

    /**
     * Adds a CSS stylesheet to the specified scene.
     *
     * @param scene The scene to add the stylesheet to.
     * @param file  The name of the stylesheet file.
     */
    private void addCSS(Scene scene, String file) {
        var css = scene.getStylesheets();
        var module = this.getClass()
                .getResource("/desktop/styles/" + file + ".css")
                .toExternalForm();
        css.add(module);
    }

    /**
     * The Warning class provides methods for displaying alerts and
     * notifications.
     */
    public class Warning {
        /**
         * Displays an error alert with the specified message key.
         *
         * @param messageKey The key of the message to display.
         */
        public void error(String messageKey) {
            show(messageKey, Alert.AlertType.ERROR);
        }

        /**
         * Displays a success alert with the specified message key.
         *
         * @param messageKey The key of the message to display.
         */
        public void success(String messageKey) {
            show(messageKey, Alert.AlertType.INFORMATION);
        }

        /**
         * Displays a notification with the specified title and message.
         *
         * @param title   The title of the notification.
         * @param message The message of the notification.
         */
        public void notify(String title, String message) {
            new Notification(title, message);
        }

        /**
         * Displays an alert with the specified message key and alert type.
         *
         * @param messageKey The key of the message to display.
         * @param type       The type of the alert.
         */
        private void show(String messageKey, Alert.AlertType type) {
            var alert = new Alert(type);
            alert.setContentText(translator.translationOf(messageKey));
            alert.show();
        }
    }

    /**
     * Gets the Warning instance for displaying alerts and notifications.
     *
     * @return The Warning instance.
     */
    public Warning warn() { return new Warning(); }
}
