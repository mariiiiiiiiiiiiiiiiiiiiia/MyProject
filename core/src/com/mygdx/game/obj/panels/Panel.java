package com.mygdx.game.obj.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Const;
import com.mygdx.game.utils.MyText;

public abstract class Panel {

    static final float PANEL_HEIGHT = 2.0f * Const.H;
    MyText text;
    float maxTextWidth;

    private boolean isPanelVisible;
    private float standardHeight;
    private float maxTextHeight;

    Panel() {
        setPanelVisible(false);
        standardHeight = 0.3f * Const.H;
        text = new MyText("", standardHeight);
        maxTextHeight = PANEL_HEIGHT * 0.9f;
    }

    void changePanelText(String message) {
        text.setMessage(message);
        text.setHeight(standardHeight);
        if (text.getWidth() > maxTextWidth || text.getHeight() > maxTextHeight) {
            text.addEnters(maxTextWidth, maxTextHeight);
        }
    }

    void drawPanel(ShapeRenderer renderer) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0f, 0f, 0f, 0.64f);
        renderer.rect(0, 0, Const.SCREEN_WIDTH, PANEL_HEIGHT);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    void drawText(SpriteBatch batch) {
        batch.begin();
        text.draw(batch);
        batch.end();
    }

    public abstract void onClick(float x, float y);

    public void draw(ShapeRenderer renderer, SpriteBatch batch) {
        drawPanel(renderer);
        drawText(batch);
    }

    public boolean isPanelVisible() {
        return isPanelVisible;
    }

    public void setPanelVisible(boolean panelVisible) {
        isPanelVisible = panelVisible;
    }

    public void dispose() {
        text.dispose();
    }
}
