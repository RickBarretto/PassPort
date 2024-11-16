package passport.application.desktop.ui.purchase;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    record Properties(EventId eventId, UserId userId, Double price) {}

    class Components {
        final VBox root;

        public Components() { this.root = root(); }

        private VBox root() {
            var root = new VBox(20);
            root.setPadding(new Insets(20));
            root.getChildren().add(paymentTabs());

            return root;
        }

        private TabPane paymentTabs() {
            var tabPane = new TabPane();

            var creditCardTab = new Tab("Credit Card");
            creditCardTab.setContent(creditCardForm());
            creditCardTab.setClosable(false);

            var pixTab = new Tab("PIX");
            pixTab.setContent(pixForm());
            pixTab.setClosable(false);

            tabPane.getTabs().addAll(creditCardTab, pixTab);

            return tabPane;
        }

        private void inject(VBox box, Region child) {
            box.getChildren().add(child);
        }

        private VBox creditCardForm() {
            // Form Setup
            VBox form = new VBox(10);
            form.setPadding(new Insets(10));

            // Card's number
            var number = new TextField();
            inject(form, new Label("Card Number:"));
            inject(form, number);

            // Card's expiry
            var expiryDate = new TextField();
            expiryDate.setPromptText("MM/YY");

            inject(form, new Label("Expiry Date:"));
            inject(form, expiryDate);

            // Card's CVV
            var cvv = new TextField();
            cvv.setPromptText("Enter CVV");

            inject(form, new Label("CVV:"));
            inject(form, cvv);

            // Amount
            var amount = amountSpinner();

            inject(form, new Label("Amount:"));
            inject(form, amount);

            // Submit
            var submit = new Button("Purchase");
            submit.setOnAction(
                    _ -> purchase("Credit Card",
                            number.getText(),
                            amount.getValue()));

            inject(form, submit);

            return form;
        }

        private VBox pixForm() {
            var form = new VBox(10);
            form.setPadding(new Insets(10));

            // PIX's Key
            var pixKey = new TextField();

            inject(form, new Label("PIX Key:"));
            inject(form, pixKey);

            // Amount
            var amount = amountSpinner();

            inject(form, new Label("Amount:"));
            inject(form, amount);

            // Submit
            Button submit = new Button("Purchase");
            submit.setOnAction(
                    e -> purchase("PIX",
                            pixKey.getText(), amount.getValue()));

            inject(form, submit);

            return form;
        }

        private Spinner<Integer> amountSpinner() {
            var amountSpinner = new Spinner<Integer>();
            amountSpinner.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
                            10, 1));
            return amountSpinner;
        }
    }

    public PurchaseWindow(PassPort app, Event event, User user) {
        this.app = app;
        this.props = new Properties(event.id(), user.id(),
                event.boxOffice().ticket().price());
        this.ui = new Components();
        this.setupStage(ui.root);
    }

    private void setupStage(Region root) {
        Scene scene = new Scene(root, 800, 600);
        var stage = this.newStageFromCurrent();
        // stage.setTitle(app.translator().translationOf("events.title"));
        stage.setScene(scene);
        stage.show();
    }

    private Stage newStageFromCurrent() {
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(app.stage());
        return newStage;
    }

    private void purchase(
            String paymentMethod,
            String details,
            int amount) {
        try {
            app.services().purchasing()
                    .of(props.eventId())
                    .by(props.userId())
                    .via(new PaymentMethod(paymentMethod, details))
                    .buy(amount);
            app.warn().error("purchase.success");
        }
        catch (SoldOut | PurchaseForInactiveEvent e) {
            System.err
                    .println(
                            "User should not access this for Inactive events");
            System.err
                    .println(
                            "User should not access this for Sold Out events");
            e.printStackTrace();
        }
    }

}
