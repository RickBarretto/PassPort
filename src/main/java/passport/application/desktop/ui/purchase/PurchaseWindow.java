package passport.application.desktop.ui.purchase;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import passport.application.desktop.system.PassPort;
import passport.domain.exceptions.PurchaseForInactiveEvent;
import passport.domain.exceptions.SoldOut;
import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;

/**
 * Manages the purchase window for event tickets.
 */
public class PurchaseWindow {
    private final PassPort app;
    private final Properties props;
    private final Components ui;
    private final Stage stage;

    /**
     * Holds properties related to the purchase, including event and user IDs,
     * and ticket price.
     */
    public record Properties(EventId eventId, UserId userId, Double price) {}

    /**
     * Initializes the purchase window with the given event and user.
     *
     * @param app   The PassPort application instance.
     * @param event The event for which tickets are being purchased.
     * @param user  The user making the purchase.
     */
    public PurchaseWindow(PassPort app, Event event, User user) {
        this.app = app;
        this.props = new Properties(event.id(), user.id(),
                event.boxOffice().ticket().price());
        this.ui = new Components(app, this, props.price);
        this.stage = this.newStage(ui.root());
        this.stage.show();
    }

    /**
     * Creates a new stage for the purchase window. Also allow the user to close
     * the window, when ESC is pressed.
     *
     * @param root The root region of the scene.
     * @return The newly created stage.
     */
    private Stage newStage(Region root) {
        Scene scene = new Scene(root, 800, 600);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE)
                close();
        });
        var stage = this.newStageFromCurrent();
        stage.setScene(scene);
        return stage;
    }

    /**
     * * Creates a new stage with modal properties. Using
     * {@link Modality#APPLICATION_MODAL} ensures the user must interact with
     * this window before returning to the parent application window.
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
     * Processes the purchase of tickets.
     *
     * @param paymentMethod The payment method used for the purchase.
     * @param details       Additional details related to the payment method.
     * @param amount        The number of tickets to purchase.
     */
    void purchase(String paymentMethod, String details, int amount) {
        var translator = app.translator();
        try {
            app.services().purchasing()
                    .of(props.eventId())
                    .by(props.userId())
                    .via(new PaymentMethod(paymentMethod, details))
                    .buy(amount);

            app.warn().notify(translator.translationOf(
                    "purchase.success.title"),
                    String.format(
                            translator.translationOf("purchase.success.msg"),
                            amount * props.price));
            stage.close();

        }
        catch (SoldOut e) {
            app.warn().error("purchase.soldout");
        }
        catch (PurchaseForInactiveEvent e) {
            System.err
                    .println("User should not access this for Sold Out events");
            e.printStackTrace();
        }
    }

    /**
     * Closes the purchase window.
     */
    void close() { stage.close(); }
}
