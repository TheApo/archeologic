package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.GameScreen;
import com.apogames.game.question.QuestionTextRenderer.TextSegment;

import java.util.List;

/**
 * Demo class to test the new flexible question rendering system
 */
public class QuestionRenderingDemo {

    /**
     * Test the text parsing functionality
     */
    public static void testTextParsing() {
        System.out.println("=== Testing Question Text Parsing ===");

        // Test German text
        String germanKey = "question_enum_tile_next_tile";
        List<TextSegment> germanSegments = QuestionTextRenderer.parseQuestionText(germanKey);

        System.out.println("German segments for " + germanKey + ":");
        for (int i = 0; i < germanSegments.size(); i++) {
            TextSegment segment = germanSegments.get(i);
            System.out.println("  " + i + ": " + segment.getType() + " - '" + segment.getText() + "'");
        }

        // Test another question type
        String borderKey = "question_enum_horizontal_border";
        List<TextSegment> borderSegments = QuestionTextRenderer.parseQuestionText(borderKey);

        System.out.println("\nBorder segments for " + borderKey + ":");
        for (int i = 0; i < borderSegments.size(); i++) {
            TextSegment segment = borderSegments.get(i);
            System.out.println("  " + i + ": " + segment.getType() + " - '" + segment.getText() + "'");
        }
    }

    /**
     * Test width calculation functionality
     */
    public static void testWidthCalculation() {
        System.out.println("\n=== Testing Width Calculation ===");

        List<TextSegment> segments = QuestionTextRenderer.parseQuestionText("question_enum_tile_next_tile");

        // Measure segments
        QuestionTextRenderer.measureSegments(segments, AssetLoader.font20);

        float totalWidth = QuestionTextRenderer.calculateTotalWidth(segments, 5.0f);

        System.out.println("Total width needed: " + totalWidth + " pixels");
        System.out.println("Individual segment widths:");
        for (int i = 0; i < segments.size(); i++) {
            TextSegment segment = segments.get(i);
            System.out.println("  " + i + ": " + segment.getType() + " - " + segment.getWidth() + "px");
        }
    }

    /**
     * Render a sample question for visual testing
     */
    public static void renderSampleQuestion(GameScreen screen, float x, float y) {
        List<TextSegment> segments = QuestionTextRenderer.parseQuestionText("question_enum_tile_next_tile");

        // Create a mock TileNextTile question for demonstration
        MockTileNextTile mockQuestion = new MockTileNextTile();

        QuestionTextRenderer.renderQuestionSegments(
            screen, segments, x, y,
            AssetLoader.font20, Constants.COLOR_BLACK,
            8.0f, mockQuestion
        );
    }

    /**
     * Mock TileNextTile for testing purposes
     */
    private static class MockTileNextTile extends Question {
        private byte[][] tile1 = {{1, 1}, {1, 0}};
        private byte[][] tile2 = {{1}, {1}};

        @Override
        protected Object getTileVisualData(int segmentIndex) {
            switch (segmentIndex) {
                case 0: return tile1;
                case 1: return tile2;
                default: return null;
            }
        }

        @Override
        protected Object getDropdownData(int segmentIndex) {
            return null;
        }

        @Override
        public void draw(GameScreen screen, int x, int y, int size) {}

        @Override
        public int getCosts() { return 1; }

        @Override
        public String getText() { return "Mock Question"; }

        @Override
        public String getAnswer() { return "true"; }

        @Override
        public java.util.ArrayList<Integer> filter(java.util.ArrayList<byte[][]> a,
                                                  java.util.ArrayList<byte[][]> b,
                                                  java.util.ArrayList<byte[][]> c) {
            return new java.util.ArrayList<>();
        }
    }
}