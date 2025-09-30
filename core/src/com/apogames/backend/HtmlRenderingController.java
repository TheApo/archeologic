package com.apogames.backend;

import com.badlogic.gdx.Gdx;

/**
 * HTML5-spezifischer Rendering Controller
 * Umgeht das Problem dass setContinuousRendering(false) in GWT nicht funktioniert
 */
public class HtmlRenderingController {

    private static boolean shouldRender = true;
    private static long lastRenderTime = 0;
    private static long renderInterval = 1000; // Standard: 1 Sekunde zwischen Renders
    private static boolean forceNextRender = false;

    /**
     * Prüft ob gerendert werden soll
     */
    public static boolean shouldRender() {
        long currentTime = System.currentTimeMillis();

        // Force render wenn explizit angefordert
        if (forceNextRender) {
            forceNextRender = false;
            lastRenderTime = currentTime;
            return true;
        }

        // Zeitbasiertes Rendering
        if (currentTime - lastRenderTime >= renderInterval) {
            lastRenderTime = currentTime;
            return true;
        }

        return false;
    }

    /**
     * Fordert das nächste Rendering an
     */
    public static void requestRender() {
        forceNextRender = true;
    }

    /**
     * Setzt das Render-Intervall in Millisekunden
     */
    public static void setRenderInterval(long intervalMs) {
        renderInterval = intervalMs;
    }

    /**
     * Aktiviert/Deaktiviert kontinuierliches Rendering
     */
    public static void setContinuousRendering(boolean continuous) {
        if (continuous) {
            renderInterval = 16; // ~60fps
        } else {
            renderInterval = Long.MAX_VALUE; // Nur bei expliziter Anforderung
        }
    }
}