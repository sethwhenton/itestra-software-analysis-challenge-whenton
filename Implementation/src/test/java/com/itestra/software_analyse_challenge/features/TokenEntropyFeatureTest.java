package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenEntropyFeatureTest {
    private final TokenEntropyFeature feature = new TokenEntropyFeature();
    private final JavaParser parser = new JavaParser();

    @Test
    void testEmptyCode() {
        String code = "public class Test {}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertTrue(feature.extract(cu) > 0);
    }

    @Test
    void testSingleToken() {
        String code = "public class Test { int x; }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertTrue(feature.extract(cu) > 0);
    }

    @Test
    void testRepeatedTokens() {
        String code = "public class Test { " +
                     "    int x = 1; " +
                     "    int y = 2; " +
                     "    int z = 3; " +
                     "}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        double entropy = feature.extract(cu);
        assertTrue(entropy > 0);
    }

    @Test
    void testMixedTokens() {
        String code = "public class Test { " +
                     "    void method() { " +
                     "        int x = 1 + 2 * 3; " +
                     "        String s = \"test\"; " +
                     "        boolean b = true && false; " +
                     "    } " +
                     "}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        double entropy = feature.extract(cu);
        assertTrue(entropy > 0);
    }

    @Test
    void testEntropyCalculation() {
        // Test with a simple case where we can predict the entropy
        String code = "public class Test { int x = 1; int y = 1; }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        double entropy = feature.extract(cu);
        assertTrue(entropy > 0);
    }
} 