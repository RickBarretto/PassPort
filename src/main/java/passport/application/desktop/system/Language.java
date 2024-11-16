package passport.application.desktop.system;

public enum Language {
    ENGLISH("en", "English"),
    PORTUGUESE("pt", "Português"),
    SPANISH("es", "Español"),
    JAPANESE("ja", "日本語");

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String code() { return code; }

    @Override
    public String toString() { return displayName; }
}