package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Const;
import com.mygdx.game.MyGame;


public class HelpScreen implements Screen, InputProcessor {


    private final MyGame myGame;
    private SpriteBatch helpbatch;
    private Sprite helpSprite;
   public int id;
    public String[] textures = {"help/1.png", "help/2.png","help/3.png","help/4.png","help/5.png","help/6.png","help/7.png","help/8.png","help/9.png","help/10.png","help/11.png","help/12.png","help/13.png"};

    public HelpScreen(MyGame myGame) {
        this.myGame = myGame;


    }

    @Override
    public void show() { //показывает нам
        helpbatch = new SpriteBatch();
        id = 0;
        String[] textures = {"help/1.png", "help/2.png","help/2.png","help/2.png","help/2.png","help/2.png"};
        helpSprite = new Sprite(new Texture(Gdx.files.internal(textures[id])));
        helpSprite.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
        helpSprite.setBounds(0 * Const.W, 0* Const.H, 16 * Const.W, 9 * Const.H);
        Gdx.input.setInputProcessor(this);


    }

    @Override
    public void render(float delta) { //рисуется каждую секунду
        helpbatch.begin();
        helpSprite.draw(helpbatch);
        helpbatch.end();
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { //реагирует на касание
        final int realX = screenX;
        final int realY = Const.SCREEN_HEIGHT - screenY;
        if ( helpSprite.getBoundingRectangle().contains(realX, realY)) { //переходим на игру без сохранения
            id = id + 1;
            if (id == 13) {
                myGame.goToMenuScreen();

            }else{
            helpSprite.set(new Sprite(new Texture(Gdx.files.internal(textures[id]))));
            helpSprite.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
            helpSprite.setBounds(0 * Const.W, 0* Const.H, 16 * Const.W, 9 * Const.H);}

        }
        return true;
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


}
