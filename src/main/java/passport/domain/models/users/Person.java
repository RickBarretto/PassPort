package passport.domain.models.users;

import java.util.Objects;

/**
 * Represents a person with a name and CPF.
 */
public class Person {
    private final String name;
    private final String cpf;

    /**
     * Constructs a new Person with the specified name and CPF.
     *
     * @param name the name of the person
     * @param cpf  the CPF of the person
     */
    public Person(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    /**
     * Returns the name of the person.
     *
     * @return the name of the person
     */
    public String name() { return name; }

    /**
     * Returns the CPF of the person.
     *
     * @return the CPF of the person
     */
    public String cpf() { return cpf; }

    /**
     * Creates a new Person with the specified name, keeping the current CPF.
     *
     * @param name the new name of the person
     * @return a new Person with the updated name
     */
    public Person withName(String name) {
        return new Person(name, this.cpf);
    }

    /**
     * Creates a new Person with the specified CPF, keeping the current name.
     *
     * @param cpf the new CPF of the person
     * @return a new Person with the updated CPF
     */
    public Person withCpf(String cpf) { return new Person(this.name, cpf); }

    /**
     * Checks if the current person is equal to another person.
     *
     * @param other the other person to compare
     * @return true if the persons are equal, false otherwise
     */
    public boolean equals(Person other) {
        return Objects.equals(name, other.name)
                && Objects.equals(cpf, other.cpf);
    }

    /**
     * Checks if the current object is equal to another object.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person person)
            return this.equals(person);
        return false;
    }

    /**
     * Returns the hash code of the person.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() { return Objects.hash(name, cpf); }
}
