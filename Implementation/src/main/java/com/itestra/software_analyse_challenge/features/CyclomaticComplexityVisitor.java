// This file was written with the help of an LLM. See LLM/prompts.txt for details.

package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

public class CyclomaticComplexityVisitor extends VoidVisitorAdapter<Void> {
    private int branchCount = 0;

    @Override
    public void visit(IfStmt n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(SwitchEntry n, Void arg) {
        // Only count case labels, not default
        if (n.getLabels().size() > 0) {
            branchCount++;
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        branchCount++;
        super.visit(n, arg);
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        if (n.getOperator() == BinaryExpr.Operator.AND || n.getOperator() == BinaryExpr.Operator.OR) {
            branchCount++;
        }
        super.visit(n, arg);
    }

    public int getBranchCount() {
        return branchCount;
    }
} 