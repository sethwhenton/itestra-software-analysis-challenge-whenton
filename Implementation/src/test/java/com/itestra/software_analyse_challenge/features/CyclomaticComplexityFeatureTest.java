package com.itestra.software_analyse_challenge.features;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CyclomaticComplexityFeatureTest {
    private final CyclomaticComplexityFeature feature = new CyclomaticComplexityFeature();
    private final JavaParser parser = new JavaParser();

    @Test
    void testEmptyCode() {
        String code = "public class Test {}";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(1.0, feature.extract(cu));
    }

    @Test
    void testSingleIf() {
        String code = "public class Test { void m() { if (true) {} } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(2.0, feature.extract(cu));
    }

    @Test
    void testIfElseIfElse() {
        String code = "public class Test { void m() { if (a) {} else if (b) {} else {} } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(3.0, feature.extract(cu));
    }

    @Test
    void testLoops() {
        String code = "public class Test { void m() { for(int i=0;i<10;i++){} while(true){} do{}while(false); } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(4.0, feature.extract(cu));
    }

    @Test
    void testSwitchCase() {
        String code = "public class Test { void m() { switch(x) { case 1: break; case 2: break; default: break; } } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(3.0, feature.extract(cu));
    }

    @Test
    void testCatchAndConditional() {
        String code = "public class Test { void m() { try { int x = a > 0 ? 1 : 2; } catch(Exception e) {} } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(3.0, feature.extract(cu));
    }

    @Test
    void testLogicalOperators() {
        String code = "public class Test { void m() { if (a && b || c) {} } }";
        CompilationUnit cu = parser.parse(code).getResult().orElse(null);
        assertEquals(4.0, feature.extract(cu));
    }
} 