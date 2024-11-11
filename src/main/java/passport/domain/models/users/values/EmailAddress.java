package passport.domain.models.users.values;

public record EmailAddress(String value) {

    public boolean equals(String value) {
        return this.value.equals(value);
    }

}
