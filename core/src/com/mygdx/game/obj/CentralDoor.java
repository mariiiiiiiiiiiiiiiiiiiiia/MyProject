package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Const;
import com.mygdx.game.TextureManager;
import com.mygdx.game.utils.FillType;

import org.json.JSONObject;

public class CentralDoor extends Door {

    private float x;

    public CentralDoor(com.mygdx.game.obj.House house, JSONObject object) {
        super(house, object);
        x = (float) object.optDouble(FillType.X);
    }

    @Override
    public float getX() {
        return x * Const.W;
    }

    @Override
    public float getY() {
        return Const.CENTRAL_DOOR_Y;
    }

    @Override
    public float getWidth() {
        return Const.CENTRAL_DOOR_W;
    }

    @Override
    public float getHeight() {
        return Const.CENTRAL_DOOR_H;
    }

    @Override
    public void draw(SpriteBatch batch, float delta) {
        Room cr = house.getCurrentRoom();
        if (cr == from || cr == to) {
            Sprite centralDoor = TextureManager.getInstance().centralDoor();
            centralDoor.setX(getX());
            centralDoor.draw(batch);
        }
    }

    @Override
    public boolean touch(int x, int y) {
        if (!getType().equals(DoorType.NOT_IN_ROOM)) {
            Rectangle doorRectangle = new Rectangle(getX(), getY() + house.getCurrentRoom().floor.getHeight(), getWidth(), getHeight());
            return doorRectangle.contains(x, y);
        }
        return false;
    }
}
