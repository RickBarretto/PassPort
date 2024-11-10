package passport.domain.contexts.user.forms;

import java.util.Objects;

import passport.domain.models.users.Person;

/**
 * Represents the personal information for a user, including name and CPF.
 */
public class PersonalInformation {
    private String name;
    private String cpf;

    /**
     * Sets the name for the personal information.
     *
     * @param name the user's name
     * @return the updated PersonalInformation object
     */
    public PersonalInformation name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the CPF for the personal information.
     *
     * @param cpf the user's CPF
     * @return the updated PersonalInformation object
     */
    public PersonalInformation cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    /**
     * Submits the personal information and returns a Person object.
     *
     * @return a new Person object with the provided name and CPF
     * @throws NullPointerException if the name or CPF is null
     */
    public Person submit() {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(cpf, "CPF cannot be null");
        return new Person(name, cpf);
    }
}
