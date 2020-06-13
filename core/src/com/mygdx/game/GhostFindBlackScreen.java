package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.panels.BottomPanel;
import com.mygdx.game.obj.panels.DialogPanel;
import com.mygdx.game.shit.BigListener;
import com.mygdx.game.shit.level.LevelManager;

public class GhostFindBlackScreen implements Screen, InputProcessor {

    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private House house;
    private float alpha;
    private float alphaVel;
    private Color color;
    private MyGame myGame;
    private Ghost ghost;
    private Sprite fail;

    public GhostFindBlackScreen(House house, MyGame myGame, Ghost ghost) {
        this.house = house;
        this.myGame = myGame;
        this.ghost = ghost;
    }

    @Override
    public void show() {
        DialogPanel.getInstance().setPanelVisible(false);
        BottomPanel.getInstance().setPanelVisible(false);
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        alpha = 0f;
        alphaVel = 200f / 255f;
        color = Color.BLACK;
        color.set(0f, 0f, 0f, alpha);
        fail = TextureManager.getInstance().sprite("NPC/fail");
        fail.setBounds(0,0,Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        house.draw(batch, 0f);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        renderer.rect(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        alpha = alpha + alphaVel * delta;
        if (alpha >= 4f) {
            alpha = 1f;
            alphaVel = -alphaVel;
            house.setCurrentRoom(house.getRoom(14), null);
            Hero hero = house.getHero();
            hero.setX(Const.W * 13f);
        } else if(alpha >= 1) {
            batch.begin();
            fail.draw(batch);
            batch.end();
        } else if (alpha <= 0) {
            myGame.goToGameScreen();
            ghost.returnToStartPoint();
            BigListener.getInstance().catchMe();
            LevelManager.getInstance().catchMe();
        }
        System.out.println(alpha);
        color.set(0f, 0f, 0f, alpha);
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
}
