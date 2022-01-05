/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    //
    // GraphPoet() :
    // graph contains edges with weights = 1, >1
    // file exist or doesn't exist
    // 
    // GraphPoet.poem() :
    // bridge words are inserted in the poem or doesn't
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // GraphPoet() : file does not exist
    @Test
    public void testGraphPoetFileDoesNotExist() {
        File corpusFile = new File("fileDoesNotExist.txt");
        try {
            GraphPoet gp = new GraphPoet(corpusFile);
            fail("expected IOException to be thrown");
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    // GraphPoet() : graph contains edges with weights = 1
    @Test
    public void testGraphPoetWeightsOne() {
        File corpusFile = new File("poet/tiny_corpus1.txt");
        try {
            GraphPoet gp = new GraphPoet(corpusFile);
            assertTrue("expected all vertices in graph",
                    gp.getVertices().contains("No") &&
                    gp.getVertices().contains("try") &&
                    gp.getVertices().contains("not"));

            assertTrue("expected edges with weights 1",
                    gp.getTargets("No").containsKey("try") &&
                    gp.getTargets("No").get("try") == 1 &&
                    gp.getTargets("try").containsKey("not") &&
                    gp.getTargets("try").get("not") == 1);

        } catch(IOException e) {
            e.printStackTrace();
            fail("expected no exception");
        }
    }

    // GraphPoet() : graph contains edges with some edge weights >1
    @Test
    public void testGraphPoetWeightsGreaterThanOne() {
        File corpusFile = new File("poet/tiny_corpus2.txt");
        try {
            GraphPoet gp = new GraphPoet(corpusFile);
            assertTrue("expected all vertices in graph",
                    gp.getVertices().contains("No") &&
                    gp.getVertices().contains("try") &&
                    gp.getVertices().contains("not"));

            assertTrue("expected edges with weights",
                    gp.getTargets("No").containsKey("try") &&
                    gp.getTargets("No").get("try") == 2 &&
                    gp.getTargets("try").containsKey("not") && 
                    gp.getTargets("try").get("not") == 2 &&
                    gp.getTargets("not").containsKey("No") &&
                    gp.getTargets("not").get("No") == 1);
        } catch(IOException e) {
            e.printStackTrace();
            fail("expected no exception");
        }

    }


    // GraphPoet.poem(): bridge words are inserted in the poem
    @Test
    public void testPoemBridgeWordsInserted() {
        File corpusFile = new File("poet/inspirational_corpus.txt");
        try {
            GraphPoet gp = new GraphPoet(corpusFile);
            String poemInput = "Seek to explore new and exciting synergies!";
            String expectedPoem = "Seek to explore strange new life and exciting synergies!";

            String truePoem = gp.poem(poemInput);

            assertEquals("expected same poems", expectedPoem, truePoem);
        } catch(IOException e) {
            e.printStackTrace();
            fail("expected no exception");
        }
    }

    // GraphPoet.poem(): bridge words are not inserted in the poem
    @Test
    public void testPoemBridgeWordsNonInserted() {
        File corpusFile = new File("poet/inspirational_corpus_2.0.txt");
        try {
            GraphPoet gp = new GraphPoet(corpusFile);
            String poemInput = "No way!";
            String truePoem = gp.poem(poemInput);

            assertEquals("expected output to be same as input", poemInput, truePoem);
        } catch(IOException e) {
            e.printStackTrace();
            fail("expected no exception");
        }
    }
}
