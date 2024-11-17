package passport.application.desktop.ui.purchase;

import java.util.Arrays;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;
import passport.application.desktop.system.PassPort;

public class Forms {

    public static VBox creditCardForm(PassPort app,
            PurchaseWindow purchaseWindow, Double ticketPrice) {

        var translator = app.translator();
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // Card Number
        var cardNumber = new TextField();
        inject(form,
                new Label(
                        translator.translationOf("purchase.card.number.label")),
                cardNumber);

        // Expiry Date
        var expiryDate = new TextField();
        expiryDate.setPromptText(
                translator.translationOf("purchase.card.expiry.promp"));
        inject(form,
                new Label(
                        translator.translationOf("purchase.card.expiry.label")),
                expiryDate);

        // CVV
        var cvv = new TextField();
        inject(form,
                new Label(translator.translationOf("purchase.card.cvv.label")),
                cvv);

        // Amount
        var amount = amountSpinner();
        inject(form,
                new Label(translator.translationOf("purchase.amount.label")),
                amount);

        // Submit Button
        var submit = new Button(
                totalPrice(translator, ticketPrice, amount.getValue()));
        inject(form, submit);

        // Set Button Action
        submit.setOnAction(_ -> {
            if (isFormValid(cardNumber, expiryDate, cvv)) {
                purchaseWindow.purchase(
                        translator.translationOf("purchase.credit-card"),
                        cardNumber.getText(),
                        amount.getValue());
            }
            else {
                warnEmptyFields(app);
            }
        });

        // Update Button Text on Amount Change
        amount.setOnMouseReleased((_) -> submit.setText(
                totalPrice(translator, ticketPrice, amount.getValue())));

        return form;
    }

    public static VBox pixForm(PassPort app,
            PurchaseWindow purchaseWindow, Double price) {
        var translator = app.translator();

        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // PIX Key
        var pixKey = new TextField();
        inject(form,
                new Label(translator.translationOf("purchase.pix.key.label")),
                pixKey);

        // Amount
        var amount = amountSpinner();
        inject(form,
                new Label(translator.translationOf("purchase.amount.label")),
                amount);

        // Submit Button
        var submit = new Button(
                totalPrice(translator, price, amount.getValue()));
        inject(form, submit);

        // Set Button Action
        submit.setOnAction(_ -> {
            if (isFormValid(pixKey)) {
                purchaseWindow.purchase(
                        "PIX",
                        pixKey.getText(),
                        amount.getValue());
            }
            else {
                warnEmptyFields(app);
            }
        });

        // Update Button Text on Amount Change
        amount.setOnMouseReleased((_) -> submit
                .setText(totalPrice(translator, price, amount.getValue())));

        return form;
    }

    private static void warnEmptyFields(PassPort app) {
        app.warn().error("purchase.error.empty-fields");
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

    private static Spinner<Integer> amountSpinner() {
        var amountSpinner = new Spinner<Integer>();
        amountSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        return amountSpinner;
    }

    private static boolean isFormValid(TextField... fields) {
        return Arrays.stream(fields)
                .noneMatch(field -> field.getText().isEmpty());
    }
}
