package expressivo;

public class Plus implements Expression {
    private final Expression left, right;

    // Abstraction function
    //      Represents an addition operation
    // Rep invariant
    //     true
    // Safety from rep exposure
    //      all fields are private and immutable

    /**
     * Creates an instance of Plus.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Plus(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + " + " + right + ")";
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Plus)) {
            return false;
        }
        Plus p = (Plus) thatObject;
        return left.equals(p.left) && right.equals(p.right);
    }

    @Override
    public int hashCode() {
        return ("" + left.hashCode() + "+" + right.hashCode()).hashCode();
    }

    @Override
    public Expression differentiate(String variable) {
        return new Plus(left.differentiate(variable), right.differentiate(variable));
    }
}
