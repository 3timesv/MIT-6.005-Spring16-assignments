/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        // if list is empty, raise AssertionError
        assert !tweets.isEmpty();

        // initialize variables to store first tweet and last tweet (compared by timestamp)
        Tweet firstTweet = tweets.get(0);
        Tweet lastTweet = tweets.get(0);

        // iterate over the list of tweets
        for (int i=1; i < tweets.size(); i++) {
            // if current tweet's timestamp is before firstTweet's timestamp
            if (tweets.get(i).getTimestamp().isBefore(firstTweet.getTimestamp())) {
                // set current tweet as firstTweet
                firstTweet = tweets.get(i);
            }
            
            // if current tweet's timestamp is after firstTweet's timestamp
            if (tweets.get(i).getTimestamp().isAfter(lastTweet.getTimestamp())) {
                // set current tweet as lastTweet
                lastTweet = tweets.get(i);
            }
        }

        // return Timespan with starting time as timestamp of firstTweet
        // and ending time as timestamp of lastTweet
        return new Timespan(firstTweet.getTimestamp(), lastTweet.getTimestamp());
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        // if list is empty, raise AssertionError
        assert !tweets.isEmpty();

        // initialize resulting Set of usersnames
        Set<String> mentionedUsers = new HashSet<>();

        for (int i=0; i < tweets.size(); i++) {
            // get the text of tweet at index i
            String text = tweets.get(i).getText();

            
            // initialize start and end indices
            int startIndex;
            int endIndex;

            // run while loop if there's @ in the text
            while (text.indexOf('@') != -1) {
                // get the index of '@'
                startIndex = text.indexOf('@');

                // get the index of ' ' after startIndex, otherwise set endIndex as length of text
                if (text.substring(startIndex).contains(" ")) {
                    endIndex = text.indexOf(' ', startIndex);
                } else {
                    endIndex = text.length();
                }

                // add the username to result
                mentionedUsers.add(text.substring(startIndex+1, endIndex));

                // if there are no more characters in text, set text as empty string
                if (endIndex >= text.length()) {
                    text = "";
                }
                // otherwise, set text as substring from endIndex of last added username to end of the text
                else {
                    text = text.substring(endIndex+1);
                }

            }
        }

        return mentionedUsers;
    }
}
