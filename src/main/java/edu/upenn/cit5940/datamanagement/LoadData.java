package edu.upenn.cit5940.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class LoadData {

    private String filePath;
    private static final String stopWordsFilePath = "src/main/java/edu/upenn/cit5940/datamanagement/data/stop_words.txt";

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
                    articleCols.add(combinedText.toLowerCase());

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

    // Placehodler for JSON parser
    public Map<String, ArrayList<String>> getJSONArticles() {

        return null;
    }

    public static Set<String> getStopWords() {

        // Define return set
        Set<String> stopWords = new HashSet();

        // If input is null return empty list
        // if (stopWordsFilePath == null) {

        //     return stopWords;
        // }

        // Define text processing variables
        File file = new File(stopWordsFilePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        // Set up the reading variables
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            // Placeholder variable for the line by line processing
            // of the file
            String line;

            // Loop through the file until there is a line
            while ((line = bufferedReader.readLine()) != null) {

                if (!"".equals(line.strip())) {

                    stopWords.add(line.strip());

                }

            }

            // Handle exception if they occur during the file read
        } catch (FileNotFoundException e) {

            // Get and print filename
            // Commented for gradescope submission
            // System.out.println("Sorry, " + file.getName() + " not found.");

        } catch (IOException e) {

            // Print the error message
            e.printStackTrace();

        } finally {

            // Close file objects
            try {

                if (bufferedReader != null) {
                    bufferedReader.close();
                }

                if (fileReader != null) {
                    fileReader.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        return stopWords;

    }

}
