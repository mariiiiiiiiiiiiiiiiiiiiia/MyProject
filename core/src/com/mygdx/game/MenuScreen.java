package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MenuScreen implements Screen, InputProcessor { //скрин меню

    private final MyGame myGame;
    private Sprite menubackSprite;
    private SpriteBatch menubatch;
    private Sprite newgameSprite;
    private Sprite resgameSprite;
    private Sprite helpgameSprite;
    private Sprite exitgameSprite;


    public MenuScreen(MyGame myGame) {
        this.myGame = myGame;

    }

    @Override
    public void show() { //показывает нам
        MusicManager.playMusic(Const.MENU_MUSIC_FILEPATH, true);
        menubatch = new SpriteBatch();
        menubackSprite = new Sprite(new Texture(Gdx.files.internal(Const.MENU_BACK_S_FILEPATH)));
        menubackSprite.setSize(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);

        newgameSprite = new Sprite(new Texture(Gdx.files.internal(Const.MENU_NGAME_S_FILEPATH)));
        newgameSprite.setBounds(0.456f * Const.W, 5.19f * Const.H, 3.5f * Const.W, 0.78f * Const.H);

        resgameSprite = new Sprite(new Texture(Gdx.files.internal(Const.MENU_RGAME_S_FILEPATH)));
        resgameSprite.setBounds(0.456f * Const.W, 3.889f * Const.H, 3.5f * Const.W, 0.78f * Const.H);

        helpgameSprite = new Sprite(new Texture(Gdx.files.internal(Const.MENU_HGAME_S_FILEPATH)));
        helpgameSprite.setBounds(0.456f * Const.W, 2.74f * Const.H, 3.5f * Const.W, 0.78f * Const.H);

        Sprite setgameSprite = new Sprite(new Texture(Gdx.files.internal(Const.MENU_SGAME_S_FILEPATH)));
        setgameSprite.setBounds(0.456f * Const.W, 1.603f * Const.H, 3.5f * Const.W, 0.78f * Const.H);

        exitgameSprite = new Sprite(new Texture(Gdx.files.internal(Const.MENU_EGAME_S_FILEPATH)));
        exitgameSprite.setBounds(0.456f * Const.W, 0.46f * Const.H, 3.5f * Const.W, 0.78f * Const.H);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) { //рисуется каждую секунду
        menubatch.begin();
        menubackSprite.draw(menubatch);
        newgameSprite.draw(menubatch);
        resgameSprite.draw(menubatch);
        exitgameSprite.draw(menubatch);
//        setgameSprite.draw(menubatch);
        helpgameSprite.draw(menubatch);

        menubatch.end();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { //реагирует на касание
        final int realX = screenX;
        final int realY = Const.SCREEN_HEIGHT - screenY;
        if (newgameSprite.getBoundingRectangle().contains(realX, realY)) { //переходим на игру без сохранения
            myGame.goToGameScreen1(false);
        }
        if (resgameSprite.getBoundingRectangle().contains(realX, realY)) { //переходим на игру с сохранением
            myGame.goToGameScreen1(true);
        }
        if (helpgameSprite.getBoundingRectangle().contains(realX, realY)) {  //выходим из игры
            myGame.goToHelpScreen();

        }
        if (exitgameSprite.getBoundingRectangle().contains(realX, realY)) {  //выходим из игры
            myGame.dispose();
            Gdx.app.exit();
        }


        return true;
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
