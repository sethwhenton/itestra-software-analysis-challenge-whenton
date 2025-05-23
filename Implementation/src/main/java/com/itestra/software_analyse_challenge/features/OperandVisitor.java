// This file was written with the help of an LLM. See LLM/prompts.txt for details.

package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.google.common.collect.Multiset;
import com.google.common.collect.HashMultiset;

public class OperandVisitor extends VoidVisitorAdapter<Void> {
    private final Multiset<String> operands = HashMultiset.create();

    @Override
    public void visit(NameExpr n, Void arg) {
        operands.add(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(StringLiteralExpr n, Void arg) {
        operands.add(n.getValue());
        super.visit(n, arg);
    }

    @Override
    public void visit(IntegerLiteralExpr n, Void arg) {
        operands.add(n.getValue().toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(DoubleLiteralExpr n, Void arg) {
        operands.add(n.getValue().toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(BooleanLiteralExpr n, Void arg) {
        operands.add(String.valueOf(n.getValue()));
        super.visit(n, arg);
    }

    @Override
    public void visit(Parameter n, Void arg) {
        operands.add(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        operands.add(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        operands.add(n.getNameAsString());
        super.visit(n, arg);
    }

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        operands.add(n.getNameAsString());
        if (n.getInitializer().isPresent()) {
            operands.add(n.getInitializer().get().toString());
        }
        super.visit(n, arg);
    }

    public Multiset<String> getOperands() {
        return operands;
    }

    public int getTotalOperands() {
        return operands.size();
    }

    public int getUniqueOperands() {
        return operands.elementSet().size();
    }
} 