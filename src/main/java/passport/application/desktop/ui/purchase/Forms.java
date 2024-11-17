package passport.application.desktop.ui.purchase;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;

public class Forms {

    public static VBox createCreditCardForm(Translator translator,
            PurchaseWindow purchaseWindow, Double ticketPrice) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // Card Number
        var cardNumber = new TextField();
        inject(form, new Label(
                translator.translationOf("purchase.card.number.label")),
                cardNumber);

        // Expiry Date
        var expiryDate = new TextField();
        expiryDate.setPromptText(
                translator.translationOf("purchase.card.expiry.promp"));
        inject(form, new Label(translator
                .translationOf("purchase.card.expiry.label")), expiryDate);

        // CVV
        var cvv = new TextField();
        inject(form, new Label(
                translator.translationOf("purchase.card.cvv.label")), cvv);

        // Amount
        var amount = createAmountSpinner();
        inject(form, new Label(
                translator.translationOf("purchase.amount.label")), amount);

        // Submit Button
        var submit = new Button(totalPrice(translator, ticketPrice, amount.getValue()));
        inject(form, submit);

        submit.setOnAction(_ -> purchaseWindow.purchase(
                translator.translationOf("purchase.credit-card"),
                cardNumber.getText(),
                amount.getValue()));

        amount.setOnMouseReleased((_) -> submit
                .setText(totalPrice(translator, ticketPrice, amount.getValue())));

        return form;
    }

    public static VBox createPixForm(Translator translator,
            PurchaseWindow purchaseWindow, Double price) {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // PIX Key
        var pixKey = new TextField();
        inject(form, new Label(
                translator.translationOf("purchase.pix.key.label")), pixKey);

        // Amount
        var amount = createAmountSpinner();
        inject(form, new Label(
                translator.translationOf("purchase.amount.label")), amount);

        // Submit Button
        var submit = new Button(totalPrice(translator, price, amount.getValue()));
        inject(form, submit);

        submit.setOnAction(_ -> purchaseWindow.purchase("PIX", pixKey.getText(),
                amount.getValue()));
        amount.setOnMouseReleased((_) -> submit
                .setText(totalPrice(translator, price, amount.getValue())));

        return form;
    }

    private static String totalPrice(Translator translator, Double price,
            Integer amount) {
        return translator.translationOf("purchase.submit")
                + String.format(": R$%.2f", price * amount);
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