/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   Graph is empty or non-empty
    
    // ConcreteEdgesGraph.toString() : Graph is empty
    @Test 
    public void testConcreteEdgesGraphToStringEmptyGraph() {
        Graph<String> g = emptyInstance();

        assertEquals("expected message for empty graph", "Graph is empty!", g.toString());
    }

    // ConcreteEdgesGraph.toString() : Graph is non-empty
    @Test
    public void testConcreteEdgesGraphToStringNonEmptyGraph() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 10);

        String expectedResult = "-- Vertices --\nA\nB\n\n-- Edges --\nA --10-> B\n";

        assertEquals("expected String representation of graph", expectedResult, g.toString());
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //  
    // check the creation of an Edge - Edge(String, String, String)
    // check source, target and weight of the edge
    //
    // check toString() of an Edge

    @Test
    public void testEdgeCreation() {
        Edge<String> edge = new Edge<>("A", "B", 10);

        assertEquals("expected source", "A", edge.getSource());
        assertEquals("expected target", "B", edge.getTarget());
        assertEquals("expected weight", 10, edge.getWeight());
    }

    @Test
    public void testEdgetoString() {
        Edge<String> edge = new Edge<>("A", "B", 10);
        assertEquals("expected string representation", "A --10-> B", edge.toString());
    }
}
