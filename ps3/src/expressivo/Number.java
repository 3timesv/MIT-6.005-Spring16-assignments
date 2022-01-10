package expressivo;

import java.util.Map;

public class Number implements Expression {
    private final double value;

    // Abstraction function:
    //      Represents a nonnegative number
    // Rep invariant:
    //     value >= 0
    // Safety from rep exposure:
    //      all fields are private and immutable

    /**
     * Creates an instance of Number.
     *
     * @param value the value of the number
     */
    public Number(double value) {
        this.value = value;

        checkRep();
    }

    private void checkRep() {
        assert value >= 0;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override 
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Number)) {
            return false;
        }
        Number n = (Number) thatObject;
        return this.value == n.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public Expression differentiate(String variable) {
        return new Number(0);
    }

    @Override
    public Expression simplify(Map<String, Double> env) {
        return this;
    }

    @Override
    public double getValue() {
        return value;
    }
}
