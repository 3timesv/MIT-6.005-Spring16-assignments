/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    // 
    // Test make():
    //      Number, Variable
    //
    // Test makePlus()
    // Test makeMultiply()
    //
    // Test toString():
    //      Number, Variable, Plus, Multiply
    //
    // Test equals():
    //      Number, Variable, Plus, Multiply
    //
    // Test hashCode():
    //      Number, Variable, Plus, Multiply

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // make() : Number
    @Test
    public void testMakeNumber() {
        Expression exp = Expression.make(10.4);
        assertTrue("expected Number instance", exp instanceof Number);
    }

    // make() : Variable
    @Test
    public void testMakeVariable() {
        Expression exp = Expression.make("foo");
        assertTrue("expected Variable instance", exp instanceof Variable);
    }

    // makePlus()
    @Test
    public void testMakePlus() {
        Expression exp = Expression.makePlus(Expression.make(10), Expression.make("foo"));
        assertTrue("expected Plus instance", exp instanceof Plus);
    }

    // makeMultiply()
    @Test
    public void testMakeMultiply() {
        Expression exp = Expression.makeMultiply(Expression.make(104.1), Expression.make("foo"));
        assertTrue("expected Multiply instance", exp instanceof Multiply);
    }

    // toString() : Number
    @Test
    public void testToStringNumber() {
        Expression num = Expression.make(10.89);
        assertEquals("expected same strings", "10.89", num.toString());
    }

    // toString() : Variable
    @Test
    public void testToStringVariable() {
        Expression exp = Expression.make("foo");
        assertEquals("expected same strings", "foo", exp.toString());
    }

    // toString() : Plus
    @Test
    public void testToStringPlus() {
        Expression exp = Expression.makePlus(Expression.make("x"), Expression.make(78.4));
        assertEquals("expected same strings", "(x + 78.4)", exp.toString());
    }

    // toString() : Multiply
    @Test
    public void testToStringMultiply() {
        Expression exp = Expression.makeMultiply(Expression.make("x"), Expression.make(78.4));
        assertEquals("expected same strings", "(x * 78.4)", exp.toString());
    }

    // equals() : Number
    @Test
    public void testEqualsNumber() {
        Expression num1 = Expression.make(10.3);
        Expression num2 = Expression.make(10.3);
        Expression num3 = Expression.make(10.5);

        assertEquals("expected numbers to be equal", num1, num2);
        assertNotEquals("expected numbers to be not equal", num1, num3);
    }

    // equals() : Variable
    @Test
    public void testEqualsVariable() {
        Expression var1 = Expression.make("x");
        Expression var2 = Expression.make("x");
        Expression var3 = Expression.make("y");

        assertEquals("expected variables to be equal", var1, var2);
        assertNotEquals("expected variables to be not equal", var1, var3);
    }

    // equals() : Plus 
    @Test
    public void testEqualsPlus() {
        Expression op1 = Expression.make("x");
        Expression op2 = Expression.make(5);
        Expression p = Expression.makePlus(op1, op2);

        assertEquals("expected equal strings", "(x + 5.0)", p.toString());
        assertNotEquals("expected different strings", "(5.0 + x)", p.toString());
    }

    // equals() : Multiply
    @Test
    public void testEqualsMultiply() {
        Expression op1 = Expression.make("x");
        Expression op2 = Expression.make(5);
        Expression m = Expression.makeMultiply(op1, op2);

        assertEquals("expected equal strings", "(x * 5.0)", m.toString());
        assertNotEquals("expected different strings", "(5.0 * x)", m.toString());
    }

    // hashCode() : Number
    @Test
    public void testHashCodeNumber() {
        Expression num1 = Expression.make(45.766);
        Expression num2 = Expression.make(45.767);
        Expression num3 = Expression.make(1.000);
        Expression num4 = Expression.make(1.0);

        assertNotEquals("expected different hashCodes", num1.hashCode(), num2.hashCode());
        assertEquals("expected same hashCodes", num3.hashCode(), num4.hashCode());
    }

    // hashCode() : Variable
    @Test
    public void testHashCodeVariable() {
        Expression var1 = Expression.make("foo");
        Expression var2 = Expression.make("boo");

        assertNotEquals("expected different hashCodes", var1.hashCode(), var2.hashCode());
    }

    // hashCode() : Plus
    @Test
    public void testHashCodePlus() {
        Expression p1 = Expression.makePlus(Expression.make("x"), Expression.make(34.2));
        Expression p2 = Expression.makePlus(Expression.make(34.2), Expression.make("x"));

        assertNotEquals("expected different hashCodes", p1.hashCode(), p2.hashCode());
    }

    // hashCode() : Multiply
    @Test
    public void testHashCodeMultiply() {
        Expression m1 = Expression.makeMultiply(Expression.make("x"), Expression.make(34.2));
        Expression m2 = Expression.makeMultiply(Expression.make(34.2), Expression.make("x"));

        assertNotEquals("expected different hashCodes", m1.hashCode(), m2.hashCode());
    }
}
