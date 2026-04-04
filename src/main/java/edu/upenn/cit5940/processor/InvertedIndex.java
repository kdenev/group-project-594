package edu.upenn.cit5940.processor;

//import any classes you will need
import java.util.*;

import edu.upenn.cit5940.datamanagement.LoadData;

public class InvertedIndex {

    // Map holds keyword and set of ids
    private Map<String, Set<String>> keywords = new HashMap<>();
    private Map<String, Set<String>> dates = new HashMap<>();

    // DO NOT CHANGE THE FOLLOWING SET OF STOP_WORDS
    private static final Set<String> STOP_WORDS = LoadData.getStopWords();

    /*
     * This method adds a document
     * 
     * @param int docID, String text
     * 
     * @return no return
     */
    // addDocument
    public void addDocument(String docID, String text, String title, String date) {

        // Handle null input
        if (text == null)
            return;

        // Get the words
        HashSet<String> tokens = tokenizeText(text);

        // Create map for keywords
        for (String t : tokens) {

            if (t == null || t == "")
                continue;

            expandMap(keywords, t, docID);

            // if (keywords.containsKey(t)) {

            // Set<String> docIds = new HashSet<>(keywords.get(t));
            // docIds.add(docID);

            // } else {

            // keywords.put(t, Set.of(docID));
            // }

        }

        // Create map for dates
        expandMap(dates, date, title);

        return;
    }

    /*
     * This method returns a set of document IDs based on the query
     * 
     * @param String query
     * 
     * @return Set<Integer>
     */
    // search
    public Set<String> search(String query) {

        Set<String> returnSet = new HashSet<>();

        if (query == null || query.equals(""))
            return returnSet;

        HashSet<String> processedQuery = tokenizeText(query);

        for (String word : processedQuery) {

            if (!keywords.containsKey(word))
                continue;

            if (returnSet.isEmpty()) {

                returnSet.addAll(keywords.get(word));

            } else {

                returnSet.retainAll(keywords.get(word));

            }

        }

        return returnSet;

    }

    /*
     * This method get the map of inverted index
     * can be used for testing purposes
     * 
     * @param none
     * 
     * @return Map<String, Set<Integer>>
     */
    // returns the map of the inverted index
    public void getArticalByIndex(String uri, Map<String, ArrayList<String>> articles) {

        // Display article information
        System.out.println("Id: " + uri);
        System.out.println("Date: " + articles.get(uri).get(0));
        System.out.println("Title: " + articles.get(uri).get(1));
        System.out.println("Body: " + articles.get(uri).get(2));

    }

    /*
     * TODO: Implement helper methods below
     */

    private HashSet<String> tokenizeText(String text) {

        String processedTest = text
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", " ")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "")
                .replaceAll("\\s-+", " ")
                .replaceAll("-+\\s", " ");

        HashSet<String> returnSet = new HashSet<>(Arrays.asList(processedTest.split("\\s+")));
        returnSet.removeAll(InvertedIndex.STOP_WORDS);

        return returnSet;
    }

    private void expandMap(Map<String, Set<String>> mapToExpand, String id, String value) {

        if (mapToExpand.containsKey(id)) {

            Set<String> values = new HashSet<>(mapToExpand.get(id));
            values.add(value);

        } else {

            mapToExpand.put(id, Set.of(value));
        }

    }

}
