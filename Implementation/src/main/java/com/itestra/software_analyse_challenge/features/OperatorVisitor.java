// This file was written with the help of an LLM. See LLM/prompts.txt for details.

package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.google.common.collect.Multiset;
import com.google.common.collect.HashMultiset;

public class OperatorVisitor extends VoidVisitorAdapter<Void> {
    private final Multiset<String> operators = HashMultiset.create();

    @Override
    public void visit(AssignExpr n, Void arg) {
        operators.add(n.getOperator().asString());
        super.visit(n, arg);
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        operators.add(n.getOperator().asString());
        super.visit(n, arg);
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        operators.add(n.getOperator().asString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        operators.add("?");
        operators.add(":");
        super.visit(n, arg);
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        operators.add("instanceof");
        super.visit(n, arg);
    }

    public Multiset<String> getOperators() {
        return operators;
    }

    public int getTotalOperators() {
        return operators.size();
    }

    public int getUniqueOperators() {
        return operators.elementSet().size();
    }
} 