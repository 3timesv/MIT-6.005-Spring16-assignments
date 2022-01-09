package expressivo;

public class Variable implements Expression {
    private final String name;

    // Abstraction function:
    //      Represents a variable in expression
    // Rep invariant:
    //      name.length() > 0
    // Safety from rep exposure:
    //      field is private and immutable.

    /**
     * Creates an instance of Variable.
     *
     * @param name the name of the variable
     */
    public Variable(String name) {
        this.name = name;

        checkRep();
    }

    private void checkRep() {
        assert name.length() > 0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Variable)) {
            return false;
        }
        Variable v = (Variable) thatObject;
        return name.equals(v.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Expression differentiate(String variable) {
        if (variable.equals(name)) {
            return new Number(1);
        }
        return new Number(0);
    }
}
