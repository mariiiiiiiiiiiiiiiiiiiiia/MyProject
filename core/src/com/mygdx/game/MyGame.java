package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game {

    public GameScreen gameScreen;
    private MenuScreen menuScreen;
    private HelpScreen helpScreen;


    public void create() { //запуск игры
        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);
        helpScreen = new HelpScreen(this);
        setScreen(menuScreen); //ставим экран меню
    }

    public void goToGameScreen() { //переход к экрану игры
        gameScreen.reloadTextures = false; //не трогаем текстуры
        gameScreen.isSave = false;
        setScreen(gameScreen);
    }


    public void goToGameScreen1(boolean save) {//переход к экрану игры (с сохранением или без)
        gameScreen.reloadTextures = true; //пересобираем текстуры
        gameScreen.isSave = save;
        setScreen(gameScreen);
    }
    public void goToMenuScreen() { //переход к экрану игры
        setScreen( menuScreen);
    }

    public void goToHelpScreen() { //переход к экрану игры
        setScreen( helpScreen);
    }
}
