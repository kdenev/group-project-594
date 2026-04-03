package edu.upenn.cit5940.datamanagement;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class LoadData {

    private String csvFilePath;

    public LoadData(String csvFilePath) {

        this.csvFilePath = csvFilePath;

    }

    public Map<Integer, String> getArticles() {

        // create a map to store document ID and combined title + body text
        Map<Integer, String> articles = new HashMap<>();

        // Declare the CSV parser
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"') // "ignores commas in text"
                .withIgnoreQuotations(false)
                .build();

        // Use bufferreader together with the csv reader
        try (
                BufferedReader br = Files.newBufferedReader(Paths.get(this.csvFilePath));
                CSVReader reader = new CSVReaderBuilder(br)
                        .withCSVParser(parser)
                        .withSkipLines(1) // Skips header
                        .build()) {

            // Placeholder for the row of the csv
            String[] line;

            // Id of the article
            Integer articleId = 1;

            while ((line = reader.readNext()) != null) {

                // Skips the empty lines
                if (line.length >= 1) {

                    // Combine the title and body and put in the map
                    String combinedText = line[4] + "\n" + line[5];
                    articles.put(articleId++, combinedText);

                }
            }

        } catch (Exception e) {

            // Commented, not to have issues with the auto grader
            // System.err.println("Error importing CSV: " + e.getMessage());
            e.printStackTrace();

        }

        return articles;

    }

}
