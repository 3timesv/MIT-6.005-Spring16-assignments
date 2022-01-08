package expressivo;

public class Number implements Expression {
    private final int whole, decimal;

    // Abstraction function:
    //      Represents a nonnegative number
    // Rep invariant:
    //      whole >= 0
    //      decimal >= 0
    // Safety from rep exposure:
    //      all fields are private and immutable

    /**
     * Creates an instance of Number.
     *
     * @param whole the whole part of the number
     * @param decimal the decimal part of the number
     */
    public Number(int whole, int decimal) {
        this.whole = whole;
        this.decimal = decimal;

        checkRep();
    }

    private void checkRep() {
        assert whole >= 0;
        assert decimal >= 0;
    }

    @Override
    public String toString() {
        if (decimal == 0) {
            return "" + whole;
        }
        return "" + whole + "." + decimal;
    }

    @Override 
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Number)) {
            return false;
        }
        Number n = (Number) thatObject;
        return (whole == n.whole && decimal == n.decimal);
    }

    @Override
    public int hashCode() {
        return ("" + whole + "." + decimal).hashCode();
    }
}
