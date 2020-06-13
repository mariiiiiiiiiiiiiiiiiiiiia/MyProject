package com.mygdx.game.obj.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Const;
import com.mygdx.game.TextureManager;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.shit.Monolog;



public class MonologPanel extends Panel {
    private static MonologPanel instance;
    private Sprite lelaSprite;

    private final float faceWidth = Const.W * 2.418f;
    private final float faceOffsetX = 0.1f * faceWidth;
    public float stateTime;
    private Hero hero;
    private Monolog monolog;

    public MonologPanel() {
        lelaSprite = TextureManager.getInstance().sprite(Const.LELA_FACE_IMAGE_FILEPATH);
        float faceOffsetY = 0;
        float faceHeight = Const.H * 2.32f;
        lelaSprite.setBounds(faceOffsetX, faceOffsetY, faceWidth, faceHeight);
        maxTextWidth = Const.SCREEN_WIDTH - 3 * faceOffsetX - faceWidth;
        stateTime = 0f;
    }

    public static MonologPanel getInstance() {
        if (instance == null)
            instance = new MonologPanel();
        return instance;
    }

    @Override
    public void onClick(float x, float y) {
        String message = monolog.nextText();
        if(message == null) {
            monolog.setFirstIndex();
            setPanelVisible(false);
        } else {
            changePanelText(message);
        }
    }

    public void startMonolog(House house, Monolog monolog) {
        this.monolog = monolog;
        changePanelText(monolog.nextText());
        setPanelVisible(true);
        hero = house.getHero();
    }

    @Override
    public void draw(ShapeRenderer renderer, SpriteBatch batch) {
        drawPanel(renderer);
        float dialogTextY = PANEL_HEIGHT / 2f + text.getHeight() / 2f;
        TextureRegion keyFrame = (TextureRegion) hero.faceAnimationh.getKeyFrame(stateTime);
        Rectangle r = lelaSprite.getBoundingRectangle();
        batch.begin();
        batch.draw(keyFrame, r.getX(), r.getY(), r.getWidth(), r.getHeight());
        batch.end();
        float x = 2 * faceOffsetX + faceWidth + maxTextWidth / 2f - text.getWidth() / 2f;
        text.setPosition(x, dialogTextY);
        drawText(batch);
        stateTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
