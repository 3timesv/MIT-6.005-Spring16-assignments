/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import java.util.Set;
import java.util.HashSet;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // test other vertex label types in Problem 3.2
    @Test
    public void testIntegerVertices() {
        Graph<Integer> g = Graph.empty();
        g.add(1);
        g.add(2);

        Set<Integer> expected = new HashSet<>();
        expected.add(1);
        expected.add(2);
        assertEquals("expected set of vertices",
                expected, g.vertices());
    }

    @Test
    public void testCharacterVertices() {
        Graph<Character> g = Graph.empty();
        g.add('a');
        g.add('b');

        Set<Character> expected = new HashSet<>();
        expected.add('a');
        expected.add('b');
        assertEquals("expected set of vertices",
                expected, g.vertices());
    }
}
