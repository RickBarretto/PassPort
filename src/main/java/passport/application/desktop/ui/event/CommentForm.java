package passport.application.desktop.ui.event;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;

public class CommentForm extends VBox {
    private final PassPort app;
    private final TextArea comment;

    public CommentForm(PassPort app, Consumer<String> onSubmit) {
        this.app = app;

        this.setSpacing(10);

        this.comment = commentField();
        Button submit = submit(onSubmit);

        this.getChildren().add(comment);
        this.getChildren().add(submit);
    }

    private TextArea commentField() {
        TextArea area = new TextArea();
        area.setPromptText(
                app.translator().translationOf("events.comment-here"));
        area.setAccessibleText(
                app.translator().translationOf("events.comment-here"));
        area.setPrefRowCount(1);
        area.setWrapText(true);
        return area;
    }

    private Button submit(Consumer<String> onSubmit) {
        Button button = new Button("Comment");
        button.setAccessibleText("Comment");
        button.setOnAction(_ -> handleSubmit(onSubmit));
        return button;
    }

    private void handleSubmit(Consumer<String> onSubmit) {
        if (comment.getText().isEmpty()) {
            app.warn().error("events.warn.empty-comment");
            return;
        }

        onSubmit.accept(comment.getText());
        comment.clear();
    }
}