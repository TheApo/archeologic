package com.apogames.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * Custom Input Processor für HTML5 um Mouse Move Events zu filtern
 * und nur relevante Events zum Rendering weiterzuleiten
 */
public class HtmlInputProcessor implements InputProcessor {

    private final InputProcessor originalProcessor;
    private boolean needsRendering = false;
    private long lastRenderTime = 0;
    private static final long MIN_RENDER_INTERVAL = 33; // Max 10fps bei Mouse Move

    public HtmlInputProcessor(InputProcessor originalProcessor) {
        this.originalProcessor = originalProcessor;
    }

    private void requestRenderingThrottled() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRenderTime > MIN_RENDER_INTERVAL) {
            HtmlRenderingController.requestRender();
            lastRenderTime = currentTime;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean result = originalProcessor.keyDown(keycode);
        HtmlRenderingController.requestRender(); // Wichtige Events sofort
        return result;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean result = originalProcessor.keyUp(keycode);
        HtmlRenderingController.requestRender(); // Wichtige Events sofort
        return result;
    }

    @Override
    public boolean keyTyped(char character) {
        boolean result = originalProcessor.keyTyped(character);
        HtmlRenderingController.requestRender(); // Wichtige Events sofort
        return result;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        boolean result = originalProcessor.touchDown(screenX, screenY, pointer, button);
        HtmlRenderingController.requestRender(); // Wichtige Events sofort
        return result;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean result = originalProcessor.touchUp(screenX, screenY, pointer, button);
        HtmlRenderingController.requestRender(); // Wichtige Events sofort
        return result;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean result = originalProcessor.touchDragged(screenX, screenY, pointer);
        requestRenderingThrottled(); // Mouse Drag gedrosselt
        return result;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        boolean result = originalProcessor.mouseMoved(screenX, screenY);
        // Mouse Move nur gedrosselt rendern (max 10fps)
        requestRenderingThrottled();
        return result;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        boolean result = originalProcessor.scrolled(amountX, amountY);
        HtmlRenderingController.requestRender(); // Wichtige Events sofort
        return result;
    }
}
