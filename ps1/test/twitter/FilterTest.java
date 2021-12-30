/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /* Testing strategy
     *
     * Partition for writtenBy(tweets, username) -> tweets by user with given username
     *
     * tweets.length: 1, > 1
     * tweets contain 0, 1 or >1 tweets by username
     *
     *
     * Partition for inTimespan(tweets, timespan) -> tweets sent during the timespan
     *
     * tweets.length: 1, >1
     * tweets contains 0, 1 or >1 tweets sent during the timespan 
     *
     * 
     * Partition for containing(tweets, words) -> tweets containing atleast one word from words
     * tweets.length: 1, >1
     * words.length: 1, >1
     * tweet contains atleast one word or doesn't
     * result contains 0, 1, >1 tweets
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "can we stop talking about rivest?", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

    // covers tweets.length = 1 and tweets doesn't contains tweets by username
    @Test
    public void testWrittenBySingleTweetEmptyResult() {
        List<Tweet> trueResult = Filter.writtenBy(Arrays.asList(tweet2), "alyssa");

        assertEquals("expected list of size 0", 0, trueResult.size());
    }

    // covers tweets.length = 1 and tweets contain tweets by username
    @Test
    public void testWrittenBySingleTweetSingleResult() {
        List<Tweet> trueResult = Filter.writtenBy(Arrays.asList(tweet1), "alyssa");

        assertEquals("expected singleton list", 1, trueResult.size());
        assertTrue("expected list to contian tweet", trueResult.contains(tweet1));
    }

    // covers tweets.length > 1 and tweets doesn't contains tweets by username
    @Test
    public void testWrittenByMultipleTweetsEmptyResult() {
        List<Tweet> trueResult = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alex");

        assertEquals("expected list of size 0", 0, trueResult.size());
    }

    // coverts tweets.length > 1 and tweets contain single tweet by username
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    // covers tweets.length > 1 and tweets contain multiple tweets by username
    @Test
    public void testWrittenByMultipleTweetsMultipleResult() {
        List<Tweet> trueResult = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        
        assertEquals("expected list of size 2", 2, trueResult.size());
        assertTrue("expected list to contain tweet", trueResult.contains(tweet1));
        assertTrue("expected list to contain tweet", trueResult.contains(tweet3));
        assertTrue("expected result in same order as in input", trueResult.indexOf(tweet1) < trueResult.indexOf(tweet3));
    }


    // covers tweets.length = 1 and tweets contain no tweets sent during the timespan
    @Test
    public void testInTimespanSingleTweetEmptyResult() {
        Instant testStart = Instant.parse("2016-02-17T08:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T09:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(testStart, testEnd));

        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    // covers tweets.length = 1 and tweets contain 1 tweet sent during the timespan
    @Test
    public void testInTimespanSingleTweetSingleResult() {
        Instant testStart = Instant.parse("2016-02-17T08:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1), new Timespan(testStart, testEnd));

        assertEquals("expected singleton list",1, inTimespan.size());
        assertTrue("expected list to contain tweet", inTimespan.contains(tweet1));
    }

    // covers tweets.length > 1 and tweets contain no tweet sent during the timespan
    @Test
    public void testInTimespanMultipleTweetsEmptyResult() {
        Instant testStart = Instant.parse("2016-02-17T08:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T09:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    // covers tweets.length > 1 and tweets contain 1 tweet sent during the timespan
    @Test
    public void testInTimspanMultipleTweetsSingleResult() {
        Instant testStart = Instant.parse("2016-02-17T08:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertEquals("expected singleton list",1, inTimespan.size());
        assertTrue("expected list to contain tweet", inTimespan.contains(tweet1));

    }
    
    // covers tweets.length > 1 and tweets contain >1 tweets sent during the timespan
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    // covers tweets.length = 1, words.length = 1, and tweets contain 0 words from list of words
    @Test
    public void testContainingSingleTweetSingleWordEmptyResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList("alex"));

        assertTrue("expected empty list", containing.isEmpty());
    }

    // covers tweets.length = 1, words.length = 1, and tweets contain atleast 1 word from list of words
    @Test
    public void testContainingSingleTweetSingleWordSingleResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList("talk"));

        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
    }

    // covers tweets.length = 1, words.length > 1, and tweets contain 0 words from list of words
    @Test
    public void testContainingSingleTweetMultipleWordsEmptyResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList("alice", "bob"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }

    // covers tweets.length = 1, words.length > 1, and tweets contain atleast 1 word from list of words
    @Test
    public void testContainingSingleTweetMultipleWordsSingleResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1), Arrays.asList("alice", "talk"));

        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
    }

    // covers tweets.length > 1, words.length = 1, and tweets contain 0 words from list of words
    @Test
    public void testContainingMultipleTweetsSingleWordEmptyResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("alex"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }

    // covers tweets.length > 1, words.length = 1, tweets contain atleast 1 word from the list of words, and result contains multiple tweets
    @Test
    public void testContainingMultipleTweetsSingleWordMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    // covers tweets.length > 1, words.length > 1, and tweets contain 0 words from list of words
    @Test
    public void testContainingMultipleTweetsMultipleWordsEmptyResult() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("alex", "bob"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }

    // covers tweets.length > 1, words.length > 1, tweets contain atleast 1 word from list of words, and result contains multiple tweets
    @Test
    public void testContainingMultipleTweetsMultipleWordsMultipleResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("Talk", "in"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
}
