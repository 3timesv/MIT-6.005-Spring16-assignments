/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //      Represents the weighted-directed graph, with immutable vertices stored in a Set,
    //      and immutable Edges stored in a list.
    //
    // Representation invariant:
    //      For each edge in the List edges, its both vertices must be present in Set vertices.
    //
    // Safety from rep exposure:
    //      field 'vertices' is private, final and immutable.
    //      
    
    /**
     * Creates a new instance of ConcreteEdgesGraph.
     */
    public ConcreteEdgesGraph() {
    }

    /**
     * Checks that representation invariant is true.
     */
    private void checkRep() {
        for (Edge e: edges) {
            assert vertices.contains(e.getSource());
            assert vertices.contains(e.getTarget());
        }
    }
    
    @Override
    public boolean add(String vertex) {
        // if graph already contains the vertex, return false
        if (vertices.contains(vertex)) {
            return false;
        }

        // otherwise, add the vertex to the graph and return true
        vertices.add(vertex);

        checkRep();

        return true;
    }
    
    @Override
    public int set(String source, String target, int weight) {
        assert weight >= 0;

        int previousWeight = 0;

        // check whether edge exists in the graph
        boolean edgeExists = false;
        for (Edge e: edges) {
            // if edge exists in the graph
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                edgeExists = true;
                previousWeight = e.getWeight();

                // if weight is 0, remove the edge
                if (weight == 0) {
                    edges.remove(e);
                    // otherwise, add the new edge to the graph and remove the old edge from the graph
                } else {
                    Edge newEdge = new Edge(source, target, weight);
                    edges.remove(e);
                    edges.add(newEdge);
                }
                break;
            } 
        }

        // if edge does not exist, create the edge and add it to the graph
        if (!edgeExists) {
            if (weight != 0) {
                Edge newEdge = new Edge(source, target, weight);
                edges.add(newEdge);
                vertices.add(source);
                vertices.add(target);
            }
        }

        checkRep();

        return previousWeight;
    }
    
    @Override
    public boolean remove(String vertex) {
        // if graph doesn't contain the vertex, return false
        if (!vertices.contains(vertex)) {
            return false;
        }

        // remove all the edges to or from the vertex
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));

        // remove the vertex
        vertices.remove(vertex);

        checkRep();
        return true;
    }
    
    @Override
    public Set<String> vertices() {
        return this.vertices;
    }
    
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sourcesMap = new HashMap<>();

        // for all edges, add the edge in the sourcesMap if edge.getTarget() equals target
        for (Edge edge: edges) {
            if (edge.getTarget().equals(target)) {
                sourcesMap.put(edge.getSource(), edge.getWeight());
            }
        }

        return sourcesMap;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        Map<String, Integer> targetsMap = new HashMap<>();

        // for all edges, add the edge in the targetsMap if edge.getSource() equals source
        for (Edge edge: edges) {
            if (edge.getSource().equals(source)) {
                targetsMap.put(edge.getTarget(), edge.getWeight());
            }
        }

        return targetsMap;
    }
    
    @Override
    public String toString() {
        // if graph is empty, return the message
        if (vertices.isEmpty()) {
            return "Graph is empty!";
        }

        // initialize an empty String
        String str = "";
        
        // add all the vertices to the String
        str += "-- Vertices --\n";
        for (String s: vertices) {
            str += s + "\n";
        }

        // add all the edges to the String
        str += "\n" + "-- Edges --" + "\n";
        for (Edge e: edges) {
            str += e + "\n";
        }

        return str;
    }
}


/**
 * A weighted-directed edge of a graph.
 * Weights of the edge is positive integer.
 *
 * The Edge is Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge {
    
    // fields
    private final String source; 
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //      Represents a weighted-directed-edge of a graph with source, target and weight

    // Representation invariant:
    //      source and target are non-empty Strings
    //      source and target cannot be same
    //      weight > 0

    // Safety from rep exposure:
    //      All fields are private, final and immutable
    
    /**
     * Creates a new edge.
     *
     * @param source vertex from which the edge originates
     * @param target vertex at which the edge terminates
     * @param weight weight of the edge
     */
    public Edge(String source, String target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;

        checkRep();
    }
    
    /**
     * Checks that representation invariant is true.
     */
    private void checkRep() {
        assert source.length() != 0;
        assert target.length() != 0;
        assert !source.equals(target);
        assert weight > 0;
    }
    
    /**
     * @return source of the edge
     */
    public String getSource() {
        return source;
    }

    /**
     * @return target of the edge
     */
    public String getTarget() {
        return target;
    }

    /**
     * @return weight of the edge
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * @return String representation of the Edge object
     */
    public String toString() {
        return source + " --" + weight + "-> " + target;
    }
}
