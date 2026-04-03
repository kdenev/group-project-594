package edu.upenn.cit5940.processor;

//import any classes you will need
import java.util.*;


public class InvertedIndex {

    // Root of the BST
    private BSTNode root;

    // define a private static inner class that represents a node in the BST
    private static class BSTNode {
        // keyWord that is indexed
        String keyWord;
        // set of IDs where the keyWord appears
        Set<Integer> documentIDs;
        // the left node stores keywords less than this node's keyword
        // the right node stores keywords greater than this node's keyword
        BSTNode left, right;

        // constructor to initialize each node
        BSTNode(String keyWord, int docID) {
            this.keyWord = keyWord;
            this.documentIDs = new HashSet<>();
            this.documentIDs.add(docID);
        }
    }

    // DO NOT CHANGE THE FOLLOWING SET OF STOP_WORDS
    private static final Set<String> STOP_WORDS = Set.of(
            "i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your",
            "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she",
            "her", "hers", "herself", "it", "its", "itself", "they", "them", "their",
            "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being",
            "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an",
            "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through",
            "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off",
            "over", "under", "again", "further", "then", "once", "here", "there", "when",
            "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such",
            "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just",
            "don", "should", "now", "said", "announced", "company", "industry", "technology", "system", "application",
            "software", "update", "service");

    /*
     * This method adds a document
     * 
     * @param int docID, String text
     * 
     * @return no return
     */
    // addDocument
    public void addDocument(int docID, String text) {

        // Handle null input
        if (text == null)
            return;

        // Get the words
        HashSet<String> tokens = tokenizeText(text);

        for (String t : tokens) {

            if (t == null || t == "")
                continue;

            if (this.root == null) {

                this.root = new BSTNode(t, docID);
            } else {

                addWord(t, docID, this.root);
            }

        }

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
    public Set<Integer> search(String query) {

        Set<Integer> returnSet = new HashSet<>();

        if (query == null || this.root == null || query.equals(""))
            return returnSet;

        HashSet<String> processedQuery = tokenizeText(query);

        for (String word : processedQuery) {

            BSTNode node = traverseNode(this.root, word);

            if (node.documentIDs.contains(-1))
                continue;

            if (returnSet.isEmpty()) {

                returnSet.addAll(node.documentIDs);

            } else {

                returnSet.retainAll(node.documentIDs);

            }

        }

        return returnSet;

    }

    /*
     * This method removes a document based on the docID
     * 
     * @param int docID
     * 
     * @return void
     */
    // to remove a document traverse the entire tree and remove the given docID from
    // the node's set
    // remove the document ID
    public void removeDocument(int docID) {

        // There is no docid with 0
        if (docID == 0)
            return;

        removeDocID(this.root, docID);

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
    public Map<String, Set<Integer>> getIndex() {

        Map<String, Set<Integer>> indexMap = new HashMap<>();

        traverseMap(this.root, indexMap);

        return indexMap;
    }

    /*
     * TODO: Implement helper methods below
     */

    private boolean addWord(String token, int docID, BSTNode node) {

        // Word already in the map
        if (node.keyWord.compareTo(token) == 0) {

            node.documentIDs.add(docID);
            return false;

            // Token higher value
        } else if (node.keyWord.compareTo(token) < 0) {

            if (node.right == null) {

                node.right = new BSTNode(token, docID);
                return true;

            } else {

                return addWord(token, docID, node.right);

            }

            // Token lower value
        } else {

            if (node.left == null) {

                node.left = new BSTNode(token, docID);
                return true;

            } else {

                return addWord(token, docID, node.left);

            }

        }

    }

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

    private void traverseMap(BSTNode node, Map<String, Set<Integer>> indexMap) {

        if (node == null)
            return;

        indexMap.put(node.keyWord, node.documentIDs);

        traverseMap(node.left, indexMap);
        traverseMap(node.right, indexMap);

    }

    private BSTNode traverseNode(BSTNode node, String query) {

        if (node == null)
            return new BSTNode(query, -1);

        if (node.keyWord.equals(query)) {

            return node;

        } else if (node.keyWord.compareTo(query) > 0) {

            return traverseNode(node.left, query);

        } else {

            return traverseNode(node.right, query);

        }

    }

    private void removeDocID(BSTNode node, Integer id) {

        if (node == null)
            return;

        if (node.documentIDs != null && node.documentIDs.contains(id))
            node.documentIDs.remove(id);

        removeDocID(node.left, id);
        removeDocID(node.right, id);

    }

}
