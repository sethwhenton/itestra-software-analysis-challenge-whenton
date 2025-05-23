package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.CompilationUnit;

public interface Feature {
    /**
     * Extracts a feature value from a Java compilation unit.
     * @param cu The compilation unit to analyze
     * @return The extracted feature value
     */
    double extract(CompilationUnit cu);
} 