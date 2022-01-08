package expressivo;

public class Multiply implements Expression {
    private final Expression left, right;

    // Abstraction function:
    //     Represents a multiplication operation
    // Rep invariant
    //     true
    // Safety from rep exposure
    //      All fields are private and immutable

    /**
     * Creates an instance of Multiply.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Multiply(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + " * " + right + ")";
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Multiply)) {
            return false;
        }
        Multiply m = (Multiply) thatObject;
        return left.equals(m.left) && right.equals(m.right);
    }

    @Override
    public int hashCode() {
        return ("" + left.hashCode() + "*" + right.hashCode()).hashCode();
    }
}

