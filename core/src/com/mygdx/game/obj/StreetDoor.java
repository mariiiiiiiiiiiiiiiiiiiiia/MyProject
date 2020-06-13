package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.TextureManager;

import org.json.JSONObject;

public class StreetDoor extends CentralDoor {

    public StreetDoor(House house, JSONObject object) {
        super(house, object);
    }

    @Override
    public void draw(SpriteBatch batch, float delta) {
        Room cr = house.getCurrentRoom();
        Sprite centralDoor;
        if(cr == to) {
            centralDoor = TextureManager.getInstance().darkDoor();
        } else {
            centralDoor = TextureManager.getInstance().centralDoor();
        }
        if (cr == from || cr == to) {
            centralDoor.setX(getX());
            centralDoor.draw(batch);
        }
    }
}
