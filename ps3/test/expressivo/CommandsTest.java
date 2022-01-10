/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //
    // Commands.differentiate() :
    //      Number, Variable, Plus, Multiply
    //
    // Commands.simplify() :
    //      Number, Variable, Plus, Multiply
    //      env contains 0, 1, all variables to be substituted
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // Commands.differentiate() : Number
    @Test
    public void testDifferentiateNumber() {
        assertEquals("0.0", Commands.differentiate("1", "x"));
    }

    // Commands.differentiate() : Variable
    @Test
    public void testDifferentiateVariable() {
        assertEquals("1.0", Commands.differentiate("x", "x"));
    }

    // Commands.differentiate() : Plus
    @Test
    public void testDifferentiatePlus() {
        assertEquals("(1.0 + 0.0)", Commands.differentiate("x+1", "x"));
    }

    // Commands.differentiate() : Multiply
    @Test
    public void testDifferentiateMultiply() {
        assertEquals("((1.0 * x) + (x * 1.0))", Commands.differentiate("x*x", "x"));
    }

    // simplify() : Number
    @Test
    public void testNumber() {
        String exp = "3.4";
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected equal numbers", "3.4", Commands.simplify(exp, env));
    }

    // simplify() : Variable, env doesn't contain variable value
    @Test
    public void testVariableEnvNotContain() {
        String exp = "x";
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected variable to be same", "x", Commands.simplify(exp, env));
    }

    // simplify() : Variable, env contains variable's value
    @Test
    public void testVariableEnvContains() {
        String exp = "x";
        Map<String, Double> env = new HashMap<>();
        env.put("x", 4.9);

        assertEquals("expected variable to be same", "4.9", Commands.simplify(exp, env));
    }

    // simplify() : Plus, env contains 0 variables to be substituted
    @Test
    public void testPlusEnvContainZero() {
        String exp = "x+y";
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected same expression", "(x + y)", Commands.simplify(exp, env));
    }

    // simplify() : Plus, env contains 1 variables to be substituted
    @Test
    public void testPlusEnvContainOne() {
        String exp = "x+y";
        Map<String, Double> env = new HashMap<>();
        env.put("y", 4.9);

        assertEquals("expected value substitution", "(x + 4.9)", Commands.simplify(exp, env));
    }

    // simplify() : Plus, env contains 2 variables to be substituted
    @Test
    public void testPlusEnvContainTwo() {
        String exp = "x + y";
        Map<String, Double> env = new HashMap<>();
        env.put("x", 4.9);
        env.put("y", 5.1);

        assertEquals("expected evaluated expression", "10.0", Commands.simplify(exp, env));
    }

    // simplify() : Multiply, env contains 0 variables to be substituted
    @Test
    public void testMultiplyEnvContainZero() {
        String exp = "x*y";
        Map<String, Double> env = new HashMap<>();

        assertEquals("expected same expression", "(x * y)", Commands.simplify(exp, env));
    }

    // simplify() : Multiply, env contains 1 variable to be substituted
    @Test
    public void testMultiplyEnvContainOne() {
        String exp = "x*y";
        Map<String, Double> env = new HashMap<>();
        env.put("y", 4.9);

        assertEquals("expected value substitution", "(x * 4.9)", Commands.simplify(exp, env));
    }

    // simplify() : Multiply, env contains 2 variables to be substituted
    @Test
    public void testMultiplyEnvContainTwo() {
        String exp = "x * y";
        Map<String, Double> env = new HashMap<>();
        env.put("x", 4.0);
        env.put("y", 5.0);

        assertEquals("expected evaluated expression", "20.0", Commands.simplify(exp, env));
    }
}
