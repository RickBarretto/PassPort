package passport.application.desktop.system;

/**
 * Represents the supported languages for the PassPort application.
 */
public enum Language {
    ENGLISH("en", "English"),
    PORTUGUESE("pt", "Português"),
    SPANISH("es", "Español"),
    JAPANESE("ja", "日本語");

    private final String code;
    private final String displayName;

    /**
     * Constructs a Language enum with the specified code and display name.
     *
     * @param code        The language code.
     * @param displayName The display name of the language.
     */
    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    /**
     * Gets the language code.
     *
     * @return The language code.
     */
    public String code() { return code; }

    /**
     * Returns the display name of the language.
     *
     * @return The display name of the language.
     */
    @Override
    public String toString() { return displayName; }
}
