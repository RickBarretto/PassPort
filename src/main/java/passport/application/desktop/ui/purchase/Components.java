package passport.application.desktop.ui.purchase;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;

public class Components {
    private final PurchaseWindow purchaseWindow;
    private final Double price;
    private final VBox root;

    public Components(PassPort app, PurchaseWindow purchaseWindow,
            Double price) {
        this.purchaseWindow = purchaseWindow;
        this.price = price;
        this.root = layout(app);
    }

    public VBox root() { return root; }

    private VBox layout(PassPort app) {
        var root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().add(createPaymentTabs(app));
        return root;
    }

    private TabPane createPaymentTabs(PassPort app) {
        var translator = app.translator();
        var tabPane = new TabPane();

        var creditCard = new Tab(
                translator.translationOf("purchase.credit-card"));
        creditCard.setContent(Forms.creditCardForm(app, purchaseWindow, price));
        creditCard.setClosable(false);

        var pix = new Tab("PIX");
        pix.setContent(Forms.pixForm(app, this.purchaseWindow, this.price));
        pix.setClosable(false);

        tabPane.getTabs().addAll(creditCard, pix);
        return tabPane;
    }
}
