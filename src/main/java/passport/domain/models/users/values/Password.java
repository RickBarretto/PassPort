package passport.domain.models.users.values;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

class Sha256 {
    private String value;

    public Sha256(String value) { this.value = hash(value); }

    public String value() { return value; }

    public String hash(String value) {
        try {
            final var valueBytes = value.getBytes();
            final var digest = MessageDigest.getInstance("SHA-256");

            return Base64
                    .getEncoder()
                    .encodeToString(digest.digest(valueBytes));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    public boolean equals(String value) { return this.value.equals(value); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Sha256))
            return false;
        Sha256 other = (Sha256) obj;
        return Objects.equals(value, other.value);
    }
}

public class Password {
    private Sha256 value;

    public Password(String value) { this.value = new Sha256(value); }

    @Override
    public int hashCode() { return Objects.hash(value); }

    public boolean equals(String value) { return this.value.equals(value); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Password))
            return false;
        return equals((Password) obj);
    }

    private boolean equals(Password other) {
        return Objects.equals(value, other.value);
    }

}
