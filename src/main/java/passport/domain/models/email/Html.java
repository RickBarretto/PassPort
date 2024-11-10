package passport.domain.models.email;

/**
 * Represents an HTML element with tag, content, attributes, and closure
 * information.
 */
public class Html {
    private final String tag;
    private final String content;
    private final String attributes;
    private final boolean closable;

    /**
     * Constructs a new Html element with the specified tag.
     *
     * @param tag the HTML tag
     */
    private Html(String tag) { this(tag, "", "", true); }

    /**
     * Constructs a new Html element with the specified tag, content,
     * attributes, and closure information.
     *
     * @param tag        the HTML tag
     * @param content    the content within the tag
     * @param attributes the attributes of the tag
     * @param closable   whether the tag is closable
     */
    private Html(String tag, String content, String attributes,
            boolean closable) {
        this.tag = tag;
        this.content = content;
        this.attributes = attributes;
        this.closable = closable;
    }

    /**
     * Creates an empty HTML line break element.
     *
     * @return a new Html element representing a line break
     */
    public static Html br() { return new Html("br").empty(); }

    /**
     * Creates an HTML element with the specified tag.
     *
     * @param tag the HTML tag
     * @return a new Html element
     */
    public static Html node(String tag) { return new Html(tag); }

    /**
     * Sets the content of the HTML element.
     *
     * @param content the content to set
     * @return a new Html element with the specified content
     */
    public Html content(String content) {
        assert closable;
        return new Html(tag, content, attributes, true);
    }

    /**
     * Creates an empty HTML element.
     *
     * @return a new Html element that is empty
     */
    public Html empty() {
        assert content.isEmpty();
        return new Html(tag, "", attributes, false);
    }

    /**
     * Sets the attributes of the HTML element.
     *
     * @return a new Html element with the specified attributes
     */
    public Html attributes() {
        return new Html(tag, content, attributes, closable);
    }

    @Override
    public String toString() {
        return new StringBuilder(open())
                .append(content)
                .append(close())
                .toString();
    }

    /**
     * Generates the opening tag of the HTML element.
     *
     * @return the opening tag as a string
     */
    private String open() {
        return new StringBuilder("<")
                .append(tag)
                .append((attributes.isEmpty()) ? "" : " ")
                .append(attributes)
                .append((closable) ? ">" : "/>\n")
                .toString();
    }

    /**
     * Generates the closing tag of the HTML element.
     *
     * @return the closing tag as a string
     */
    private String close() {
        return new StringBuilder("</")
                .append(tag)
                .append(">\n")
                .toString();
    }
}
