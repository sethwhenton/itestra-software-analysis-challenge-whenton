package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.TokenRange;
import com.google.common.collect.Multiset;
import com.google.common.collect.HashMultiset;

import java.util.Map;
import java.util.stream.Collectors;

public class TokenEntropyFeature implements Feature {
    @Override
    public double extract(CompilationUnit cu) {
        if (cu == null) return 0.0;

        // token range and count frequencies
        Multiset<String> tokenFrequencies = HashMultiset.create();
        cu.getTokenRange().ifPresent(range -> {
            range.forEach(token -> tokenFrequencies.add(token.getText()));
        });

        if (tokenFrequencies.isEmpty()) return 0.0;

        // total tokens
        int totalTokens = tokenFrequencies.size();

        //  entropy
        return -tokenFrequencies.entrySet().stream()
            .mapToDouble(entry -> {
                double probability = (double) entry.getCount() / totalTokens;
                return probability * (Math.log(probability) / Math.log(2));
            })
            .sum();
    }
} 