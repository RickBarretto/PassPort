package passport.application.desktop.ui.purchase;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import passport.application.desktop.Translator;
import passport.application.desktop.system.PassPort;

public class Components {
    private final PurchaseWindow purchaseWindow;
    private final Double price;
    private final VBox root;

    public Components(PassPort app, PurchaseWindow purchaseWindow,
            Double price) {
        this.purchaseWindow = purchaseWindow;
        this.price = price;
        this.root = createRoot(app);
    }

    public VBox getRoot() { return root; }

    private VBox createRoot(PassPort app) {
        var root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().add(createPaymentTabs(app));
        return root;
    }

    private TabPane createPaymentTabs(PassPort app) {
        var translator = app.translator();
        var tabPane = new TabPane();

        var creditCardTab = new Tab(
                translator.translationOf("purchase.credit-card"));
        creditCardTab.setContent(Forms.createCreditCardForm(
                app, purchaseWindow, price));
        creditCardTab.setClosable(false);

        var pixTab = new Tab("PIX");
        pixTab.setContent(Forms.createPixForm(app, this.purchaseWindow,
                this.price));
        pixTab.setClosable(false);

        tabPane.getTabs().addAll(creditCardTab, pixTab);

        return tabPane;
    }
}
