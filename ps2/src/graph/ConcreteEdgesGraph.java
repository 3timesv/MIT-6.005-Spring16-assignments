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
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //      Represents the weighted-directed graph, with immutable vertices stored in a Set,
    //      and immutable Edges stored in a list.
    //
    // Representation invariant:
    //      For each edge in the List edges, its both vertices must be present in Set vertices.
    //
    // Safety from rep exposure:
    //      field 'vertices' is private and immutable.
    //      field 'edges' is private but mutable.
    //      There is no method which exposes the edges List to the client.
    
    /**
     * Creates a new instance of ConcreteEdgesGraph.
     */
    public ConcreteEdgesGraph() {
    }

    /**
     * Checks that representation invariant is true.
     */
    private void checkRep() {
        for (Edge<L> e: edges) {
            assert vertices.contains(e.getSource());
            assert vertices.contains(e.getTarget());
        }
    }
    
    @Override
    public boolean add(L vertex) {
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
    public int set(L source, L target, int weight) {
        assert weight >= 0;

        int previousWeight = 0;

        // check whether edge exists in the graph
        boolean edgeExists = false;
        for (Edge<L> e: edges) {
            // if edge exists in the graph
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                edgeExists = true;
                previousWeight = e.getWeight();

                // if weight is 0, remove the edge
                if (weight == 0) {
                    edges.remove(e);
                    // otherwise, add the new edge to the graph and remove the old edge from the graph
                } else {
                    Edge<L> newEdge = new Edge<L>(source, target, weight);
                    edges.remove(e);
                    edges.add(newEdge);
                }
                break;
            } 
        }

        // if edge does not exist, create the edge and add it to the graph
        if (!edgeExists) {
            if (weight != 0) {
                Edge<L> newEdge = new Edge<L>(source, target, weight);
                edges.add(newEdge);
                vertices.add(source);
                vertices.add(target);
            }
        }

        checkRep();

        return previousWeight;
    }
    
    @Override
    public boolean remove(L vertex) {
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
    public Set<L> vertices() {
        return this.vertices;
    }
    
    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> sourcesMap = new HashMap<>();

        // for all edges, add the edge in the sourcesMap if edge.getTarget() equals target
        for (Edge<L> edge: edges) {
            if (edge.getTarget().equals(target)) {
                sourcesMap.put(edge.getSource(), edge.getWeight());
            }
        }

        return sourcesMap;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targetsMap = new HashMap<>();

        // for all edges, add the edge in the targetsMap if edge.getSource() equals source
        for (Edge<L> edge: edges) {
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
        for (L s: vertices) {
            str += s + "\n";
        }

        // add all the edges to the String
        str += "\n" + "-- Edges --" + "\n";
        for (Edge<L> e: edges) {
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
class Edge<L> {
    
    // fields
    private final L source; 
    private final L target;
    private final int weight;
    
    // Abstraction function:
    //      Represents a weighted-directed-edge of a graph with source, target and weight

    // Representation invariant:
    //      source and target cannot be same
    //      weight > 0

    // Safety from rep exposure:
    //      All fields are private and immutable
    
    /**
     * Creates a new edge.
     *
     * @param source vertex from which the edge originates
     * @param target vertex at which the edge terminates
     * @param weight weight of the edge
     */
    public Edge(L source, L target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;

        checkRep();
    }
    
    /**
     * Checks that representation invariant is true.
     */
    private void checkRep() {
        assert !source.equals(target);
        assert weight > 0;
    }
    
    /**
     * @return source of the edge
     */
    public L getSource() {
        return source;
    }

    /**
     * @return target of the edge
     */
    public L getTarget() {
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
