package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Const;
import com.mygdx.game.TextureManager;
import com.mygdx.game.utils.FillType;

import org.json.JSONObject;

public class Door {


    protected House house;
    protected String id;
    protected Room from;
    protected Room to;
    protected boolean visible;
    protected boolean closed;
    private String specialDescription;

    public Door(com.mygdx.game.obj.House house, JSONObject object) {
        this.house = house;
        id = object.optString(FillType.ID);
        from = house.getRoom(object.optInt(FillType.FROM));
        to = house.getRoom(object.optInt(FillType.TO));
        visible = object.optBoolean(FillType.VISIBLE);
        closed = object.optBoolean(FillType.CLOSED);
    }

    public String getId() {
        return id;
    }

    public Room getFrom() {
        return from;
    }

    public Room getTo() {
        return to;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void draw(SpriteBatch batch, float delta) {
        if (getType().equals(DoorType.FROM)) {
            TextureManager.getInstance().rightDoor().draw(batch);
        }

        if (getType().equals(DoorType.TO)) {
            TextureManager.getInstance().leftDoor().draw(batch);
        }
    }

    public DoorType getType() {
        if (house.getCurrentRoom() == from) {
            return DoorType.FROM;
        }

        if (house.getCurrentRoom() == to) {
            return DoorType.TO;
        }
        return DoorType.NOT_IN_ROOM;
    }

    public String getSpecialDescription() {
        return specialDescription;
    }

    public void setSpecialDescription(String specialDescription) {
        this.specialDescription = specialDescription;
    }

    public float getX() {
        if (getType().equals(DoorType.FROM)) {
            return Const.RIGHT_DOOR_X;
        }

        if (getType().equals(DoorType.TO)) {
            return Const.LEFT_DOOR_X;
        }
        return 0;
    }

    public float getY() {
        if (getType().equals(DoorType.FROM)) {
            return Const.RIGHT_DOOR_Y;
        }

        if (getType().equals(DoorType.TO)) {
            return Const.LEFT_DOOR_Y;
        }
        return 0;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public float getWidth() {
        return Const.LR_DOOR_W;
    }

    public float getHeight() {
        return Const.LR_DOOR_H;
    }

    public boolean touch(int x, int y) {
        if (getType().equals(DoorType.FROM)) {
            Rectangle doorRectangle = new Rectangle(Const.RIGHT_DOOR_X, Const.RIGHT_DOOR_Y, Const.LR_DOOR_W, Const.LR_DOOR_H);
            return doorRectangle.contains(x, y);
        }

        if (getType().equals(DoorType.TO)) {
            Rectangle doorRectangle = new Rectangle(Const.LEFT_DOOR_X, Const.LEFT_DOOR_Y, Const.LR_DOOR_W, Const.LR_DOOR_H);
            return doorRectangle.contains(x, y);
        }

        return false;
    }

    public void setTexture(String file) {
    }

    public enum DoorType {
        FROM, TO, NOT_IN_ROOM
    }


}
