/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.ArrayList;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        // initialize result list
        List<Tweet> result = new ArrayList<Tweet>();

        // iterate over the given list of tweets
        for (int i=0; i < tweets.size(); i++) {
            // if tweet at index i is written by author == username, add the tweet to result
            if (tweets.get(i).getAuthor().toLowerCase().equals(username.toLowerCase())) {
                result.add(tweets.get(i));
            }
        }

        return result;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        // initialize result list
        List<Tweet> result = new ArrayList<Tweet>();

        // iterate over the given list of tweets
        for (int i=0; i < tweets.size(); i++) {
            // if tweet at index i is posted during the timespan, add the tweet to result
            if (tweets.get(i).getTimestamp().isAfter(timespan.getStart()) && tweets.get(i).getTimestamp().isBefore(timespan.getEnd())) {
                result.add(tweets.get(i));
            }
        }

        return result;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        // initialize result list
        List<Tweet> result = new ArrayList<Tweet>();

        // iterate over the given tweets
        for (int i=0; i < tweets.size(); i++) {
            // get current tweet
            Tweet currentTweet = tweets.get(i);

            // split the tweet into words
            String[] tweetWords = currentTweet.getText().split(" ");

            boolean contains = false;

            // iterate over the words in the current tweet
            for (int j=0; j < tweetWords.length; j++) {
                // get current word
                String currentWord = tweetWords[j];

                // iterate over the given list of words
                for (int k=0; k < words.size(); k++) {
                    // if currentWord is equal to word at index k in given list of words
                    if (currentWord.toLowerCase().equals(words.get(k).toLowerCase())) {
                        // set contains to true and break out of the loop
                        contains = true;
                        break;
                    }
                }
                // if currentWord is in given list of words, don't continue to compare any further words
                if (contains) {
                    break;
                }
            }
            // if the tweet contains atleast one word, add the tweet to result
            if (contains) {
                result.add(currentTweet);
            }
        }
        return result;
    }
}
