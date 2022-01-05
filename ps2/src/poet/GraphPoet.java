/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //      Represents a word affinity graph. Vertices in the graph are words (non-empty case-insensitive strings).
    //      Edges are the count of adjacencies between two words.
    //
    // Representation invariant:
    //      Vertices in the graph are non-empty case-insensitive strings.
    //      
    // Safety from rep exposure:
    //      graph is private, and no method shares the graph with the clients.
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        // read all the lines from the corpus in a list
        List<String> lines = Files.readAllLines(corpus.toPath());

        // store all the words of the corpus in a list
        List<String> words = new ArrayList<>();

        for (String line: lines) {
            String[] lineWords = line.split(" ");
            for (int i=0; i < lineWords.length; i++) {
                words.add(lineWords[i]);
            }
        }

        // iterate over pair of words
        for (int i=0; i < words.size()-1; i++) {
            // get a pair
            String first = words.get(i);
            String second = words.get(i+1);

            // check if graph contains the first word
            if (contains(graph.vertices(), first)) {
                // check if first word is already connected to the second
                if (contains(graph.targets(first).keySet(), second)) {
                    // get the weight of the edge between first and second and increment by 1
                    graph.set(first, second, graph.targets(first).get(second) + 1);
                } else {
                    // connect the first word with second and assign weight 1 to the edge
                    graph.set(first, second, 1);
                }
                // connect the first word with second and assign weight 1 to the edge
            } else {
                graph.set(first, second, 1);
            }
        }

        checkRep();
    }

    /**
     * @return the set of all vertices in the graph.
     */
    public Set<String> getVertices() {
        return graph.vertices();
    }

    /**
     * Get the source vertices with directed edges to a target vertex and the weights of those edges.
     *
     * @param target a label
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from that vertex to target, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         the key to target
     */
    public Map<String, Integer> getSources(String target) {
        return graph.sources(target);
    }
    
    /**
     * Get the target vertices with directed edges from a source vertex and the
     * weights of those edges.
     * 
     * @param source a label
     * @return a map where the key set is the set of labels of vertices such
     *         that this graph includes an edge from source to that vertex, and
     *         the value for each key is the (nonzero) weight of the edge from
     *         source to the key
     */
    public Map<String, Integer> getTargets(String source) {
        return graph.targets(source);
    }

    /**
     * Check if set of words contains a target word (case-insensitive).
     *
     * @param words Set of words
     * @Param target target word to search for
     *
     * @return true if set contains the target word, false otherwise.
     */
    private boolean contains(Set<String> words, String target) {
        for (String w: words) {
            if (w.toLowerCase().equals(target.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    
    // checkRep
    private void checkRep() {
        // initialize an empty Set to add lowercase words
        Set<String> lowercaseWords = new HashSet<>();
        for (String word: graph.vertices()) {
            // length must be non-zero
            assert word.length() != 0;
            lowercaseWords.add(word.toLowerCase());
        }

        // the size of set of all words in lowercase must be equals set of all words
        // i.e No two words are same (case-insensitive) 
        assert lowercaseWords.size() == graph.vertices().size();
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] words = input.split(" ");
        String result = words[0] + " ";

        for (int i=0; i < words.length-1; i++) {
            // get a pair of adjacent words
            String first = words[i];
            String second = words[i+1];
            String bridgeWord = "";

            // check whether both words exist in graph
            if (contains(graph.vertices(), first) && contains(graph.vertices(), second)) {
                // check if there is two edge path between them
                if (twoEdgePathExists(first, second)) {
                    // get the middle word from the path (from first to second) which has maximum weight
                    bridgeWord = findMaxWeightMidWord(first, second);
                    result += bridgeWord + " ";
                }
            }
            result += second + " ";
        }
        return result.trim();
    }

    /**
     * Find is two-edge path exists between two vertices.
     *
     * @param first source vertex
     * @param second target vertex
     *
     * @return true if two-edge path exists, false otherwise.
     */
    private boolean twoEdgePathExists(String first, String second) {
        // if there's any vertex in the middle of first and second, return true
        for (String midWord: graph.targets(first).keySet()) {
            if (contains(graph.targets(midWord).keySet(), second)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the weight associated with target word.
     *
     * @param map mapping of words to their corresponding weights.
     * @param target target word.
     */
    private int getWeight(Map<String, Integer> map, String target) {
        for (String w: map.keySet()) {
            if (w.toLowerCase().equals(target.toLowerCase())) {
                return map.get(w);
            }
        }
        return -1;
    }

    /**
     * Find the maximum weight two-edge path between two vertices.
     *
     * @param first source vertex
     * @param second target vertex
     *
     * @return String middle vertex on the path with maximum weight.
     */
    private String findMaxWeightMidWord(String first, String second) {
        // initialize map to store all the middle words between first and second and the weight of the path
        Map<String, Integer> midWords = new HashMap<>();

        // fill the midWords map
        for (String midWord: graph.targets(first).keySet()) {
            if (contains(graph.targets(midWord).keySet(), second)) {
                midWords.put(midWord, getWeight(graph.targets(first), midWord) + getWeight(graph.targets(midWord), second));
            }
        }

        // find the mid word on the maximum weight two-edge path
        int maxWeight = -1;
        String result = "";
        for (Map.Entry entry: midWords.entrySet()) {
            if ((int) entry.getValue() > maxWeight) {
                maxWeight = (int) entry.getValue();
                result = (String) entry.getKey();
            }
        }

        return result;
    }
    
    /**
     * @return String representation of the GraphPoet.
     */
    public String toString() {
        return graph.toString();
    }
}
