package passport.application.desktop.ui.event;

public class Comment {
    private final String username;
    private final String content;

    public Comment(String username, String content) {
        this.username = username;
        this.content = content;
    }

    @Override
    public String toString() { return username + ": " + content; }
}