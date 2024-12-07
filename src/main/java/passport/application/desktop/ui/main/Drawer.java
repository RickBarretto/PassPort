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

/**
 * Custom button used in the drawer, with an icon and a description.
 */
class DrawerButton extends Button {
    private final Components ui;

    /**
     * Holds UI components for the drawer button.
     */
    class Components {
        final VBox content = new VBox(5);
        final Label description = new Label();
        final Node icon;

        /**
         * Initializes the components for the drawer button.
         *
         * @param iconName The name of the icon.
         */
        public Components(String iconName) {
            this.icon = icon(iconName);
            setupLayout();
        }

        /**
         * If the ".png" file of the provided icon is not found, this will be
         * replaced by the Label: "•".
         * 
         * @param name icon name
         * @return The icon as an ImageView or Label.
         */
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

    /**
     * Constructs a drawer button with the given icon and description.
     *
     * @param icon        The name of the icon file.
     * @param description The description text.
     */
    public DrawerButton(String icon, String description) {
        this.ui = new Components(icon);
        this.setGraphic(ui.content);
        this.setPrefWidth(200);
    }

    /**
     * Sets the description text.
     *
     * @param description The description text to set.
     */
    public void setDescription(String description) {
        ui.description.setText(description);
    }

    /**
     * Marks the button as selected.
     *
     * @return The updated DrawerButton instance.
     */
    public DrawerButton selected() {
        this.getStyleClass().add("accent");
        return this;
    }
}

/**
 * Represents the drawer in the main window, providing navigation buttons.
 */
public class Drawer extends VBox {
    private final PassPort app;
    private final Components ui;
    private final Action openAllEvents;
    private final Action openMyEvents;

    /**
     * Holds UI components for the drawer.
     */
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
                openMyEvents.exec();
            });
            return btn;
        }
    }

    /**
     * Constructs the drawer with the given application instance and actions.
     *
     * @param app           The PassPort application instance.
     * @param openAllEvents Action to open the available events view.
     * @param openMyEvents  Action to open the owned events view.
     */
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
                .translateFrom(ui.profile::setDescription, "main.drawer.profile")
                .translateFrom(ui.profile::setAccessibleText, "main.drawer.profile")
                .translateFrom(ui.events::setDescription, "main.drawer.events")
                .translateFrom(ui.events::setAccessibleText, "main.drawer.events")
                .translateFrom(ui.tickets::setDescription, "main.drawer.tickets")
                .translateFrom(ui.tickets::setAccessibleText, "main.drawer.tickets")
                .resourcesProp().addListener((_, _, _) -> translate());
    }
}
