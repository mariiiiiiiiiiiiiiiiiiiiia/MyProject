package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class TextureManager {

    private static TextureManager instance;
    private TextureAtlas mainAtlas;
    private Sprite centralDoor;
    private Sprite rightDoor;
    private Sprite darkDoor;
    private Sprite leftDoor;
    private HashMap<String, TextureRegion> backgrounds = new HashMap<>();

    private TextureManager() {
    }

    public static TextureManager getInstance() {
        if (instance == null)
            instance = new TextureManager();
        return instance;
    }

    public Sprite centralDoor() {
        return centralDoor;
    }

    public Sprite rightDoor() {
        return rightDoor;
    }

    public Sprite leftDoor() {
        return leftDoor;
    }

    public Sprite darkDoor() {
        return darkDoor;
    }

    public void init(String filename) {
        mainAtlas = new TextureAtlas(Gdx.files.internal(filename));

        centralDoor = mainAtlas.createSprite(Const.CENTRAL_DOOR_IMAGE_FILEPATH);
        centralDoor.setBounds(0, Const.CENTRAL_DOOR_Y, Const.CENTRAL_DOOR_W, Const.CENTRAL_DOOR_H);

        darkDoor = mainAtlas.createSprite(Const.DARK_DOOR_IMAGE_FILEPATH);
        darkDoor.setBounds(0, Const.CENTRAL_DOOR_Y, Const.CENTRAL_DOOR_W, Const.CENTRAL_DOOR_H);

        rightDoor = mainAtlas.createSprite(Const.RIGHT_DOOR_IMAGE_FILEPATH);
        rightDoor.setBounds(Const.RIGHT_DOOR_X, Const.RIGHT_DOOR_Y, Const.LR_DOOR_W, Const.LR_DOOR_H);

        leftDoor = mainAtlas.createSprite(Const.LEFT_DOOR_IMAGE_FILEPATH);
        leftDoor.setBounds(Const.LEFT_DOOR_X, Const.LEFT_DOOR_Y, Const.LR_DOOR_W, Const.LR_DOOR_H);
        backgrounds.clear();
    }

    public Sprite sprite(String name) {
        Sprite sprite = mainAtlas.createSprite(name);
        if(sprite == null) {
            System.out.println();
        }
        return sprite;
    }

    public TextureRegion getRoomBackground(String filename) {
        TextureRegion region = backgrounds.get(filename);
        if (region == null) {
            region = new TextureRegion(new Texture(filename));
            backgrounds.put(filename, region);
        }
        return region;
    }

    public TextureRegion region(String filepath) {
        TextureAtlas.AtlasRegion region = mainAtlas.findRegion(filepath);
        if(region == null) {
            System.out.println();
        }
        return region;
    }


}
