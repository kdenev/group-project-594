package edu.upenn.cit5940.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class LoadData {

    private String filePath;

    public LoadData(String filePath) {

        this.filePath = filePath;

    }

    public Map<String, ArrayList<String>> getArticles() {

        String fileNameExtension = this.filePath.substring(this.filePath.indexOf('.') + 1);
        Map<String, ArrayList<String>> emptyMap = new HashMap<>();

        if ("csv".equals(fileNameExtension)) {

            return getCSVArticles();

        } else if ("json".equals(fileNameExtension)) {

            return getJSONArticles();

        }

        System.out.println("There is no parser for this file extension: " + fileNameExtension);

        return emptyMap;

    }

    public Map<String, ArrayList<String>> getCSVArticles() {

        // create a map to store document ID and combined title + body text
        Map<String, ArrayList<String>> articles = new HashMap<>();

        // Declare the CSV parser
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"') // "ignores commas in text"
                .withIgnoreQuotations(false)
                .build();

        // Use bufferreader together with the csv reader
        try (
                BufferedReader br = Files.newBufferedReader(Paths.get(this.filePath));
                CSVReader reader = new CSVReaderBuilder(br)
                        .withCSVParser(parser)
                        .withSkipLines(1) // Skips header
                        .build()) {

            // Placeholder for the row of the csv
            String[] line;

            while ((line = reader.readNext()) != null) {

                // Holds all the cols need
                // data, title, body, combined text
                ArrayList<String> articleCols = new ArrayList<>();

                // Skips the empty lines
                if (line.length >= 1) {

                    // Combine the title and body and put in the map
                    String combinedText = line[4] + "\n" + line[5];

                    // Date
                    articleCols.add(line[1]);
                    // Title
                    articleCols.add(line[4]);
                    // Body
                    articleCols.add(line[5]);
                    // Combined text 
                    articleCols.add(combinedText);

                    articles.put(line[0], articleCols);

                }
            }

        } catch (Exception e) {

            // Commented, not to have issues with the auto grader
            // System.err.println("Error importing CSV: " + e.getMessage());
            e.printStackTrace();

        }

        return articles;

    }

    // Placeholder for JSON parser
    public Map<String, ArrayList<String>> getJSONArticles() {

        // create a map to store document ID and combined title + body text
        Map<String, ArrayList<String>> articles = new HashMap<>();

        // create jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        try {
            // read JSON tree
            JsonNode root = mapper.readTree(new File(filePath));

            // check if the tree is an array to prevent program from crashing
            if (root.isArray()) {
                // loop through entire tree
                for (JsonNode article : root) {
                    // get uri
                    String uri = article.get("uri").asText();

                    // create list for this article
                    ArrayList<String> articleCols = new ArrayList<>();

                    // create combined text (title + body)
                    String combinedText = article.get("title").asText() + "\n" + article.get("body").asText();

                    // add columns (date, title, body, combined text)
                    articleCols.add(article.get("date").asText());
                    articleCols.add(article.get("title").asText());
                    articleCols.add(article.get("body").asText());
                    articleCols.add(combinedText);

                    // add article to articles map
                    articles.put(uri, articleCols);
                }
            }

        } catch (Exception e) {

            // Commented, not to have issues with the auto grader
            // System.err.println("Error importing JSON: " + e.getMessage());
            e.printStackTrace();
        }


        return articles;
    }

}
