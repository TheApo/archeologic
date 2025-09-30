package com.apogames.game.question;

import com.apogames.Constants;
import com.apogames.asset.AssetLoader;
import com.apogames.backend.DrawString;
import com.apogames.backend.GameScreen;
import com.apogames.common.Localization;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Flexible text renderer for questions that supports dynamic positioning
 * of UI elements (dropdowns, buttons) based on text length rather than
 * hardcoded positions.
 */
public class QuestionTextRenderer {

    public enum SegmentType {
        TEXT,           // Regular text segment
        DROPDOWN,       // Dropdown/selection element
        TILE_VISUAL,    // Visual tile representation
        BUTTON          // Interactive button
    }

    /**
     * Represents a segment of a question that can be text or a UI element
     */
    public static class TextSegment {
        private final SegmentType type;
        private final String text;
        private final Object data;
        private float width;
        private float height;

        public TextSegment(SegmentType type, String text) {
            this(type, text, null);
        }

        public TextSegment(SegmentType type, String text, Object data) {
            this.type = type;
            this.text = text != null ? text : "";
            this.data = data;
            this.width = 0;
            this.height = 0;
        }

        public SegmentType getType() { return type; }
        public String getText() { return text; }
        public Object getData() { return data; }
        public float getWidth() { return width; }
        public float getHeight() { return height; }

        public void setWidth(float width) { this.width = width; }
        public void setHeight(float height) { this.height = height; }
    }

    /**
     * Parse a question text string with semicolon separators into text segments.
     * Format: "text1;DROPDOWN;text2;TILE_VISUAL;text3"
     *
     * @param questionKey The localization key for the question
     * @return List of text segments
     */
    public static List<TextSegment> parseQuestionText(String questionKey) {
        List<TextSegment> segments = new ArrayList<>();

        String questionText = Localization.getInstance().getCommon().get(questionKey);
        if (questionText == null || questionText.isEmpty()) {
            // Fallback to empty text segment
            segments.add(new TextSegment(SegmentType.TEXT, questionKey));
            return segments;
        }

        return parseQuestionTextFromString(questionText);
    }

    /**
     * Parse a question text string directly
     *
     * @param questionText The text to parse
     * @return List of text segments
     */
    public static List<TextSegment> parseQuestionTextFromString(String questionText) {
        List<TextSegment> segments = new ArrayList<>();

        // Check if this uses the new semicolon format
        if (!questionText.contains(";")) {
            // Old format - just return as single text segment
            segments.add(new TextSegment(SegmentType.TEXT, questionText));
            return segments;
        }

        String[] parts = questionText.split(";");

        for (String part : parts) {
            part = part.trim();

            if (part.isEmpty()) {
                continue;
            }

            // Check if this part represents a UI element type
            SegmentType segmentType = parseSegmentType(part);

            if (segmentType == SegmentType.TEXT) {
                segments.add(new TextSegment(SegmentType.TEXT, part));
            } else {
                segments.add(new TextSegment(segmentType, "", null));
            }
        }

        // If no segments were created, add a fallback text segment
        if (segments.isEmpty()) {
            segments.add(new TextSegment(SegmentType.TEXT, questionText));
        }

        return segments;
    }

    /**
     * Check if a text string uses the new flexible format
     */
    public static boolean isFlexibleFormat(String text) {
        return text != null && (text.contains(";TILE_VISUAL") || text.contains(";DROPDOWN") || text.contains(";BUTTON"));
    }

    /**
     * Safe method to render any question text, with maximum backward compatibility
     */
    public static float renderSafeQuestionText(GameScreen screen, String text,
                                             float startX, float startY, boolean error) {
        try {
            return renderEnhancedTextAndTileQuestion(screen, text, startX, startY, error);
        } catch (Exception e) {
            // If anything goes wrong, fall back to simple text rendering
            System.err.println("Warning: Flexible rendering failed, using fallback: " + e.getMessage());
            float[] textColor = error ? Constants.COLOR_RED : Constants.COLOR_BLACK;
            screen.drawString(text, startX, startY, textColor, AssetLoader.font15, DrawString.BEGIN, true, false);

            // Calculate approximate width
            Constants.glyphLayout.setText(AssetLoader.font15, text);
            return startX + Constants.glyphLayout.width;
        }
    }

    /**
     * Calculate positions for UI elements (dropdowns, buttons) in a question text
     * This version uses real button dimensions for accurate spacing
     */
    public static java.util.List<UIElementPosition> calculateUIElementPositions(String questionText,
                                                                              float startX, float startY,
                                                                              GameScreen screen) {
        java.util.List<UIElementPosition> positions = new java.util.ArrayList<>();

        if (!isFlexibleFormat(questionText)) {
            return positions; // No UI elements in old format
        }

        List<TextSegment> segments = parseQuestionTextFromString(questionText);

        // Determine if this question has a large dropdown based on i18n key patterns
        // The large dropdown appears in horizontal/vertical border questions
        boolean hasLargeDropdown = questionText.contains(";TILE_VISUAL;") && questionText.contains(";DROPDOWN");

        // Use real button dimensions instead of default sizes
        measureSegmentsWithRealButtonSizes(segments, AssetLoader.font15, screen, hasLargeDropdown);

        float currentX = startX;
        int tileVisualIndex = 0;
        int dropdownIndex = 0;
        float padding = 8.0f; // Increased padding around buttons

        for (TextSegment segment : segments) {
            switch (segment.getType()) {
                case DROPDOWN:
                    positions.add(new UIElementPosition(SegmentType.DROPDOWN, dropdownIndex,
                                                      currentX, startY, segment.getWidth(), segment.getHeight()));
                    dropdownIndex++;
                    break;

                case TILE_VISUAL:
                    positions.add(new UIElementPosition(SegmentType.TILE_VISUAL, tileVisualIndex,
                                                      currentX, startY, segment.getWidth(), segment.getHeight()));
                    tileVisualIndex++;
                    break;

                case BUTTON:
                    positions.add(new UIElementPosition(SegmentType.BUTTON, 0,
                                                      currentX, startY, segment.getWidth(), segment.getHeight()));
                    break;
            }

            currentX += segment.getWidth() + padding;
        }

        return positions;
    }

    /**
     * Backward compatibility method - uses default button sizes
     */
    public static java.util.List<UIElementPosition> calculateUIElementPositions(String questionText,
                                                                              float startX, float startY) {
        return calculateUIElementPositions(questionText, startX, startY, null);
    }

    /**
     * Measure segments using real button dimensions where possible
     */
    private static void measureSegmentsWithRealButtonSizes(List<TextSegment> segments, BitmapFont font, GameScreen screen) {
        measureSegmentsWithRealButtonSizes(segments, font, screen, false);
    }

    /**
     * Measure segments using real button dimensions where possible
     */
    private static void measureSegmentsWithRealButtonSizes(List<TextSegment> segments, BitmapFont font, GameScreen screen, boolean hasLargeDropdown) {
        GlyphLayout layout = new GlyphLayout();

        int dropdownIndex = 0;
        int tileVisualIndex = 0;

        for (TextSegment segment : segments) {
            switch (segment.getType()) {
                case TEXT:
                    if (!segment.getText().isEmpty()) {
                        layout.setText(font, segment.getText());
                        segment.setWidth(layout.width);
                        segment.setHeight(layout.height);
                    }
                    break;

                case DROPDOWN:
                    // Check if this is the large dropdown (STRING_SIDE)
                    if (hasLargeDropdown && dropdownIndex == 1) {
                        // This is the large string dropdown (DROPDOWN_STRING_SIDE) - 330x40
                        segment.setWidth(330);
                        segment.setHeight(40);
                    } else {
                        // Small dropdown - 40x40
                        segment.setWidth(40);
                        segment.setHeight(40);
                    }
                    dropdownIndex++;
                    break;

                case TILE_VISUAL:
                    // Standard tile visual button size - 40x40
                    segment.setWidth(40);
                    segment.setHeight(40);
                    tileVisualIndex++;
                    break;

                case BUTTON:
                    // Default button size - 40x40
                    segment.setWidth(40);
                    segment.setHeight(40);
                    break;
            }
        }
    }

    /**
     * Data class to hold UI element position information
     */
    public static class UIElementPosition {
        private final SegmentType type;
        private final int index;
        private final float x, y, width, height;

        public UIElementPosition(SegmentType type, int index, float x, float y, float width, float height) {
            this.type = type;
            this.index = index;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public SegmentType getType() { return type; }
        public int getIndex() { return index; }
        public float getX() { return x; }
        public float getY() { return y; }
        public float getWidth() { return width; }
        public float getHeight() { return height; }
    }

    /**
     * Parse segment type from string
     */
    private static SegmentType parseSegmentType(String part) {
        switch (part.toUpperCase()) {
            case "DROPDOWN":
                return SegmentType.DROPDOWN;
            case "TILE_VISUAL":
                return SegmentType.TILE_VISUAL;
            case "BUTTON":
                return SegmentType.BUTTON;
            default:
                return SegmentType.TEXT;
        }
    }

    /**
     * Measure the dimensions of all segments using the specified font
     */
    public static void measureSegments(List<TextSegment> segments, BitmapFont font) {
        measureSegments(segments, font, 30, 30); // Default UI element size
    }

    /**
     * Measure the dimensions of all segments
     *
     * @param segments List of segments to measure
     * @param font Font to use for text measurement
     * @param defaultUIWidth Default width for UI elements
     * @param defaultUIHeight Default height for UI elements
     */
    public static void measureSegments(List<TextSegment> segments, BitmapFont font,
                                     float defaultUIWidth, float defaultUIHeight) {
        GlyphLayout layout = new GlyphLayout();

        for (TextSegment segment : segments) {
            switch (segment.getType()) {
                case TEXT:
                    if (!segment.getText().isEmpty()) {
                        layout.setText(font, segment.getText());
                        segment.setWidth(layout.width);
                        segment.setHeight(layout.height);
                    }
                    break;

                case DROPDOWN:
                case BUTTON:
                    segment.setWidth(defaultUIWidth);
                    segment.setHeight(defaultUIHeight);
                    break;

                case TILE_VISUAL:
                    // Tile visuals can have variable size based on data
                    segment.setWidth(defaultUIWidth);
                    segment.setHeight(defaultUIHeight);
                    break;
            }
        }
    }

    /**
     * Calculate total width of all segments including padding
     */
    public static float calculateTotalWidth(List<TextSegment> segments, float padding) {
        float totalWidth = 0;
        for (int i = 0; i < segments.size(); i++) {
            totalWidth += segments.get(i).getWidth();
            if (i < segments.size() - 1) {
                totalWidth += padding;
            }
        }
        return totalWidth;
    }

    /**
     * Render only text segments with proper spacing for button positions
     */
    private static void renderTextOnlyWithButtonSpacing(GameScreen screen, List<TextSegment> segments,
                                                       float startX, float startY, float[] textColor,
                                                       DrawString drawString, float padding) {
        float currentX = startX;
        measureSegmentsWithRealButtonSizes(segments, AssetLoader.font15, screen);

        for (TextSegment segment : segments) {
            if (segment.getType() == SegmentType.TEXT && !segment.getText().isEmpty()) {
                screen.drawString(segment.getText(), currentX, startY,
                                textColor, AssetLoader.font15, drawString, true, false);
            }
            currentX += segment.getWidth() + padding;
        }
    }

    /**
     * Render question segments with dynamic positioning
     *
     * @param screen GameScreen for rendering
     * @param segments List of segments to render
     * @param startX Starting X position
     * @param startY Starting Y position
     * @param font Font for text rendering
     * @param textColor Color for text
     * @param padding Padding between segments
     * @param question Question instance for UI element callbacks
     */
    public static void renderQuestionSegments(GameScreen screen, List<TextSegment> segments,
                                            float startX, float startY, BitmapFont font,
                                            float[] textColor, float padding, Question question) {
        float currentX = startX;

        // Ensure segments are measured
        measureSegments(segments, font);

        int tileVisualIndex = 0;
        int dropdownIndex = 0;

        // First pass: Render all text segments (SpriteBatch mode)
        currentX = startX;
        for (TextSegment segment : segments) {
            if (segment.getType() == SegmentType.TEXT && !segment.getText().isEmpty()) {
                screen.drawString(segment.getText(), currentX, startY,
                                textColor, font, DrawString.BEGIN, true, false);
            }
            currentX += segment.getWidth() + padding;
        }

        // Second pass: Render all shape-based segments (ShapeRenderer mode)
        boolean hasShapeSegments = false;
        for (TextSegment segment : segments) {
            if (segment.getType() != SegmentType.TEXT) {
                hasShapeSegments = true;
                break;
            }
        }

        if (hasShapeSegments) {
            // Switch to shape rendering mode
            screen.spriteBatch.end();
            screen.getRenderer().begin(ShapeRenderer.ShapeType.Filled);

            currentX = startX;
            for (TextSegment segment : segments) {
                switch (segment.getType()) {
                    case DROPDOWN:
                        // Skip rendering placeholder - real dropdowns are positioned dynamically
                        dropdownIndex++;
                        break;

                    case TILE_VISUAL:
                        // Skip rendering placeholder - real buttons are positioned dynamically
                        tileVisualIndex++;
                        break;

                    case BUTTON:
                        // Skip rendering placeholder - real buttons are positioned dynamically
                        break;
                }
                currentX += segment.getWidth() + padding;
            }

            // Restore batch state
            screen.getRenderer().end();
            screen.spriteBatch.begin();
        }
    }

    /**
     * Render a dropdown UI element
     */
    private static void renderDropdown(GameScreen screen, float x, float y,
                                     TextSegment segment, Question question, int dropdownIndex) {
        // Get dropdown data from question
        Object dropdownData = null;
        if (question != null) {
            dropdownData = question.getDropdownData(dropdownIndex);
        }

        // Render dropdown background
        screen.getRenderer().setColor(Constants.COLOR_GREY[0], Constants.COLOR_GREY[1],
                                    Constants.COLOR_GREY[2], 1f);
        screen.getRenderer().rect(x, y - 15, segment.getWidth(), segment.getHeight());

        // Draw dropdown indicator
        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1],
                                    Constants.COLOR_BLACK[2], 1f);
        screen.getRenderer().rect(x + segment.getWidth() - 10, y - 10, 8, 5);

        // Render dropdown content if available
        if (dropdownData instanceof String) {
            screen.drawString((String) dropdownData, x + 5, y,
                            Constants.COLOR_BLACK, AssetLoader.font15,
                            DrawString.BEGIN, true, false);
        }
    }

    /**
     * Render a tile visual element
     */
    private static void renderTileVisual(GameScreen screen, float x, float y,
                                       TextSegment segment, Question question, int tileVisualIndex) {
        // Get tile data from question
        Object tileData = null;
        if (question != null) {
            tileData = question.getTileVisualData(tileVisualIndex);
        }

        if (tileData instanceof byte[][]) {
            renderTileFromByteArray(screen, x, y, (byte[][]) tileData, 12);
        } else {
            // Render simple tile placeholder for question UI
            renderSimpleTilePlaceholder(screen, x, y, segment.getWidth(), segment.getHeight());
        }
    }

    /**
     * Render a tile from byte array data
     */
    private static void renderTileFromByteArray(GameScreen screen, float x, float y,
                                              byte[][] tileData, int tileSize) {
        if (tileData == null) return;

        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1],
                                    Constants.COLOR_BLACK[2], 1f);

        int extraY = 0;
        if (tileData.length == 1) {
            extraY += tileSize;
        } else if (tileData.length == 2) {
            extraY += tileSize / 2;
        }

        float startY = y - tileData.length * tileSize / 2f;

        for (int row = 0; row < tileData.length; row++) {
            for (int col = 0; col < tileData[0].length; col++) {
                if (tileData[row][col] != 0) {
                    screen.getRenderer().rect(x + 1 + col * tileSize,
                                            startY + 1 + row * tileSize + extraY,
                                            tileSize - 2, tileSize - 2);
                }
            }
        }
    }

    /**
     * Render a simple tile placeholder for question UI
     */
    private static void renderSimpleTilePlaceholder(GameScreen screen, float x, float y,
                                                  float width, float height) {
        // Draw a simple tile representation
        screen.getRenderer().setColor(Constants.COLOR_GREY[0], Constants.COLOR_GREY[1],
                                    Constants.COLOR_GREY[2], 1f);
        screen.getRenderer().rect(x, y - height/2, width, height);

        // Draw border
        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1],
                                    Constants.COLOR_BLACK[2], 1f);
        screen.getRenderer().rect(x, y - height/2, width, 2); // top
        screen.getRenderer().rect(x, y + height/2 - 2, width, 2); // bottom
        screen.getRenderer().rect(x, y - height/2, 2, height); // left
        screen.getRenderer().rect(x + width - 2, y - height/2, 2, height); // right

        // Draw small pattern inside
        screen.getRenderer().setColor(Constants.COLOR_BLACK[0], Constants.COLOR_BLACK[1],
                                    Constants.COLOR_BLACK[2], 1f);
        float patternSize = Math.min(width, height) / 3;
        screen.getRenderer().rect(x + width/2 - patternSize/2, y - patternSize/2,
                                patternSize, patternSize);
    }

    /**
     * Render a button element
     */
    private static void renderButton(GameScreen screen, float x, float y,
                                   TextSegment segment, Question question) {
        // Placeholder for button rendering
        screen.getRenderer().setColor(Constants.COLOR_BUTTONS[0], Constants.COLOR_BUTTONS[1],
                                    Constants.COLOR_BUTTONS[2], 1f);
        screen.getRenderer().rect(x, y - 15, segment.getWidth(), segment.getHeight());
    }

    /**
     * Enhanced text and tile rendering that automatically handles both old and new formats
     * This method replaces the old renderTextAndTileQuestion method in ArcheOLogicPanel
     */
    public static float renderEnhancedTextAndTileQuestion(GameScreen screen, String text,
                                                        float startX, float startY,
                                                        boolean error, DrawString drawString) {
        // Check if this uses the new flexible format
        if (isFlexibleFormat(text)) {
            // Use new flexible rendering
            List<TextSegment> segments = parseQuestionTextFromString(text);

            float[] textColor = error ? Constants.COLOR_RED : Constants.COLOR_BLACK;

            // Check if we have any non-text segments
            boolean hasNonTextSegments = false;
            for (TextSegment segment : segments) {
                if (segment.getType() != SegmentType.TEXT) {
                    hasNonTextSegments = true;
                    break;
                }
            }

            // Improved padding for better spacing around buttons
            float improvedPadding = 8.0f;

            if (hasNonTextSegments) {
                // Use improved text-only rendering that accounts for button spacing
                renderTextOnlyWithButtonSpacing(screen, segments, startX, startY, textColor, drawString, improvedPadding);
            } else {
                // Simple text-only rendering - no need for complex batch management
                float currentX = startX;
                measureSegmentsWithRealButtonSizes(segments, AssetLoader.font15, screen);

                for (TextSegment segment : segments) {
                    if (segment.getType() == SegmentType.TEXT && !segment.getText().isEmpty()) {
                        screen.drawString(segment.getText(), currentX, startY,
                                        textColor, AssetLoader.font15, drawString, true, false);
                    }
                    currentX += segment.getWidth() + improvedPadding;
                }
            }

            // Calculate total width for return value
            measureSegmentsWithRealButtonSizes(segments, AssetLoader.font15, screen);
            return startX + calculateTotalWidth(segments, improvedPadding);
        } else {
            // Fall back to old rendering system for backward compatibility
            return renderOldStyleTextAndTileQuestion(screen, text, startX, startY, error, drawString);
        }
    }

    /**
     * Convenience method that uses BEGIN alignment by default
     */
    public static float renderEnhancedTextAndTileQuestion(GameScreen screen, String text,
                                                        float startX, float startY, boolean error) {
        return renderEnhancedTextAndTileQuestion(screen, text, startX, startY, error, DrawString.BEGIN);
    }

    /**
     * Old style rendering method for backward compatibility
     */
    private static float renderOldStyleTextAndTileQuestion(GameScreen screen, String text,
                                                         float realStartX, float y,
                                                         boolean error, DrawString drawString) {
        boolean run = true;
        float startX = realStartX;
        int startIndex = 0;
        while (run) {
            String curText = text.substring(startIndex);
            int nextIndex = text.substring(startIndex).indexOf("$");
            if (nextIndex > 0) {
                curText = text.substring(startIndex, startIndex + nextIndex);
                startIndex = startIndex + nextIndex + 2;
                Constants.glyphLayout.setText(AssetLoader.font15, curText);
            }
            float[] color = Constants.COLOR_BLACK;
            if (error) {
                color = Constants.COLOR_RED;
            }
            screen.drawString(curText, startX, y, color, AssetLoader.font15, drawString, true, false);

            if (nextIndex > 0) {
                startX += Constants.glyphLayout.width + 30;

                int value = 0;
                if (text.charAt(startIndex - 1) == 'F') {
                    value = 1;
                } else if (text.charAt(startIndex - 1) == 'G') {
                    value = 2;
                } else if (text.charAt(startIndex - 1) == 'W') {
                    value = 3;
                }
                screen.spriteBatch.draw(AssetLoader.backgroundTextureRegion[value], startX - 25, y - 11, 20, 20);
            } else {
                Constants.glyphLayout.setText(AssetLoader.font15, curText);
                startX += Constants.glyphLayout.width;
                run = false;
            }
        }
        return startX;
    }
}