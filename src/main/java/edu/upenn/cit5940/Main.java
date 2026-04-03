package edu.upenn.cit5940;

import java.util.HashMap;

import edu.upenn.cit5940.datamanagement.LoadData;

public class Main {
    public static void main(String[] args) {

        LoadData dataLoader = new LoadData("src\\main\\java\\edu\\upenn\\cit5940\\datamanagement\\data\\articles_small.csv");
        HashMap<Integer, String> articles = new HashMap<>(dataLoader.getArticles());

        System.out.println(articles.get(1));
    }
}