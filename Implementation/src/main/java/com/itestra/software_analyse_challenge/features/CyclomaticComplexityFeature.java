package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.CompilationUnit;

public class CyclomaticComplexityFeature implements Feature {
    @Override
    public double extract(CompilationUnit cu) {
        if (cu == null) return 1.0; 
        CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor();
        cu.accept(visitor, null);
        return 1.0 + visitor.getBranchCount();
    }
} 