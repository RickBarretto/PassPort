package passport.application.desktop.ui.event;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;

public class CommentForm extends VBox {
    private final TextField username;
    private final TextArea comment;

    public CommentForm(BiConsumer<String, String> onSubmit) {
        this.setSpacing(10);

        this.username = usernameField();
        this.comment = commentField();
        Button submit = submit(onSubmit);

        this.getChildren().add(username);
        this.getChildren().add(comment);
        this.getChildren().add(submit);
    }

    private TextField usernameField() {
        TextField field = new TextField();
        field.setPromptText("Your username");
        return field;
    }

    private TextArea commentField() {
        TextArea area = new TextArea();
        area.setPromptText("Comment here...");
        area.setPrefRowCount(1);
        area.setWrapText(true);
        return area;
    }

    private Button submit(BiConsumer<String, String> onSubmit) {
        Button button = new Button("Comment");
        button.setOnAction(e -> handleSubmit(onSubmit));
        return button;
    }

    private void handleSubmit(BiConsumer<String, String> onSubmit) {
        if (!username.getText().isEmpty()
                && !comment.getText().isEmpty()) {
            onSubmit.accept(username.getText(), comment.getText());
            clearFields();
        }
        else {
            showWarning();
        }
    }

    private void clearFields() {
        username.clear();
        comment.clear();
    }

    private void showWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in both username and comment!");
        alert.showAndWait();
    }
}