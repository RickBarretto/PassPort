package passport.application.desktop.ui.event;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class Comments extends VBox {
    private final List<Comment> comments = new ArrayList<>();
    private final ListView<Comment> commentsList;
    private final CommentForm commentForm;

    public Comments() {
        super(10);

        Label header = createHeader();
        this.commentsList = createCommentsList();
        this.commentForm = new CommentForm(this::addComment);

        getChildren().addAll(
                header,
                commentsList,
                new Separator(),
                commentForm);
    }

    private Label createHeader() {
        Label header = new Label("Comments");
        header.getStyleClass().add("title-2");
        return header;
    }

    private ListView<Comment> createCommentsList() {
        ListView<Comment> list = new ListView<>();
        list.setPrefHeight(150);
        return list;
    }

    public void addComment(String username, String content) {
        comments.add(new Comment(username, content));
        updateCommentsList();
    }

    private void updateCommentsList() {
        commentsList.getItems().clear();
        commentsList.getItems().addAll(comments);
    }
}
