package passport.application.desktop.ui.purchase;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import passport.application.desktop.system.PassPort;
import passport.domain.exceptions.PurchaseForInactiveEvent;
import passport.domain.exceptions.SoldOut;
import passport.domain.models.events.Event;
import passport.domain.models.events.EventId;
import passport.domain.models.purchases.PaymentMethod;
import passport.domain.models.users.User;
import passport.domain.models.users.UserId;

public class PurchaseWindow {
    private final PassPort app;
    private final Properties props;
    private final Components ui;
    private final Stage stage;

    public record Properties(EventId eventId, UserId userId, Double price) {}

    public PurchaseWindow(PassPort app, Event event, User user) {
        this.app = app;
        this.props = new Properties(event.id(), user.id(),
                event.boxOffice().ticket().price());
        this.ui = new Components(app.translator(), this, props.price);
        this.stage = this.newStage(ui.getRoot());
        this.stage.show();
    }

    private Stage newStage(Region root) {
        Scene scene = new Scene(root, 800, 600);
        var stage = this.newStageFromCurrent();
        stage.setScene(scene);
        return stage;
    }

    private Stage newStageFromCurrent() {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(app.stage());
        return newStage;
    }

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
}