/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.time.Instant;
import java.util.Arrays;

import org.junit.Test;

public class SocialNetworkTest {

    /*  Testing strategy
     *
     *  Partition for guessFollowsGraph(tweets) -> Map<String, Set<String>>
     *
     *  tweets.length = 0, 1, >1
     *  Nobody follows nobody, everyone follows everyone else, >=1 users follow >=1 other users
     *
     *
     *  Partition for influencers(Map<String, Set<String>> followsGraph) -> List<String>
     *
     *  followsGraph is empty or non-empty
     *  resulting list is empty or non-empty
     */

    // covers followsGraph is empty and resulting list is empty

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alex", "baby shark dudududu...", d1);
    private static final Tweet tweet4 = new Tweet(4, "tim", "@cedric yes @fynn", d2);

    private static final Tweet tweet5 = new Tweet(5, "cedric", "@tim yes @fynn", d2);
    private static final Tweet tweet6 = new Tweet(6, "fynn", "@tim yes @cedric", d2);
    private static final Tweet tweet7 = new Tweet(7, "Fynn", "@Tim is a good guy :)", d2);

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // covers tweets.length = 0
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // covers tweets.length = 1 and nobody follows nobody
    @Test
    public void testGuessFollowsGraphSingleTweetEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());

        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // covers tweets.length = 1 and user follow >1 other users
    @Test
    public void testGuessFollowsGraphSingleTweetNonEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4));

        Map<String, Set<String>> expectedResult = new HashMap<String, Set<String>>();
        Set<String> followsSet = new HashSet<String>();
        followsSet.add("cedric");
        followsSet.add("fynn");
        expectedResult.put("tim", followsSet);

        assertTrue("expected non-empty graph", !followsGraph.isEmpty());
        assertEquals("expected graphs to be equal", expectedResult, followsGraph);
    }

    // covers tweets.length > 1 and nobody follows nobody
    @Test
    public void testGuessFollowsGraphMultipleTweetsEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3));

        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // covers tweets.length > 1 and everyone follows everyone else
    @Test
    public void testGuessFollowsGraphMultipleTweetsFullGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4, tweet5, tweet6));

        Map<String, Set<String>> expectedResult = new HashMap<String, Set<String>>();
        Set<String> followsSet1 = new HashSet<String>();
        followsSet1.add("cedric");
        followsSet1.add("fynn");
        expectedResult.put("tim", followsSet1);

        Set<String> followsSet2 = new HashSet<String>();
        followsSet2.add("tim");
        followsSet2.add("fynn");
        expectedResult.put("cedric", followsSet2);

        Set<String> followsSet3 = new HashSet<String>();
        followsSet3.add("tim");
        followsSet3.add("cedric");
        expectedResult.put("fynn", followsSet3);

        assertTrue("expected non-empty grpah", !followsGraph.isEmpty());
        assertEquals("expected graphs to be equal", expectedResult, followsGraph);
    }

    // covers tweets.length > 1 and >=1 users follow >=1 other users
    @Test
    public void testGuessFollowsGraphMultipleTweetsNonEmptyGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3, tweet5, tweet6, tweet7));

        Map<String, Set<String>> expectedResult = new HashMap<String, Set<String>>();
        Set<String> followsSet1 = new HashSet<String>();
        followsSet1.add("tim");
        followsSet1.add("fynn");
        expectedResult.put("cedric", followsSet1);


        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertTrue("expected key", followsGraph.containsKey("cedric"));
        assertTrue("expected same values", followsGraph.get("cedric").equals(expectedResult.get("cedric")));

        // the result can contain exactly one of the cases below:
        //      - map[fynn] = {tim, cedric};
        //      - map[fynn] = {Tim, cedric};
        //      - map[Fynn] = {tim, cedric};
        //      - map[Fynn] = {Tim, cedric};
        assertEquals("expected same number of entries", 2, followsGraph.size());

        Set<String> expectedFollows1 = new HashSet<String>();
        expectedFollows1.add("tim");
        expectedFollows1.add("cedric");

        Set<String> expectedFollows2 = new HashSet<String>();
        expectedFollows2.add("Tim");
        expectedFollows2.add("cedric");

        if (followsGraph.containsKey("fynn")) {
            if (followsGraph.get("fynn").contains("tim")) {
                assertEquals("expected same values", expectedFollows1, followsGraph.get("fynn"));
            } else  {
                assertEquals("expected same values", expectedFollows2, followsGraph.get("fynn"));
            }
        } else if (followsGraph.containsKey("Fynn")) {
            if (followsGraph.get("Fynn").contains("tim")) {
                assertEquals("expected same values", expectedFollows1, followsGraph.get("Fynn"));
            } else  {
                assertEquals("expected same values", expectedFollows2, followsGraph.get("Fynn"));
            }
        } else {
            assertTrue("expected different key", false);
        }
    }

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    // covers followsGraph is non-empty and resulting list is also non-empty
    @Test
    public void testInfluencersNonEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Set<String> timFollows = new HashSet<>();
        timFollows.add("cedric");
        timFollows.add("fynn");
        followsGraph.put("tim", timFollows);

        Set<String> fynnFollows = new HashSet<>();
        fynnFollows.add("tim");
        fynnFollows.add("Cedric");
        followsGraph.put("fynn", fynnFollows);

        List<String> result = SocialNetwork.influencers(followsGraph);

        // result can be anyone of the following (all are valid)
        List<String> expectedInfluencers1 = Arrays.asList("Cedric", "fynn", "tim");
        List<String> expectedInfluencers2 = Arrays.asList("Cedric", "tim", "fynn");
        List<String> expectedInfluencers3 = Arrays.asList("cedric", "fynn", "tim");
        List<String> expectedInfluencers4 = Arrays.asList("cedric", "tim", "fynn");

        if (result.get(0).equals("Cedric")) {
            if (result.get(1).equals("fynn")) {
                assertEquals("expected same list", expectedInfluencers1, result);
            } else {
                assertEquals("expected same list", expectedInfluencers2, result);
            }
        } else if (result.get(0).equals("cedric")) {
            if (result.get(1).equals("fynn")) {
                assertEquals("expected same list", expectedInfluencers3, result);
            } else {
                assertEquals("expected same list", expectedInfluencers4, result);
            }
        } else {
            assertTrue("wrong result", false);
        }
    }
}
