package passport.application.desktop.ui.event;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import passport.application.desktop.system.PassPort;
import passport.domain.contexts.events.EvaluationListing.Comment;
import passport.domain.models.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Comments extends VBox {
    private final PassPort app;
    private final Event event;
    private final List<Comment> commentsData = new ArrayList<>();
    private final ListView<Comment> comments;
    private final CommentForm form;

    public Comments(PassPort app, Event event, Consumer<String> addComment) {
        super(10);

        this.app = app;
        this.event = event;
        Label header = header(app);
        this.comments = comments();
        this.form = new CommentForm(app, addComment);

        this.getChildren().addAll(
                header,
                comments);

        this.loadCommentsFromInfra();
        this.update();
    }

    public Comments withForms() {
        this.getChildren().addAll(
                new Separator(),
                form);
        return this;
    }

    private Label header(PassPort app) {
        Label header = new Label(
                app.translator().translationOf("events.comments"));
        header.setAccessibleText(
                app.translator().translationOf("events.comments"));
        header.getStyleClass().add("title-2");
        return header;
    }

    private void loadCommentsFromInfra() {
        var data = app.services().evaluationListing().of(event);
        commentsData.addAll(data);
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
