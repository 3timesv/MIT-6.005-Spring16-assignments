/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    // Graph is empty or non-empty
    
    // ConcreteVerticesGraph.toString() : graph is empty
    @Test
    public void testToStringEmptyGraph() {
        Graph<String> g = new ConcreteVerticesGraph<String>();

        assertEquals("expected empty String", "", g.toString());
    }

    // ConcreteVerticesGraph.toString() : graph is non-empty
    @Test
    public void testToStringNonEmptyGraph() {
        Graph<String> g = new ConcreteVerticesGraph<String>();
        g.set("A", "B", 10);

        // there can be two possible answers (vertex A can be printed before B or vice versa), both are valid
        String expectedString1 = "Value = A\n\n-- Edges starting from current vertex --\n";
        expectedString1 += "--10-> B\nNo edges ends at this vertex!\n\n";
        expectedString1 += "Value = B\n\nNo edges starts from this vertex!\n";
        expectedString1 += "-- Edges ending at current vertex --\nA --10->\n\n";

        String expectedString2 = "Value = B\n\nNo edges starts from this vertex!\n";
        expectedString2 += "-- Edges ending at current vertex --\nA --10->\n\n";
        expectedString2 += "Value = A\n\n-- Edges starting from current vertex --\n";
        expectedString2 += "--10-> B\nNo edges ends at this vertex!\n\n";

        assertTrue("expected one of two Strings", expectedString1.equals(g.toString()) || expectedString2.equals(g.toString()));
    }
    
    /*
     * Testing Vertex...
     * Testing strategy for Vertex
     *
     * connectEdge() :
     * edge is already connected to vertex or doesn't
     * edge starts or ends at the vertex
     *
     * removeEdge() :
     * edge is already connected to vertex or doesn't
     * edge starts or ends at the vertex
     *
     * getValue() :
     *
     * setValue() :
     *
     * getstarts() :
     * Number of nodes that starts from the vertex are zero or nonzero
     *
     * getEnds() :
     * Number of nodes that ends at the vertex are zero or nonzero
     *
     * toString() :
     * Zero or nonzero edges starts from vertex
     * Zero or nonzero edges ends at vertex
     */

    // connectEdge() : edge is not connected to vertex & edge starts from the vertex
    @Test
    public void testConnectEdgeNotConnectedStarts() {
        Vertex<String> v = new Vertex<>("A");
        boolean result = v.connectEdge("B", 10, true);

        assertTrue("expected true", result);
        assertTrue("expected edge to start from current vertex", v.getStarts().containsKey("B"));
    }

    // connectEdge() : edge is not connected to vertex & edge ends at the vertex
    @Test
    public void testConnectEdgeNotConnectedEnds() {
        Vertex<String> v = new Vertex<>("A");
        boolean result = v.connectEdge("B", 10, false);

        assertTrue("expected true", result);
        assertTrue("expected edge to end at current vertex", v.getEnds().containsKey("B"));
    }

    // connectEdge() : edge is already connected to vertex & edge starts from the vertex
    @Test
    public void testConnectEdgeConnectedStarts() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, true);

        boolean result = v.connectEdge("B", 5, true);
        assertFalse("expected edge to be already connected", result);
    }

    // connectEdge() : edge is already connected to vertex & edge ends at the vertex
    @Test
    public void testConnectEdgeConnectedEnds() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, false);

        boolean result = v.connectEdge("B", 5, false);
        assertFalse("expected edge to be already connected", result);
    }

    // removeEdge() : edge is not connected to vertex & edge starts from the vertex
    @Test
    public void testRemoveEdgeNotConnectedStarts() {
        Vertex<String> v = new Vertex<>("A");
        boolean result = v.removeEdge("B", true);

        assertFalse("expected false", result);
    }

    // removeEdge() : edge is not connected to vertex & edge ends at the vertex
    @Test
    public void testRemoveEdgeNotConnectedEnds() {
        Vertex<String> v = new Vertex<>("A");
        boolean result = v.removeEdge("B", false);

        assertFalse("expected false", result);
    }

    // removeEdge() : edge is already connected to vertex & edge starts from the vertex
    @Test
    public void testRemoveEdgeConnectedStarts() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, true);

        boolean result = v.removeEdge("B", true);
        assertTrue("expected edge to be removed", result);
    }

    // removeEdge() : edge is already connected to vertex & edge ends at the vertex
    @Test
    public void testRemoveEdgeConnectedEnds() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, false);

        boolean result = v.removeEdge("B", false);
        assertTrue("expected edge to be removed", result);
    }

    // getValue() :
    @Test
    public void testGetValue() {
        Vertex<String> v = new Vertex<>("A");

        assertEquals("expected vertex value", "A", v.getValue());
    }

    // setValue() :
    @Test
    public void testSetValue() {
        Vertex<String> v = new Vertex<>("A");
        v.setValue("B");

        assertEquals("expected value to be replaced", "B", v.getValue());
    }

    // getStarts() : Zero vertices starts from the vertex
    @Test
    public void testGetStartsZeroVertices() {
        Vertex<String> v = new Vertex<>("A");

        assertTrue("expected empty map", v.getStarts().isEmpty());
    }

    // getStarts() : NonZero vertices starts from the vertex
    @Test
    public void testGetStartsNonZeroVertices() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, true);

        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("B", 10);

        assertEquals("expected same maps", expectedMap, v.getStarts());
    }

    // getEnds() : Zero vertices ends at the vertex
    @Test
    public void testGetEndsZeroVertices() {
        Vertex<String> v = new Vertex<>("A");
        assertTrue("expected empty map", v.getEnds().isEmpty());
    }

    // getEnds() : NonZero vertices ends at the vertex
    @Test
    public void testGetEndsNonZeroVertices() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, false);

        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put("B", 10);

        assertEquals("expected same maps", expectedMap, v.getEnds());
    }

    // toString() : Zero vertices starts from and Zero vertices ends at the vertex
    @Test
    public void testToStringZeroVerticesStartsZeroVerticesEnds() {
        Vertex<String> v = new Vertex<>("A");

        String expectedString = "Value = A\n\nNo edges starts from this vertex!\n";
        expectedString += "No edges ends at this vertex!\n";

        assertEquals("expected same strings", expectedString, v.toString());
    }

    // toString() : Zero vertices starts from and NonZero vertices ends at the vertex
    @Test
    public void testToStringZeroVerticesStartsNonZeroVerticesEnds() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, false);

        String expectedString = "Value = A\n\nNo edges starts from this vertex!\n";
        expectedString += "-- Edges ending at current vertex --\n";
        expectedString += "B --10->\n";

        assertEquals("expected same strings", expectedString, v.toString());
    }

    // toString() : NonZero vertices starts from and Zero vertices ends at the vertex
    @Test
    public void testToStringNonZeroVerticesStartsZeroVerticesEnds() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, true);

        String expectedString = "Value = A\n\n-- Edges starting from current vertex --\n";
        expectedString += "--10-> B\nNo edges ends at this vertex!\n";

        assertEquals("expected same strings", expectedString, v.toString());
    }

    // toString() : Zero vertices starts from and Zero vertices ends at the vertex
    @Test 
    public void testToStringNonZeroVerticesStartsNonZeroVerticesEnds() {
        Vertex<String> v = new Vertex<>("A");
        v.connectEdge("B", 10, true);
        v.connectEdge("C", 5, false);

        String expectedString = "Value = A\n\n-- Edges starting from current vertex --\n";
        expectedString += "--10-> B\n";
        expectedString += "-- Edges ending at current vertex --\n";
        expectedString += "C --5->\n";

        assertEquals("expected same strings", expectedString, v.toString());
    }
}
