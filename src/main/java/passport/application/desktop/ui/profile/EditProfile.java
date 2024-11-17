package passport.application.desktop.ui.profile;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import passport.application.desktop.contracts.Action;
import passport.application.desktop.system.PassPort;

public class EditProfile {
    final PassPort app;
    final Components ui;

    class Components {
        // New Attributes
        TextField email = new TextField();
        PasswordField password = new PasswordField();
        TextField name = new TextField();
        TextField cpf = new TextField();

        // Confirmation
        PasswordField confirmation = new PasswordField();
        Button save = new Button();

        public Components(Action saveOnClick) {
            confirmation.setMaxWidth(250);
            confirmation.setPrefWidth(250);

            save.setMaxWidth(250);
            save.setPrefWidth(250);
            this.save.getStyleClass().add("accent");
            this.save.setOnAction((_) -> saveOnClick.exec());
            translate();
        }

        private void translate() {
            app.translator().translateFrom(save::setText, "profile.save");
        }
    }

    public EditProfile(PassPort app) {
        this.app = app;
        this.ui = new Components(this::save);
        setup();
    }

    private void save() { System.out.println("Saved!"); }

    private void setup() {
        var top = $("profile.edit");

        var middle = new HBox(5,
                new VBox(10,
                        $("logon.email"), ui.email,
                        $("logon.password"), ui.password),
                vsplit(),
                new VBox(10,
                        $("logon.fullname"), ui.name,
                        $("logon.cpf"), ui.cpf));

        var bottom = new VBox(10,
                $("profile.current-password"),
                ui.confirmation,
                ui.save);

        var main = new VBox(20, top, middle, bottom);

        top.getStyleClass().add("title-1");
        middle.setAlignment(Pos.CENTER);
        bottom.setFillWidth(true);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        main.setPadding(new Insets(20));
        main.setAlignment(Pos.CENTER);

        openStage(main);
    }

    private Label $(String key) {
        return new Label(app.translator().translationOf(key));
    }

    private Separator vsplit() {
        var widget = new Separator();
        widget.setOrientation(Orientation.VERTICAL);
        return widget;
    }

    void openStage(Region root) {
        var editProfile = new Stage();
        editProfile.initModality(Modality.APPLICATION_MODAL);
        editProfile.initOwner(app.stage());
        editProfile.setTitle(app.translator().translationOf("profile.edit"));
        editProfile.setScene(new Scene(root, 500, 400));
        editProfile.show();
    }
}
