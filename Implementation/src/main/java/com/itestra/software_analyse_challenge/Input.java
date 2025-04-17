package com.itestra.software_analyse_challenge;

import org.apache.commons.cli.CommandLine;

import java.io.File;

public class Input {

    private final File inputDirectory;

    public Input(CommandLine commandLine) {
        String optionValue = commandLine.getOptionValue(SourceCodeAnalyser.INPUT_DIR, SourceCodeAnalyser.DEFAULT_INPUT_DIR);
        this.inputDirectory = new File(optionValue);
        if (!this.inputDirectory.isDirectory()) {
            throw new IllegalArgumentException(optionValue + " is not a directory");
        }
    }

    @SuppressWarnings("unused")
    public File getInputDirectory() {
        return inputDirectory;
    }
}
