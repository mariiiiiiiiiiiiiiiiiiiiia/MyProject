package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.utils.MyText;

import java.util.Set;


public class InventarScreen implements Screen, InputProcessor {

    private House house;
    private MyGame myGame;
    private SpriteBatch houseBatch;
    private SpriteBatch batch;
    private Sprite inventarBackground;
    private ShapeRenderer renderer;

    private int maxX;
    private int maxY;


    private float offsetX;
    private float offsetY;

    private float rectW;
    private float rectH;

    private MyText text;

    private float maxTextWidth;
    private float maxTextHeight;
    private InventarObject inventarObjects[][];

    public InventarScreen(MyGame myGame, House house) {
        this.myGame = myGame;
        this.house = house;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        houseBatch = new SpriteBatch();
        inventarBackground = TextureManager.getInstance().sprite("inventar_back");
        float width = Const.W * 10.6f;
        float height = Const.W * 8.96f;
        inventarBackground.setBounds(Const.SCREEN_WIDTH / 2f - width / 2f, Const.SCREEN_HEIGHT / 2f - height / 2f, width, height);


        text = new MyText("", Const.H * 0.3f);
        maxX = 4;
        maxY = 2;

        offsetX = Const.W * 1.067f;
        offsetY = Const.H * 1.077f;

        float realW = inventarBackground.getWidth() - 2 * offsetX;
        float realH = inventarBackground.getHeight() - 2 * offsetY;

        rectW = realW / ((float) maxX);
        rectH = realH / ((float) (maxY + 1));

        float percentX = offsetX / inventarBackground.getWidth();
        float percentY = offsetY / inventarBackground.getHeight();

        float cubeOffsetX = rectW * percentX;
        float cubeOffsetY = rectH * percentY;

        float realSubjectW = rectW - 2 * cubeOffsetX;
        float realSubjectH = rectH - 2 * cubeOffsetY;

        maxTextWidth = realW * 0.8f;
        maxTextHeight = rectH * 0.5f;

        inventarObjects = new InventarObject[maxX][maxY];
        Set<Subject> subjects = house.getHero().getInventar().getSubjects();
        int x = 0;
        int y = maxY - 1;
        for (Subject subject : subjects) {
            float subX = inventarBackground.getX() + offsetX + rectW * x + cubeOffsetX;
            float subY = inventarBackground.getY() + offsetY + rectH * (y + 1) + cubeOffsetY;
            Sprite sprite = new Sprite(subject.getRegion());
            sprite.setBounds(subX, subY, realSubjectW, realSubjectH);
            inventarObjects[x][y] = new InventarObject(sprite, subject);
            x++;
            if (x == maxX) {
                x = 0;
                y--;
            }
        }
        renderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        houseBatch.begin();
        house.draw(houseBatch, 0f);
        houseBatch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0f, 0f, 0f, 0.44f);
        renderer.rect(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);



        batch.begin();
        inventarBackground.draw(batch);

        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                if (inventarObjects[i][j] != null) {
                    inventarObjects[i][j].sprite.draw(batch);
                }
            }
        }
        text.setPosition(inventarBackground.getX() + offsetX + rectW * maxX / 2f - text.getWidth() / 2f,
                inventarBackground.getY() + offsetY + rectH / 1.5f - text.getHeight() / 2f);

        text.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!inventarBackground.getBoundingRectangle().contains(screenX, Const.SCREEN_HEIGHT - screenY)) {
            myGame.goToGameScreen();
        } else {

            String description = null;
            boolean click = false;
            for (int i = 0; i < maxX; i++) {
                for (int j = 0; j < maxY; j++) {
                    if (inventarObjects[i][j] != null) {
                        if (inventarObjects[i][j].sprite.getBoundingRectangle().contains(screenX, Const.SCREEN_HEIGHT - screenY)) {
                            description = inventarObjects[i][j].subject.getDescription();
                            click = true;
                            break;
                        }
                    }
                }
            }
            if (!click) description = "";
            if (description != null) {
                text.setMessage(description);
                text.setHeight(Const.H * 0.3f);
                if (text.getWidth() > maxTextWidth || text.getHeight() > maxTextHeight) {
                    text.addEnters(maxTextWidth, maxTextHeight);
                    text.central();
                }
            }


        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private class InventarObject {
        Sprite sprite;
        Subject subject;

        public InventarObject(Sprite sprite, Subject subject) {
            this.sprite = sprite;
            this.subject = subject;
        }
    }
}
