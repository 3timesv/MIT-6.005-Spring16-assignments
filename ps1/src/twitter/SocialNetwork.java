/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        // initialize result
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();

        // iterate over tweets
        for (Tweet tweet: tweets) {
            // get the usernames mentioned in the tweet
            Set<String> usernames = Extract.getMentionedUsers(Arrays.asList(tweet));

            // if there are no usernames in the tweet, continue to next tweet
            if (usernames.isEmpty()) {
                continue;
            }

            Set<String> value;
            // if the map contains the tweet's author as key
            if (contains(map.keySet(), tweet.getAuthor())) {

                // get the corresponding value
                value = getValue(map, tweet.getAuthor());
                // add the usernames mentioned in the tweet to the value
                for (String user: usernames) {
                    if (!contains(value, user)) {
                        value.add(user);
                    }
                }
                // replace the old value with new value
                replaceValue(map, tweet.getAuthor(), value);
                // otherwise
            } else {
                // create new set to map with the tweet's author
                value = new HashSet<String>();
                // add the usernames mentioned in the tweet to the value
                for (String user: usernames) {
                    if (!contains(value, user)) {
                        value.add(user);
                    }
                }
                // if the value is not empty, put the entry into the map
                if (!value.isEmpty()) {
                    map.put(tweet.getAuthor(), value);
                }
            }
        }
        return map;
    }

    // helper method to check whether a Set contains a target element (keeping in mind the case-insensitive criteria)
    public static boolean contains(Set<String> collection, String target) {
        for (String element: collection) {
            if (element.toLowerCase().equals(target.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // helper method to get the corresponding value associated with target key in the map (keeping in mind the case-insensitive criteria)
    public static Set<String> getValue(Map<String, Set<String>> map, String targetKey) {
        for (String key: map.keySet()) {
            if (key.toLowerCase().equals(targetKey.toLowerCase())) {
                return map.get(key);
            }
        }
        return new HashSet<String>();
    }

    // helper method to replace the value associated with targetKey with new value in the map (keeping in mind the case_insensitive criteria) 
    public static void replaceValue(Map<String, Set<String>> map, String targetKey, Set<String> value) {
        for (String key: map.keySet()) {
            if (key.toLowerCase().equals(targetKey.toLowerCase())) {
                map.replace(targetKey, value);
                break;
            }
        }
    }

    // helper function to increment the corresponding value of the key by 1 in the map (keeping in mind the case-insensitive criteria)
    public static void increment(Map<String, Integer> map, String key) {
        for (String k: map.keySet()) {
            if (k.toLowerCase().equals(key.toLowerCase())) {
                map.replace(k, map.get(k)+1);
                break;
            }
        }
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        // if the graph is empty, return empty List 
        if (followsGraph.isEmpty()) {
            return new ArrayList<String>();
        }

        // initialize an empty map to store the username and corresponding number of followers
        Map<String, Integer> followers = new HashMap<>();

        // fill the followers map 
        for (String key: followsGraph.keySet()) {
            // get all the usernames that follows key
            Set<String> usernames = followsGraph.get(key);
            // iterate over usernames 
            for (String user: usernames) {
                // if username is already in the followers map, increment its value by 1
                if (contains(followers.keySet(), user)) {
                    increment(followers, user);
                    // otherwise, put the username in followers map with value 1
                } else {
                    followers.put(user, 1);
                }
            }
        }

        // initialize result ArrayList to store usernames by descending order of number of followers
        List<String> result = new ArrayList<>();

        // iterate over followers' keys
        for (String key: followers.keySet()) {
            // get the corresponding value
            int value = followers.get(key);

            // if result list is empty, add current key as the only element and continue to next element
            if (result.isEmpty()) {
                result.add(key);
                continue;
            } 

            // get the first and last element of the result
            String firstElement = result.get(0);
            String lastElement = result.get(result.size()-1);

            // if the element is larger than the first element, add it to the start of the result list
            if (followers.get(firstElement) <= value) {
                result.add(0, key);
                continue;
            }

            // if the element is less than or equal to the last element, add it to the end of the result list
            if (followers.get(lastElement) >= value) {
                result.add(result.size(), key);
                continue;
            }

            // otherwise, look at all the elements in result list and add the key at index i such that
            //      the key at index i-1 has more followers than key at index i and 
            //      the key at index i+1 has less followers than key at index i
            for (int i=1; i < result.size()-1; i++) {
                if (value <= followers.get(result.get(i-1)) && value >= followers.get(result.get(i+1))) {
                    result.add(i, key);
                    break;
                }
            }
        }
        // return the sorted list
        return result;
    }
}
