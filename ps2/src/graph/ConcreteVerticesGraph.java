/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //      Represents the weighted-directed graph, with mutable vertices stored in a List;
    //
    // Representation invariant:
    //      - All vertices in the vertices List are unique.
    //      - if there is a vertex V in the vertices List, and consider another two vertices S and E
    //          such that V.getStarts() contains S and V.getEnds() contains E
    //          * then S.getEnds() must contain V and E.getStarts() must contain V
    //          * vertices List must contain S and E
    //
    // Safety from rep exposure:
    //      field is private.
    //      vertices is mutable but there is no method which shares the vertices List with clients.
    
    public ConcreteVerticesGraph() {
    }

    private void checkRep() {
        Set<L> uniqueValues = new HashSet<>();

        // iterate over vertices
        for (Vertex v: vertices) {
            // add current vertex's label to the uniqueValues set
            uniqueValues.add( (L) v.getValue());

            // get the labels of vertices that starts/ ends at v
            Set<L> starts = v.getStarts().keySet();
            Set<L> ends = v.getEnds().keySet();

            // v must be in the getEnds()/ getStarts() of vertices that starts/ ends at v
            for (Vertex vert: vertices) {
                if (starts.contains(vert.getValue())) {
                    assert vert.getEnds().containsKey(v.getValue());
                }
                if (ends.contains(vert.getValue())) {
                    assert vert.getStarts().containsKey(v.getValue());
                }
            }

            // vertices must contain all the vertices of starts
            for (L s: starts) {
                boolean found = false;
                for (Vertex vertex: vertices) {
                    if (vertex.getValue().equals(s)) {
                        found = true;
                        break;
                    }
                }
                assert found;
            }

            // vertices must contain all the vertices of ends
            for (L e: ends) {
                boolean found = false;
                for (Vertex vertex: vertices) {
                    if (vertex.getValue().equals(e)) {
                        found = true;
                        break;
                    }
                }
                assert found;
            }
        }

        // vertices must are unique
        assert vertices.size() == uniqueValues.size();
    }
    
    @Override
    public boolean add(L vertex) {
        // if vertex is already in the graph, return false
        for (Vertex v: vertices) {
            if (v.getValue().equals(vertex)) {
                return false;
            }
        }

        // create a new vertex and add it to vertices List
        Vertex newVertex = new Vertex<L>(vertex);
        vertices.add(newVertex);

        checkRep();
        return true;
    }
    
    @Override
    public int set(L source, L target, int weight) {
        assert weight >= 0;

        // initialize previous weight to zero
        int previousWeight = 0;

        // check if edge already exists
        for (Vertex src: vertices) {
            for (Vertex tgt: vertices) {
                // if edge exists,  
                if (src.getValue().equals(source) && tgt.getValue().equals(target) && src.getStarts().containsKey(target)) {
                    previousWeight = (int) src.getStarts().get(target);

                    // if weight is zero, remove the edge
                    src.removeEdge(target, true);
                    tgt.removeEdge(source, false);

                    // if weight is nonzero, update the weights
                    if (weight != 0) {
                        src.connectEdge(target, weight, true);
                        tgt.connectEdge(source, weight, false);
                    }

                    checkRep();
                    return previousWeight;
                }
            }
        }

        // if edge does not exists and weight is nonzero, add the edge
        if (weight != 0) {
            boolean sourceExists = false;
            boolean targetExists = false;

            // if source and/or target already exists in the graph, connect the edge
            for (Vertex v: vertices) {
                // if source exists, connect it to target
                if (v.getValue().equals(source)) {
                    sourceExists = true;
                    v.connectEdge(target, weight, true);
                }

                // if target exists, connect it to source
                if (v.getValue().equals(target)) {
                    targetExists = true;
                    v.connectEdge(source, weight, false);
                }
            }

            // if source does not exist, create a new vertex and connect it to target
            if (!sourceExists) {
                Vertex sourceVertex = new Vertex<L>(source);
                sourceVertex.connectEdge(target, weight, true);
                vertices.add(sourceVertex);
            }

            // if target does not exist, create a new vertex and connect it to source
            if (!targetExists) {
                Vertex targetVertex = new Vertex<L>(target);
                targetVertex.connectEdge(source, weight, false);
                vertices.add(targetVertex);
            }
        }

        checkRep();
        return previousWeight;
    }
    
    @Override
    public boolean remove(L vertex) {

        // find the vertex in vertices List
        for (Vertex v: vertices) {
            // if vertex is found
            if (v.getValue().equals(vertex)) {
                // get the labels of vertices that starts at v
                Set<L> startsAtV = v.getStarts().keySet();

                // get the labels of vertices that ends at v
                Set<L> endsAtV = v.getEnds().keySet();

                // remove the connection of v from the vertices which starts or ends at v
                for (Vertex vert: vertices) {
                    if (startsAtV.contains(vert.getValue())) {
                        vert.removeEdge(v.getValue(), false);
                    }

                    if (endsAtV.contains(vert.getValue())) {
                        vert.removeEdge(v.getValue(), true);
                    }
                }
                // remove the vertex v and return true
                vertices.remove(v);

                checkRep();
                return true;
            }
        }

        // return false, if vertex does not exist in the vertices List
        checkRep();
        return false;
    }
    
    @Override
    public Set<L> vertices() {
        // initialize a set to store the labels of all the vertices
        Set<L> result = new HashSet<>();

        // store the labels of vertices in the set
        for (Vertex v: vertices) {
            result.add( (L) v.getValue());
        }

        return result;
    }
    
    @Override
    public Map<L, Integer> sources(L target) {
        // initialize Map to store the result
        Map<L, Integer> result = new HashMap<>();

        // find the target vertex in vertices List
        for (Vertex v: vertices) {
            // store the sources (or the vertices that ends at target) in the map
            if (v.getValue().equals(target)) {
                result = v.getEnds();
                break;
            }
        }
        return result;
    }
    
    @Override
    public Map<L, Integer> targets(L source) {
        // initialize Map to store the result
        Map<L, Integer> result = new HashMap<>();

        // find the source vertex in the vertices List
        for (Vertex v: vertices) {
            // store the targets (or the vertices that starts at the source) in the map
            if (v.getValue().equals(source)) {
                result = v.getStarts();
                break;
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        // initialize empty String
        String str = "";

        // add all the vertices' String representation to str
        for (Vertex v: vertices) {
            str += v.toString() + "\n";
        }
        return str;
    }
}

/**
 * A Vertex of weighted-directed graph.
 * Vertex contains the information of edges that starts/ ends from/ to it.
 *
 * Vertex is Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    
    // fields
    private L value;
    private Map<L, Integer> starts;
    private Map<L, Integer> ends;

    
    // Abstraction function:
    //      Represents a vertex of weighted-directed graph.
    //   
    // Representation invariant:
    //      values of starts and ends map are > 0 (i.e positive weights)
    //      no edge can connect vertex to itself i.e !starts.containsKey(value) && !ends.containsKey(value)
    //      
    // Safety from rep exposure:
    //      all fields are private and immutable.
    
    /**
     * Creates a new instance of Vertex.
     *
     * @param value value of the vertex
     */
    public Vertex(L value) {
        this.value = value;

        // initialize empty Maps for starts and ends edges
        starts = new HashMap<>();
        ends = new HashMap<>();

        checkRep();
    }

    /**
     * Checks that representation invariant is true.
     */
    private void checkRep() {
        // check that edges that starts from the current vertex have positive weight
        for (Map.Entry edge: starts.entrySet()) {
            assert (int) edge.getValue() > 0;
        }

        // check that edges that ends at the current vertex have positive weight
        for (Map.Entry edge: ends.entrySet()) {
            assert (int) edge.getValue() > 0;
        }

        // no edge can connect the vertex to itself
        assert !starts.containsKey(value) && !ends.containsKey(value);
    }

    /**
     * Connects an edge to the current vertex.
     *
     * @param value value of vertex to which edge is connected at other end.
     * @param weight weight of the edge
     * @param isStart true if the edge starts from current vertex,
     *               false if the edge ends at current vertex.
     * 
     * @return false if the edge is already connected to current vertex, true otherwise.
     */
    public boolean connectEdge(L value, Integer weight, boolean isStart) {
        assert weight > 0;

        // if such an edge already exists, return false
        if (isStart && starts.containsKey(value) || (!isStart) && ends.containsKey(value)) {
            return false;
        } 

        // connect edge to the vertex and return true
        if (isStart) {
            starts.put(value, weight);
        } else {
            ends.put(value, weight);
        }

        checkRep();
        return true;
    }

    /**
     * Removes an edge from the current vertex. 
     *
     * @param value value of vertex to which edge is connected at other end.
     * @param isStart true if the edge starts from current vertex,
     *               false if the edge ends at current vertex.
     *
     * @return false if the edge is does not exist, true otherwise.
     */
    public boolean removeEdge(L value, boolean isStart) {
        // if such an edge does not exist, return false
        if (isStart && (!starts.containsKey(value)) || (!isStart) && (!ends.containsKey(value))) {
            return false;
        }

        // remove the edge connection from the vertex
        if (isStart) {
            starts.remove(value);
        } else {
            ends.remove(value);
        }

        checkRep();
        return true;
    }

    /**
     * @return value of the vertex.
     */
    public L getValue() {
        return value;
    }

    /**
     * Sets the value of current vertex.
     *
     * @param value new value of current vertex.
     */
    public void setValue(L value) {
        this.value = value;
        checkRep();
    }

    /**
     * @return edges that starts from the current vertex.
     */
    public Map<L, Integer> getStarts() {
        return starts;
    }

    /**
     * @return edges that ends at the current vertex.
     */
    public Map<L, Integer> getEnds() {
        return ends;
    }
    
    /**
     * @return String representation of the object of Vertex class.
     */
    public String toString() {
        // add value to the string
        String str = "Value = " + getValue() + "\n\n";

        if (starts.isEmpty()) {
            str += "No edges starts from this vertex!\n";
        } else {
            // add the edges that starts from the current vertex
            str += "-- Edges starting from current vertex --\n";
            for (Map.Entry edge: starts.entrySet()) {
                str += "--" + edge.getValue() + "-> " + edge.getKey() + "\n";
            }
        }

        if (ends.isEmpty()) {
            str += "No edges ends at this vertex!\n";
        } else {
            // add the edges that ends at the current vertex
            str += "-- Edges ending at current vertex --\n";
            for (Map.Entry edge: ends.entrySet()) {
                str += edge.getKey() + " --" + edge.getValue() + "->\n";
            }
        }
        return str;
    }
}
