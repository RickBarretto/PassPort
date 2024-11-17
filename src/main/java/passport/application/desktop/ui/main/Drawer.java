package passport.application.desktop.ui.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

class DrawerButton extends Button {
    private final Components ui;
    private final String icon;

    class Components {
        final VBox content = new VBox(5);
        final Label description = new Label();
        final Node icon;

        public Components(String iconName) {
            this.icon = icon(iconName);
            setupLayout();
        }

        private Node icon(String name) {
            try {
                var icon = loadImageIcon(name);
                icon.setFitHeight(24);
                icon.setFitWidth(24);
                return icon;
            }
            catch (Exception e) {
                e.printStackTrace();
                return new Label("•");
            }
        }

        private ImageView loadImageIcon(String name) {
            return new ImageView(
                    new Image(getClass().getResourceAsStream(
                            "/desktop/icons/" + name)));
        }

        private void setupLayout() {
            content.setAlignment(Pos.CENTER);
            content.getChildren().add(icon);
            content.getChildren().add(description);
        }
    }

    public DrawerButton(String icon, String description) {
        this.ui = new Components(icon);
        this.icon = icon;
        this.setGraphic(ui.content);
        this.setPrefWidth(200);
    }

    public void setDescription(String description) {
        ui.description.setText(description);
    }

    public DrawerButton selected() {
        this.getStyleClass().add("accent");
        return this;
    }
}

public class Drawer extends VBox {
    private final PassPort app;
    private final Components ui;
    private final Action openAllEvents;
    private final Action openMyEvents;

    class Components {
        final DrawerButton profile = profile();
        final DrawerButton events = events();
        final DrawerButton tickets = tickets();

        private DrawerButton profile() {
            var btn = new DrawerButton("profile.png", "Profile");
            btn.setOnAction((_) -> app.toProfileEditing());
            return btn;
        }

        private DrawerButton events() {
            var btn = new DrawerButton("event.png", "Events")
                    .selected();
            btn.setOnAction((_) -> {
                this.tickets.getStyleClass().remove("accent");
                btn.getStyleClass().add("accent");
                openAllEvents.exec();
            });
            return btn;
        }

        private DrawerButton tickets() {
            var btn = new DrawerButton("ticket.png", "My Events");
            btn.setOnAction((_) -> {
                this.events.getStyleClass().remove("accent");
                btn.getStyleClass().add("accent");
                openMyEvents.exec();});
            return btn;
        }
    }

    public Drawer(PassPort app, Action openAllEvents, Action openMyEvents) {
        this.app = app;
        this.ui = new Components();
        this.openAllEvents = openAllEvents;
        this.openMyEvents = openMyEvents;
        this.setup();
        this.translate();
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
        this.setMaxWidth(150);
        this.setSpacing(10);
        this.setPadding(new Insets(0, 20, 0, 20));
    }

    private void translate() {
        app.translator()
                .translateFrom(ui.profile::setDescription,
                        "main.drawer.profile")
                .translateFrom(ui.events::setDescription, "main.drawer.events")
                .translateFrom(ui.tickets::setDescription,
                        "main.drawer.tickets")
                .resourcesProp().addListener((_, _, _) -> translate());
    }
}