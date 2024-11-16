package passport.application.desktop.ui.purchase;

import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Components {
    private final PurchaseWindow purchaseWindow;
    private final VBox root;

    public Components(PurchaseWindow purchaseWindow) {
        this.purchaseWindow = purchaseWindow;
        this.root = createRoot();
    }

    public VBox getRoot() { return root; }

    private VBox createRoot() {
        var root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getChildren().add(createPaymentTabs());
        return root;
    }

    private TabPane createPaymentTabs() {
        var tabPane = new TabPane();

        var creditCardTab = new Tab("Credit Card");
        creditCardTab
                .setContent(Forms.createCreditCardForm(purchaseWindow));
        creditCardTab.setClosable(false);

        var pixTab = new Tab("PIX");
        pixTab.setContent(Forms.createPixForm(purchaseWindow));
        pixTab.setClosable(false);

        tabPane.getTabs().addAll(creditCardTab, pixTab);

        return tabPane;
    }
}
