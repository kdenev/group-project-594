package edu.upenn.cit5940;

import java.util.*;

import edu.upenn.cit5940.datamanagement.LoadData;
import edu.upenn.cit5940.processor.InvertedIndex;

public class Main {
    public static void main(String[] args) {

        LoadData dataLoader = new LoadData("src\\main\\java\\edu\\upenn\\cit5940\\datamanagement\\data\\articles_small.csv");
        HashMap<String, ArrayList<String>> articles = new HashMap<>(dataLoader.getArticles());
        HashSet<String> stopWords = new HashSet<>(dataLoader.getStopWords());

        InvertedIndex index = new InvertedIndex();

        for(String key: articles.keySet()){

            index.addDocument(key, articles.get(key).get(3), articles.get(key).get(1), articles.get(key).get(0));

        }

        index.getArticalByIndex("6949974693", articles);
        
    }
}