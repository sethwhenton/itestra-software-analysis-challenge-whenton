package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.CompilationUnit;

public class HalsteadVolumeFeature implements Feature {
    @Override
    public double extract(CompilationUnit cu) {
        if (cu == null) return 0.0;

        // Count operators x operands
        OperatorVisitor operatorVisitor = new OperatorVisitor();
        OperandVisitor operandVisitor = new OperandVisitor();
        
        cu.accept(operatorVisitor, null);
        cu.accept(operandVisitor, null);

        //  total and unique counts
        int N1 = operatorVisitor.getTotalOperators();
        int N2 = operandVisitor.getTotalOperands();
        int n1 = operatorVisitor.getUniqueOperators();
        int n2 = operandVisitor.getUniqueOperands();

        //  program length and vocabulary
        int N = N1 + N2;  // program length
        int n = n1 + n2;  // program vocabulary

        // return 0.0 if there are no operators or operands
        if (N == 0 || n == 0) return 0.0;

        //  Halstead Volume
        return N * (Math.log(n) / Math.log(2));
    }
} 