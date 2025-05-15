package com.itestra.software_analyse_challenge;

import java.util.List;
import java.util.Collections;

public class Output {

    private final int lineNumber;
    private boolean hasLineNumberBonus = false;
    private int lineNumberBonus;

    private final List<String> dependencies;

    public Output(final int lineNumber) {
        this.lineNumber = lineNumber;
        this.dependencies = Collections.emptyList();
    }

    /**
     * Full constructor for Task 2: line count + list of project dependencies.
     */
    public Output(final int lineNumber, List<String> dependencies) {
        this.lineNumber   = lineNumber;
        this.dependencies = dependencies != null
                ? dependencies
                : Collections.emptyList();
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
