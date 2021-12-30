/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class ExtractTest {
    /* Testing Strategy
     * 
     *  Partition for getTimespan(tweets) -> timespan
     *
     *  tweets.length : 1, >1
     *  tweets having same timestamp or different timestamps
     *
     *
     *  Partition for getMentionedUsers(tweets) -> Set of usernames
     *
     *  tweets.length : 1, >1
     *  tweets having usernames or doesnt
     *  text of tweet contains username at start, at end or somewhere between start and end
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alex", "@alyssa sleeping...", d1);
    private static final Tweet tweet4 = new Tweet(4, "caro", "baby shark dududu.. @alex", d1);
    private static final Tweet tweet5 = new Tweet(5, "cedric", "mama @caro shark dududu..", d1);
    private static final Tweet tweet6 = new Tweet(6, "tim", "@alice fifi, @bob kl @dani", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

    // covers tweets.length = 1
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    // covers tweets.length > 1 and tweets have same timestamps
    @Test
    public void testGetTimespanSameTimestamps() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    // covers tweets.length > 1 and tweets have different timestamps
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    // covers tweets.length = 1 and tweets having no usernames
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    // covers tweets.length = 1 and tweets having username at the start
    @Test
    public void testGetMentionedUsersMentionAtStart() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("alyssa");

        assertEquals("expected usernames", expectedUsers, mentionedUsers);
    }

    // covers tweets.length = 1 and tweets having username at the end
    @Test
    public void testGetMentionedUsersMentionAtEnd() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("alex");
        assertEquals("expected usernames", expectedUsers, mentionedUsers);
    }

    // covers tweets.length = 1 and tweets having username somewhere between start and end
    @Test
    public void testGetMentionedUsersMentionInBetween() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("caro");

        assertEquals("expected usernames", expectedUsers, mentionedUsers);

    }

    // covers tweets.length > 1 and tweets having no usernames
    @Test
    public void testGetMentionedUsersTwoTweetsNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());

    }

    // covers tweets.length > 1 and tweets having username at the start
    @Test
    public void testGetMentionedUsersTwoTweetsMentionAtStart() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet1));

        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("alyssa");

        assertEquals("expected usernames", expectedUsers, mentionedUsers);

    }

    // covers tweets.length > 1 and tweets having username at the end
    @Test
    public void testGetMentionedUsersTwoTweetsMentionAtEnd() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4, tweet1));

        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("alex");

        assertEquals("expected usernames", expectedUsers, mentionedUsers);

    }

    // covers tweets.length > 1 and tweets having username somewhere between start and end
    @Test
    public void testGetMentionedUsersTwoTweetsMentionInBetween() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5, tweet1));

        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("caro");

        assertEquals("expected usernames", expectedUsers, mentionedUsers);
    }

    // covers tweets.length > 1 and tweets having usernames at start, at end and also somewhere in between
    @Test
    public void testGetMentionedUsersTwoTweetsMentionAllThree() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4, tweet6));
        Set<String> expectedUsers = new HashSet<>();
        expectedUsers.add("alice");
        expectedUsers.add("bob");
        expectedUsers.add("dani");
        expectedUsers.add("alex");

        assertEquals("expected usernames", expectedUsers, mentionedUsers);
    }
}
