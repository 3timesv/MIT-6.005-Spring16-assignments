/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   
    // add() :
    // vertex is already in graph, vertex is not in graph
    //
    // set() :
    // weight is zero, weight is non-zero
    // edge is already in graph, edge is not in graph
    //
    // remove() :
    // graph includes a vertex with given label or it doesn't
    //
    // vertices() :
    // graph is empty or non-empty
    //
    // sources() :
    // number of vertices with directed edges to the target: 0, 1, all 
    //
    // target() :
    // number of target vertices with directed edges from the source vertex: 0, 1, all

    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    // add() : vertex is already in graph
    @Test
    public void testAddVertexInGraph() {
        Graph<String> g = emptyInstance();
        g.add("A");

        assertFalse("expected false when vertex is already in graph", g.add("A"));
    }

    // add() : vertex is not in graph
    @Test
    public void testAddVertexNotInGraph() {
        Graph<String> g = emptyInstance();
        boolean result = g.add("A");

        assertTrue("expected true when vertex is not in graph", result);

        // test whether vertex is added to the graph
        Set<String> vertices = g.vertices();

        assertTrue("expected vertex in graph", vertices.contains("A"));
    }

    // set() : weight is zero, edge is not in graph
    @Test
    public void testSetZeroWeightEdgeNotInGraph() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");

        assertEquals("exptected 0 when the edge not exists already", 0, g.set("B", "C", 0));
    }

    // set() : weight is nonzero, edge is not in the graph
    @Test
    public void testSetNonZeroWeightEdgeNotInGraph() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");

        assertEquals("expected 0 when the edge not exists already", 0, g.set("B", "C", 5));

        // check whether new edge is added to the graph
        Map<String, Integer> trueMap = g.targets("B");

        Map<String, Integer> expectedMap = new HashMap<String, Integer>();
        expectedMap.put("C", 5);

        assertEquals("expected new edge in graph", expectedMap, trueMap);
    }

    // set() : weight is zero, edge is in the graph
    @Test
    public void testSetZeroWeightEdgeInGraph() {
        Graph<String> g = emptyInstance();
        // add edge to the graph
        g.set("A", "B", 10);

        assertEquals("expected previous weight", 10, g.set("A", "B", 0));

        // check whether edge is removed
        Map<String, Integer> expectedResult = new HashMap<String, Integer>();

        Map<String, Integer> trueResult = g.targets("A");

        assertEquals("expected edge to be removed", expectedResult, trueResult);

    }

    // set() : weight is nonzero, edge is in the graph
    @Test
    public void testSetNonZeroWeightEdgeInGraph() {
        Graph<String> g = emptyInstance();
        // add edge to the graph
        g.set("A", "B", 10);

        assertEquals("expected previous weight", 10, g.set("A", "B", 5));

        // check whether edge weight is updated
        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        expectedResult.put("B", 5);

        Map<String, Integer> trueResult = g.targets("A");
        assertEquals("expected edge weight to be updated", expectedResult, trueResult);

    }

    // remove() : graph includes a vertex with given label
    @Test
    public void testRemoveIncludesVertex() {
        Graph<String> g = emptyInstance();
        g.add("A");

        assertTrue("expected true when the graph included the vertex", g.remove("A"));

        // test whether vertex is removed from the graph
        assertEquals("expected graph to be empty after removing only vertex", Collections.emptySet(), g.vertices());
    }

    // remove() : graph doesn't includes a vertex with given label
    @Test
    public void testRemoveNotIncludesVertex() {
        Graph<String> g = emptyInstance();

        assertFalse("expected false when graph doesn't already have the vertex", g.remove("A"));
    }

    // vertices() : graph is empty
    @Test
    public void testVerticesEmptyGraph() {
        assertEquals("expected empty graph to have no vertices", Collections.emptySet(), emptyInstance().vertices());
    }

    // vertices() : graph is non-empty
    @Test
    public void testVerticesNonEmptyGraph() {
        Graph<String> g = emptyInstance();
        g.add("A");
        g.add("B");

        Set<String> vertices = g.vertices();
        assertTrue("expected vertex in graph", vertices.contains("A"));
        assertTrue("expected vertex in graph", vertices.contains("B"));
    }

    // sources() : 0 vertices with directed edges to the target
    @Test
    public void testSourcesZeroVertices() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 10);

        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        Map<String, Integer> trueResult = g.sources("A");

        assertEquals("expected empty map", expectedResult, trueResult);
    }

    // sources() : 1 vertex with directed edge to the target
    @Test
    public void testSourcesOneVertex() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 10);
        g.set("B", "C", 5);

        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        expectedResult.put("A", 10);

        Map<String, Integer> trueResult = g.sources("B");

        assertEquals("expected equal maps", expectedResult, trueResult);
    }

    // sources() : all vertices with directed edges to the target
    @Test
    public void testSourcesAllVertices() {
        Graph<String> g = emptyInstance();
        g.set("A", "C", 10);
        g.set("B", "C", 5);

        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        expectedResult.put("A", 10);
        expectedResult.put("B", 5);

        Map<String, Integer> trueResult = g.sources("C");

        assertEquals("expected equals maps", expectedResult, trueResult);
    }

    // targets() : 0 target vertices with directed edges from the source vertex
    @Test
    public void testTargetsZeroVertices() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 10);

        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        Map<String, Integer> trueResult = g.targets("B");

        assertEquals("expected empty maps", expectedResult, trueResult);
    }

    // targets() : 1 target vertex with directed edge from the source vertex
    @Test
    public void testTargetsOneVertex() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 10);

        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        expectedResult.put("B", 10);

        Map<String, Integer> trueResult = g.targets("A");
        
        assertEquals("expected equal maps", expectedResult, trueResult);
    }

    // targets() : all vetices are target vertices with directed edges from the source vertex
    @Test
    public void testTargetsAllVertices() {
        Graph<String> g = emptyInstance();
        g.set("A", "B", 10);
        g.set("A", "C", 5);

        Map<String, Integer> expectedResult = new HashMap<String, Integer>();
        expectedResult.put("B", 10);
        expectedResult.put("C", 5);

        Map<String, Integer> trueResult = g.targets("A");

        assertEquals("expected equal maps", expectedResult, trueResult);
    }
}
