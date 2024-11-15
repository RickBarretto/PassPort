package passport.application.desktop.ui.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

class DrawerButton extends Button {
    // private final PassPort app;
    private final String iconName;
    private final String propertyName;

    public DrawerButton(String iconName, String propertyName) {
        // this.app = app;
        this.iconName = iconName;
        this.propertyName = propertyName;
        setup();
    }

    public DrawerButton selected() {
        this.getStyleClass().add("accent");
        return this;
    }

    private void setup() {
        VBox buttonContent = new VBox(5);
        buttonContent.setAlignment(Pos.CENTER);

        try {
            putIcon(buttonContent);
        }
        catch (Exception e) {
            System.err.println("Could not load icon: " + iconName);
            this.putPlaceholderIcon(buttonContent);
        }

        this.putLabel(buttonContent);
        this.setGraphic(buttonContent);
        this.setPrefWidth(200);
    }

    private void putLabel(VBox buttonContent) {
        Label label = new Label(propertyName);
        buttonContent.getChildren().add(label);
    }

    private void putIcon(VBox buttonContent) {
        ImageView icon = loadIcon();
        this.setIconTo24px(icon);
        buttonContent.getChildren().add(icon);
    }

    private void putPlaceholderIcon(VBox buttonContent) {
        Label placeholder = new Label("•");
        buttonContent.getChildren().add(placeholder);
    }

    private void setIconTo24px(ImageView icon) {
        icon.setFitHeight(24);
        icon.setFitWidth(24);
    }

    private ImageView loadIcon() {
        ImageView icon = new ImageView(
                new Image(getClass().getResourceAsStream(
                        "/desktop/icons/" + iconName)));
        return icon;
    }
}

public class Drawer extends VBox {
    private final Components ui;

    class Components {
        final DrawerButton profile = new DrawerButton("profile.png", "Profile");
        final DrawerButton events = new DrawerButton("event.png", "Events")
                .selected();
        final DrawerButton tickets = new DrawerButton("ticket.png",
                "My Events");
    }

    public Drawer() {
        this.ui = new Components();
        this.setup();
    }

    private void setup() {
        this.setupLayout();
        this.getChildren().addAll(
                ui.profile,
                ui.events,
                ui.tickets);
    }

    private void setupLayout() {
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(150);
        this.setSpacing(10);
        this.setPadding(new Insets(0, 20, 0, 20));
    }

    public Button getButtonByIndex(int index) {
        return (Button) this.getChildren().get(index);
    }
}