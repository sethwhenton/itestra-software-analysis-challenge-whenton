package com.itestra.software_analyse_challenge;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.*;
import java.util.stream.Collectors;


public class SourceCodeAnalyser {
    /**
     * Count lines excluding:
     * - blank lines
     * - single-line // comments
     * - block comments (/* … *​/)
     * - one-line getters like: public Type getFoo() { return foo; }
     */
    private static int countCleanSourceLines(Path javaFile) throws IOException {
        int count = 0;
        boolean inBlock = false;
        for (String raw : Files.readAllLines(javaFile)) {
            String line = raw.trim();
            // block comment start?
            if (!inBlock && line.startsWith("/*")) {
                inBlock = true;
            }
            // block comment end?
            if (inBlock) {
                if (line.endsWith("*/")) {
                    inBlock = false;
                }
                continue;
            }
            // skip blank or // comments
            if (line.isEmpty() || line.startsWith("//")) {
                continue;
            }
            // skip one-line getter: public Type getX() { return x; }
            if (line.matches("public\\s+\\w+\\s+get[A-Z]\\w*\\s*\\(\\s*\\)\\s*\\{\\s*return\\s+\\w+;\\s*\\}")) {
                continue;
            }
            count++;
        }
        return count;
    }

    /**
     * Count non-empty, non-// comment lines in a .java file.
     */
    private static int countSourceLines(Path javaFile) throws IOException {
        int count = 0;
        for (String line : Files.readAllLines(javaFile)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("//")) {
                continue;
            }
            count++;
        }
        return count;
    }

    /**
     * Your implementation
     *
     *
     * @param input {@link Input} object.
     * @return mapping from filename -> {@link Output} object.
     */
    public static Map<String, Output> analyse(Input input) {
        Map<String, Output> result = new HashMap<>();

        // 1) Convert the input directory to a Path root
        Path root = input.getInputDirectory().toPath();

        try {
            // 2) Discover project roots under the input directory (e.g. "cronutils", "fig", "spark")
            List<String> projectRoots = Files.list(root)
                    .filter(Files::isDirectory)
                    .map(dir -> dir.getFileName().toString())
                    .collect(Collectors.toList());

            // 3) Walk .java files and for each, count lines and detect imports
            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            // a) Count source lines
                            int lines = countSourceLines(p);

                            // b) Parse import statements and map each import to its project root
                            List<String> deps = Files.readAllLines(p).stream()
                                    .map(String::trim)
                                    .filter(line -> line.startsWith("import "))
                                    .map(line -> line.substring(7, line.length() - 1))  // drop "import" and trailing ";"
                                    .map(pkg -> projectRoots.stream()
                                            .filter(pkg::startsWith)
                                            .findFirst()
                                            .orElse(null))
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .collect(Collectors.toList());

                            // c) Compute the path relative to the input root and record output
                            String relPath = root.relativize(p).toString();
                            int clean = countCleanSourceLines(p);
                            result.put(relPath,
                                    new Output(lines, deps)
                                            .lineNumberBonus(clean)
                            );

                        } catch (IOException e) {
                            throw new UncheckedIOException("Failed reading " + p, e);
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException("Error walking directory " + root, e);
        }

        return result;
    }


    /**
     * INPUT - OUTPUT
     *
     * No changes below here are necessary!
     */

    public static final Option INPUT_DIR = Option.builder("i")
            .longOpt("input-dir")
            .hasArg(true)
            .desc("input directory path")
            .required(false)
            .build();

    public static final String DEFAULT_INPUT_DIR = String.join(File.separator , Arrays.asList("..", "CodeExamples", "src", "main", "java"));

    private static Input parseInput(String[] args) {
        Options options = new Options();
        Collections.singletonList(INPUT_DIR).forEach(options::addOption);
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine commandLine = parser.parse(options, args);
            return new Input(commandLine);
        } catch (ParseException e) {
            formatter.printHelp("help", options);
            throw new IllegalStateException("Could not parse Command Line", e);
        }
    }

    private static void printOutput(Map<String, Output> outputMap) {
        System.out.println("Result: ");
        List<OutputLine> outputLines =
                outputMap.entrySet().stream()
                        .map(e -> new OutputLine(e.getKey(), e.getValue().getLineNumber(), e.getValue().getLineNumberBonus(), e.getValue().getDependencies()))
                        .sorted(Comparator.comparing(OutputLine::getFileName))
                        .collect(Collectors.toList());
        outputLines.add(0, new OutputLine("File", "Source Lines", "Source Lines without Getters and Block Comments", "Dependencies"));
        int maxDirectoryName = outputLines.stream().map(OutputLine::getFileName).mapToInt(String::length).max().orElse(100);
        int maxLineNumber = outputLines.stream().map(OutputLine::getLineNumber).mapToInt(String::length).max().orElse(100);
        int maxLineNumberWithoutGetterAndSetter = outputLines.stream().map(OutputLine::getLineNumberWithoutGetterSetter).mapToInt(String::length).max().orElse(100);
        int maxDependencies = outputLines.stream().map(OutputLine::getDependencies).mapToInt(String::length).max().orElse(100);
        String lineFormat = "| %"+ maxDirectoryName+"s | %"+maxLineNumber+"s | %"+maxLineNumberWithoutGetterAndSetter+"s | %"+ maxDependencies+"s |%n";
        outputLines.forEach(line -> System.out.printf(lineFormat, line.getFileName(), line.getLineNumber(), line.getLineNumberWithoutGetterSetter(), line.getDependencies()));
    }

    public static void main(String[] args) {
        Input input = parseInput(args);
        Map<String, Output> outputMap = analyse(input);
        printOutput(outputMap);
    }
}
