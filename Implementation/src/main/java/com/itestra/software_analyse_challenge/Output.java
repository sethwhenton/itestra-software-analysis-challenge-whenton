package com.itestra.software_analyse_challenge;

import java.util.List;

public class Output {

    private final int lineNumber;
    private boolean hasLineNumberBonus = false;
    private int lineNumberBonus;

    private final List<String> dependencies;

    public Output(final int lineNumber, final List<String> dependencies) {
        this.lineNumber = lineNumber;
        this.dependencies = dependencies;
    }

    @SuppressWarnings("unused")
    public Output lineNumberBonus(final int lineNumberBonus) {
        this.lineNumberBonus = lineNumberBonus;
        this.hasLineNumberBonus = true;
        return this;
    }

    public String getLineNumber() {
        return String.valueOf(lineNumber);
    }

    public String getLineNumberBonus() {
        return this.hasLineNumberBonus ? String.valueOf(lineNumberBonus) : "N/A";
    }

    public String getDependencies() {
        return String.valueOf(dependencies);
    }
}
