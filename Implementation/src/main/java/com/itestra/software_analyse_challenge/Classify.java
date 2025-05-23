package com.itestra.software_analyse_challenge;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Random;

public class Classify {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("d", "data", true, "Path to training dataset CSV file");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            
            if (!cmd.hasOption("d")) {
                formatter.printHelp("Classify", options);
                System.exit(1);
            }

            // Load dataset
            Instances dataset = loadDataset(cmd.getOptionValue("d"));
            
            // Train and evaluate
            trainAndEvaluate(dataset);
            
        } catch (ParseException e) {
            System.err.println("Error parsing command line arguments: " + e.getMessage());
            formatter.printHelp("Classify", options);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error during classification: " + e.getMessage());
            System.exit(1);
        }
    }

    public static Instances loadDataset(String csvFile) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvFile));
        Instances data = loader.getDataSet();
        
        
        data.setClassIndex(data.numAttributes() - 1);
        
        return data;
    }

    public static void trainAndEvaluate(Instances dataset) throws Exception {
       
        Logistic classifier = new Logistic();
        

        Evaluation eval = new Evaluation(dataset);
        eval.crossValidateModel(classifier, dataset, 10, new Random(1));
        
        // show results
        System.out.println("\n=== 10-Fold Cross-validation Results ===");
        System.out.println(eval.toSummaryString());
        System.out.println("\n=== Detailed Accuracy By Class ===");
        System.out.println(eval.toClassDetailsString());
        System.out.println("\n=== Confusion Matrix ===");
        System.out.println(eval.toMatrixString());
    }
} 