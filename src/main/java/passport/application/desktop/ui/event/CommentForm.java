package passport.application.desktop.ui.event;

import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class CommentForm extends VBox {
    private final TextArea comment;

    public CommentForm(Consumer<String> onSubmit) {
        this.setSpacing(10);

        this.comment = commentField();
        Button submit = submit(onSubmit);

        this.getChildren().add(comment);
        this.getChildren().add(submit);
    }

    private TextArea commentField() {
        TextArea area = new TextArea();
        area.setPromptText("Comment here...");
        area.setPrefRowCount(1);
        area.setWrapText(true);
        return area;
    }

    private Button submit(Consumer<String> onSubmit) {
        Button button = new Button("Comment");
        button.setOnAction(e -> handleSubmit(onSubmit));
        return button;
    }

    private void handleSubmit(Consumer<String> onSubmit) {
        if (comment.getText().isEmpty()) {
            showWarning();
            return;
        }

        onSubmit.accept(comment.getText());
        comment.clear();
    }

    private void showWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please fill the comment!");
        alert.showAndWait();
    }
}