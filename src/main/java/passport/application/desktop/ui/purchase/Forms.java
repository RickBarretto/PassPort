package passport.application.desktop.ui.purchase;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Forms {

    public static VBox createCreditCardForm(PurchaseWindow purchaseWindow) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // Card Number
        var cardNumber = new TextField();
        cardNumber.setPromptText("Enter your card number");
        inject(form, new Label("Card Number:"), cardNumber);

        // Expiry Date
        var expiryDate = new TextField();
        expiryDate.setPromptText("MM/YY");
        inject(form, new Label("Expiry Date:"), expiryDate);

        // CVV
        var cvv = new TextField();
        cvv.setPromptText("Enter CVV");
        inject(form, new Label("CVV:"), cvv);

        // Amount
        var amount = createAmountSpinner();
        inject(form, new Label("Amount:"), amount);

        // Submit Button
        var submit = new Button("Purchase");
        submit.setOnAction(_ -> purchaseWindow.purchase("Credit Card",
                cardNumber.getText(), amount.getValue()));
        inject(form, submit);

        return form;
    }

    public static VBox createPixForm(PurchaseWindow purchaseWindow) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // PIX Key
        var pixKey = new TextField();
        pixKey.setPromptText("Enter your PIX key");
        inject(form, new Label("PIX Key:"), pixKey);

        // Amount
        var amount = createAmountSpinner();
        inject(form, new Label("Amount:"), amount);

        // Submit Button
        var submit = new Button("Purchase");
        submit.setOnAction(_ -> purchaseWindow.purchase("PIX", pixKey.getText(),
                amount.getValue()));
        inject(form, submit);

        return form;
    }

    private static void inject(VBox parent, Region child) {
        parent.getChildren().add(child);
    }

    private static void inject(VBox parent, Region a, Region b) {
        parent.getChildren().addAll(a, b);
    }

    private static Spinner<Integer> createAmountSpinner() {
        var amountSpinner = new Spinner<Integer>();
        amountSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        return amountSpinner;
    }
}
