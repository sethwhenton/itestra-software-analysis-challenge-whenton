package com.itestra.software_analyse_challenge.features;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumberLinesFeatureTest {
    @Test
    void testEmptyCode() {
        String code = "public class Test {}";
        assertEquals(1.0, NumberLinesFeature.countNonCommentLines(code));
    }

    @Test
    void testSingleLine() {
        String code = "public class Test { void method() { } }";
        assertEquals(1.0, NumberLinesFeature.countNonCommentLines(code));
    }

    @Test
    void testMultipleLines() {
        String code = "public class Test {\n" +
                     "    void method() {\n" +
                     "        // Comment\n" +
                     "        int x = 1;\n" +
                     "        \n" +
                     "        if (x > 0) {\n" +
                     "            x++;\n" +
                     "        }\n" +
                     "    }\n" +
                     "}";
        assertEquals(10.0, NumberLinesFeature.countNonCommentLines(code));
    }

    @Test
    void testWithComments() {
        String code = "// Class comment\n" +
                     "public class Test {\n" +
                     "    // Method comment\n" +
                     "    void method() {\n" +
                     "        // Line comment\n" +
                     "        int x = 1;\n" +
                     "    }\n" +
                     "}";
        assertEquals(8.0, NumberLinesFeature.countNonCommentLines(code));
    }
} 