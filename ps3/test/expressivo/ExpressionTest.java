/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
j*/
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

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
    //
    // Test parse():
    //      sum, multiply, multiply precedes sum, sum precedes multiply, extra whitespaces, multiple parentheses
    //
    // Test differentiate():
    //      constant, variable, sum, multiply
    //
    // Test simplify():
    //     constant, variable, sum, multiply
    //     env 0, 1, all values to be substitued

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

    // parse() : sum
    @Test
    public void testParseSum() {
        Expression exp = Expression.parse("3 + 2.4");

        assertTrue("expected Plus instance", exp instanceof Plus);
        assertEquals("expected same strings", "(3.0 + 2.4)", exp.toString());
    }

    // parse() : multiply
    @Test
    public void testParseMul() {
        Expression exp = Expression.parse("3 * 2.4");

        assertTrue("expected Multiply instance", exp instanceof Multiply);
        assertEquals("expected same strings", "(3.0 * 2.4)", exp.toString());
    }

    // parse() : multiply precedes sum
    @Test
    public void testParseMulPrecedesSum() {
        Expression exp = Expression.parse("3 * x + 2.4");

        assertTrue("expected Plus instance", exp instanceof Plus);
        assertEquals("expected same strings", "((3.0 * x) + 2.4)", exp.toString());
    }

    // parse() : sum precedes multiply
    @Test
    public void testParseSumPrecedesMul() {
        Expression exp = Expression.parse("3 * (x + 2.4)");

        assertTrue("expected Multiply instance", exp instanceof Multiply);
        assertEquals("expected same strings", "(3.0 * (x + 2.4))", exp.toString());
    }

    // parse() : extra whitespaces
    @Test
    public void testParseExtraWhitespaces() {
        Expression exp = Expression.parse("(2*x   )+  (   y*x   )");

        assertTrue("expected Plus instance", exp instanceof Plus);
        assertEquals("expected same strings", "((2.0 * x) + (y * x))", exp.toString());
    }

    // parse() : Multiple parentheses
    @Test
    public void testParseMultipleParentheses() {
        Expression exp = Expression.parse("4 + 3 * x + 2 * x * x + 1 * x * x * (((x)))");

        // all expected strings below are valid
        String expected1 = "((4.0 + (3.0 * x)) + (2.0 * (x * x)) + (1.0 * (x * (x * (x)))))";
        String expected2 = "((4.0 + (3.0 * x)) + (((2.0 * x) * x) + (((1.0 * x) * x) * x)))";
        String expected3 = "(((4.0 + (3.0 * x)) + ((2.0 * x) * x)) + (((1.0 * x) * x) * x))";

        assertTrue("expected Plus instance", exp instanceof Plus);
        assertTrue("expected either one of the strings", exp.toString().equals(expected1) || exp.toString().equals(expected2) || exp.toString().equals(expected3));
    }

    // differentiate() : constant
    @Test
    public void testDifferentiateConstant() {
        Expression exp = Expression.make(5);
        Expression diff = exp.differentiate("x");

        assertEquals("expected zero", Expression.make(0), diff);
    }

    // differentiate() : variable
    @Test
    public void testDifferentiateVariable() {
        Expression exp = Expression.make("x");
        Expression diff = exp.differentiate("x");

        assertEquals("expected one", Expression.make(1), diff);
    }

    // differentiate() : sum
    @Test
    public void testDifferentiateSum() {
        Expression exp = Expression.makePlus(Expression.make("x"), Expression.make("y"));
        Expression diff = exp.differentiate("x");

        String expected = "(1.0 + 0.0)";
        assertEquals("expected same strings", expected, diff.toString());
    }

    // differentiate() : multiply
    @Test
    public void testDifferentiateMultiply() {
        Expression exp = Expression.makeMultiply(Expression.make("x"), Expression.make("y"));
        Expression diff = exp.differentiate("x");

        String expected = "((1.0 * y) + (x * 0.0))";
        assertEquals("expected same strings", expected, diff.toString());
    }

    // simplify() : Number
    @Test
    public void testNumber() {
        Expression exp = Expression.make(3.4);
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected equal numbers", Expression.make(3.4), exp.simplify(env));
    }

    // simplify() : Variable, env doesn't contain variable value
    @Test
    public void testVariableEnvNotContain() {
        Expression exp = Expression.make("x");
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected variable to be same", Expression.make("x"), exp.simplify(env));
    }

    // simplify() : Variable, env contains variable's value
    @Test
    public void testVariableEnvContains() {
        Expression exp = Expression.make("x");
        Map<String, Double> env = new HashMap<>();
        env.put("x", 4.9);

        assertEquals("expected variable to be same", Expression.make(4.9), exp.simplify(env));
    }

    // simplify() : Plus, env contains 0 variables to be substituted
    @Test
    public void testPlusEnvContainZero() {
        Expression exp = Expression.makePlus(Expression.make("x"), Expression.make("y"));
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected same expression", exp, exp.simplify(env));
    }

    // simplify() : Plus, env contains 1 variables to be substituted
    @Test
    public void testPlusEnvContainOne() {
        Expression exp = Expression.makePlus(Expression.make("x"), Expression.make("y"));
        Map<String, Double> env = new HashMap<>();
        env.put("y", 4.9);

        assertEquals("expected value substitution", Expression.makePlus(Expression.make("x"), Expression.make(4.9)), exp.simplify(env));
    }

    // simplify() : Plus, env contains 2 variables to be substituted
    @Test
    public void testPlusEnvContainTwo() {
        Expression exp = Expression.makePlus(Expression.make("x"), Expression.make("y"));
        Map<String, Double> env = new HashMap<>();
        env.put("x", 4.9);
        env.put("y", 5.1);

        assertEquals("expected evaluated expression", Expression.make(10), exp.simplify(env));
    }

    // simplify() : Multiply, env contains 0 variables to be substituted
    @Test
    public void testMultiplyEnvContainZero() {
        Expression exp = Expression.makeMultiply(Expression.make("x"), Expression.make("y"));
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected same expression", exp, exp.simplify(env));
    }

    // simplify() : Multiply, env contains 1 variable to be substituted
    @Test
    public void testMultiplyEnvContainOne() {
        Expression exp = Expression.makeMultiply(Expression.make("x"), Expression.make("y"));
        Map<String, Double> env = new HashMap<>();
        env.put("y", 4.9);

        assertEquals("expected value substitution", Expression.makeMultiply(Expression.make("x"), Expression.make(4.9)), exp.simplify(env));
    }

    // simplify() : Multiply, env contains 2 variables to be substituted
    @Test
    public void testMultiplyEnvContainTwo() {
        Expression exp = Expression.makeMultiply(Expression.make("x"), Expression.make("y"));
        Map<String, Double> env = new HashMap<>();
        env.put("x", 4.0);
        env.put("y", 5.0);

        assertEquals("expected evaluated expression", Expression.make(20), exp.simplify(env));
    }
}
