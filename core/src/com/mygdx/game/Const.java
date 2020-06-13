package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Const {
    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth(); //ширина экран
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight(); //высота экрана
    public static final float W = SCREEN_WIDTH / 16.f; //ширина клеточки экрана
    public static final float H = SCREEN_HEIGHT / 9.f; //высоата клеточки экрана

    public static final float HERO_Y = H * 0.2f;// положение персонажа по у
    public static final float HERO_W = W * 2.2f; //ширина персонажа
    public static final float HERO_H = H * 4; //высота персонажа
    public static final String LELA_FACE_IMAGE_FILEPATH = "lela"; //лицо деовчки

    public static final Application.ApplicationType TYPE = Gdx.app.getType();

    public static final float RIGHT_DOOR_X = 14.84f * Const.W; //положение двери х (правая)
    public static final float RIGHT_DOOR_Y = 0; //положение двери у (правая)
    public static final float LEFT_DOOR_X = 0 * Const.W;//положение двери х (левая)
    public static final float LEFT_DOOR_Y = 0 * Const.H; //положение двери у (левая)

    public static final float LR_DOOR_W = 1.177f * Const.W; // ширина боковой двери
    public static final float LR_DOOR_H = 7.828f * Const.H; //высота боковой двери

    public static final float CENTRAL_DOOR_Y = 0.5175f * Const.H; //положение двери у (центр)
    public static final float CENTRAL_DOOR_W = 4.27475f * Const.W;// высота центральной двери
    public static final float CENTRAL_DOOR_H = 7.857f * Const.H;// ширина цетральной двери

    public static final String CENTRAL_DOOR_IMAGE_FILEPATH = "doors/central"; // путь до ц. двери
    public static final String RIGHT_DOOR_IMAGE_FILEPATH = "doors/right"; //путь до п. двери
    public static final String LEFT_DOOR_IMAGE_FILEPATH = "doors/left";//  путь до л. двери
    public static final String DARK_DOOR_IMAGE_FILEPATH = "doors/dark_central" ;

    public static final String INVENTAR_IMAGE_FILEPATH = "InventBut"; //путь до кнопки инвент.
    public static final String BUTTON_SEARCH_IMAGE_FILEPATH = "buttons/search";//путь до кнопки инвент.
    public static final String BUTTON_HIDE_IMAGE_FILEPATH = "buttons/hide";//путь до кнопки инвент.

    public static final String MENU_BACK_S_FILEPATH = "menu/back.png";
    public static final String MENU_NGAME_S_FILEPATH = "menu/startgame.png";
    public static final String MENU_RGAME_S_FILEPATH = "menu/resgame.png";
    public static final String MENU_SGAME_S_FILEPATH = "menu/setgame.png";
    public static final String MENU_HGAME_S_FILEPATH = "menu/helpgame.png";
    public static final String MENU_EGAME_S_FILEPATH = "menu/exitgame.png";

    public static final String BACKGROUND_MUSIC_FILEPATH = "songs/song2.mp3";//музыка на заднем плане ФОН

    public static final String MENU_MUSIC_FILEPATH = "songs/menu.mp3";//музыка на заднем плане МЕНЮ
    public static final String JSON = "All_Things";
    public static final String SAVE = "walker/save.json";
    public static final String QUEST = "quests/quest.xml";
    public static final String CUT_SCENE = "quests/cutscene.xml";
}
