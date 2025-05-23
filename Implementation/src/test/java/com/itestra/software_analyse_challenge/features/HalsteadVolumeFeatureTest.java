package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HalsteadVolumeFeatureTest {
    private final HalsteadVolumeFeature feature = new HalsteadVolumeFeature();
    private final JavaParser parser = new JavaParser();

    @Test
    void testEmptyCode() {
        String code = "public class Test {}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(0.0, feature.extract(cu));
    }

    @Test
    void testSimpleAssignment() {
        String code = "public class Test { void method() { int x = 1; } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        double value = feature.extract(cu);
        System.out.println("Halstead Volume for simple assignment: " + value);
        assertTrue(value > 0);
    }

    @Test
    void testBinaryOperation() {
        String code = "public class Test { void method() { int x = 1 + 2; } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertTrue(feature.extract(cu) > 0);
    }

    @Test
    void testComplexExpression() {
        String code = "public class Test { " +
                     "    void method() { " +
                     "        int x = 1 + 2 * 3; " +
                     "        String s = \"test\"; " +
                     "        boolean b = true && false; " +
                     "    } " +
                     "}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertTrue(feature.extract(cu) > 0);
    }

    @Test
    void testMethodCall() {
        String code = "public class Test { " +
                     "    void method() { " +
                     "        String s = \"test\"; " +
                     "        int length = s.length(); " +
                     "    } " +
                     "}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertTrue(feature.extract(cu) > 0);
    }
} 