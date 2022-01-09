/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //
    // Commands.differentiate() :
    //      Number, Variable, Plus, Multiply
    
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
}
