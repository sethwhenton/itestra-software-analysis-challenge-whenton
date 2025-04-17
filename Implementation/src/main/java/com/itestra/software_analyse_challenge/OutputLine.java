package com.itestra.software_analyse_challenge;

public class OutputLine {
    private final String fileName;
    private final String lineNumber;
    private final String lineNumberWithoutGetterSetter;
    private final String dependencies;

    public OutputLine(String fileName, String lineNumber, String lineNumberWithoutGetterSetter, String dependencies) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.lineNumberWithoutGetterSetter = lineNumberWithoutGetterSetter;
        this.dependencies = dependencies;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public String getLineNumberWithoutGetterSetter() {
        return lineNumberWithoutGetterSetter;
    }

    public String getDependencies() {
        return dependencies;
    }
}
