package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.CompilationUnit;
import java.nio.file.Files;

public class NumberLinesFeature implements Feature {
    @Override
    public double extract(CompilationUnit cu) {
        if (cu == null) return 0.0;
        // Try to get the original source code if available
        String source = cu.getStorage().map(storage -> {
            try {
                return Files.readString(storage.getPath());
            } catch (Exception e) {
                return cu.toString(); // fallback
            }
        }).orElse(cu.toString());
        return countNonCommentLines(source);
    }


    public static double countNonCommentLines(String code) {
        if (code == null || code.isEmpty()) {
            return 0.0;
        }
        // Split by any Unicode linebreak sequence 
        String[] lines = code.split("\\R");
        return (double) lines.length;
    }
} 