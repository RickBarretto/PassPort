package passport.application.desktop.ui.event;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Comments extends VBox {
    private final List<Comment> commentsData = new ArrayList<>();
    private final ListView<Comment> comments;
    private final CommentForm form;

    public Comments(PassPort app, Consumer<String> addComment) {
        super(10);

        Label header = header();
        this.comments = comments();
        this.form = new CommentForm(addComment);

        getChildren().addAll(
                header,
                comments);
    }

    public Comments withForms() {
        this.getChildren().addAll(
            new Separator(),
            form
        );
        return this;
    }

    private Label header() {
        Label header = new Label("Comments");
        header.getStyleClass().add("title-2");
        return header;
    }

    private ListView<Comment> comments() {
        ListView<Comment> list = new ListView<>();
        list.setPrefHeight(150);
        return list;
    }

    public void add(String username, String content) {
        commentsData.add(new Comment(username, content));
        update();
    }

    private void update() {
        comments.getItems().clear();
        comments.getItems().addAll(commentsData);
    }
}
