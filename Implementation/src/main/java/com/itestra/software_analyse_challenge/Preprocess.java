package com.itestra.software_analyse_challenge;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.itestra.software_analyse_challenge.features.*;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Preprocess {
    private static final double TRUTH_THRESHOLD = 3.0; 

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("s", "source", true, "Path to directory hosting .jsnp files");
        options.addOption("g", "ground-truth", true, "Path to ground truth CSV file");
        options.addOption("t", "target", true, "Path to target CSV file");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            
            if (!cmd.hasOption("s") || !cmd.hasOption("g") || !cmd.hasOption("t")) {
                formatter.printHelp("Preprocess", options);
                System.exit(1);
            }

            String snippetsDir = cmd.getOptionValue("s");
            String ratingsFile = cmd.getOptionValue("g");
            String outputFile = cmd.getOptionValue("t");

            //  ratings
            Map<String, Double> ratings = readRatings(ratingsFile);
            
            //  snippets and generate CSV
            List<String> csvLines = collectCSVBody(snippetsDir, ratings);
            
            //  output
            writeCSV(outputFile, csvLines);
            
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            formatter.printHelp("Preprocess", options);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            System.exit(1);
        }
    }

    private static Map<String, Double> readRatings(String ratingsFile) throws IOException {
        Map<String, Double> ratings = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(ratingsFile));
        
       
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length >= 2) {
                String file = parts[0];
                double rating = Double.parseDouble(parts[1]);
                ratings.put(file, rating);
            }
        }
        
        return ratings;
    }

    public static List<String> collectCSVBody(String snippetsDir, Map<String, Double> ratings) throws IOException {
        List<String> csvLines = new ArrayList<>();
        JavaParser parser = new JavaParser();
        
        //  feature extractors
        NumberLinesFeature numberLines = new NumberLinesFeature();
        TokenEntropyFeature tokenEntropy = new TokenEntropyFeature();
        HalsteadVolumeFeature halsteadVolume = new HalsteadVolumeFeature();
        CyclomaticComplexityFeature cyclomaticComplexity = new CyclomaticComplexityFeature();

        // process each .jsnp file
        Files.walk(Paths.get(snippetsDir))
            .filter(path -> path.toString().endsWith(".jsnp"))
            .forEach(path -> {
                try {
                    String fileName = path.getFileName().toString();
                    String content = Files.readString(path);
                    
                    // parse the Java code
                    CompilationUnit cu = parser.parse(content).getResult().orElse(null);
                    if (cu == null) return;

                    //  features
                    double lines = numberLines.extract(cu);
                    double entropy = tokenEntropy.extract(cu);
                    double volume = halsteadVolume.extract(cu);
                    double complexity = cyclomaticComplexity.extract(cu);

                    // Get truth value
                    double rating = ratings.getOrDefault(fileName, 0.0);
                    String truth = rating >= TRUTH_THRESHOLD ? "Y" : "N";

                    // format CSV line
                    String csvLine = String.format("%s,%.2f,%.2f,%.2f,%.2f,%s",
                        fileName, lines, entropy, volume, complexity, truth);
                    
                    csvLines.add(csvLine);
                } catch (IOException e) {
                    System.err.println("Error processing " + path + ": " + e.getMessage());
                }
            });

        // sort by filename
        return csvLines.stream()
            .sorted()
            .collect(Collectors.toList());
    }

    private static void writeCSV(String outputFile, List<String> lines) throws IOException {
        // write header
        String header = "File,NumberLines,TokenEntropy,HalsteadVolume,CyclomaticComplexity,Truth";
        Files.write(Paths.get(outputFile), 
            (header + "\n" + String.join("\n", lines)).getBytes());
    }
} 