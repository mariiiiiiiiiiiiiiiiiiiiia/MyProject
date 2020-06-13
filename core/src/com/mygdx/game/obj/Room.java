package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Const;
import com.mygdx.game.TextureManager;
import com.mygdx.game.factory.RoomSubjectFactory;
import com.mygdx.game.utils.FillType;

import org.json.JSONArray;
import org.json.JSONObject;

public class Room extends HouseObject {

    public Rectangle floor;
    public Rectangle earth;
    public RoomSubject roomSubjects[];
    private int id;
    private int houseX, houseY;
    private String backgroundFilename;
    private TextureRegion background;

    public Room(House house, JSONObject object) {
        super(house, object);
        id = object.optInt(FillType.ID);
        houseX = object.optInt(FillType.X);
        houseY = object.optInt(FillType.Y);
        JSONArray array = object.optJSONArray(FillType.SUBJECTS);
        if (array != null) {
            roomSubjects = new RoomSubject[array.length()];
            for (int i = 0; i < roomSubjects.length; i++) {
                roomSubjects[i] = RoomSubjectFactory.createSubject(array.optJSONObject(i), house, id);
            }
        }
        floor = new Rectangle(Const.LR_DOOR_W, 0, Const.SCREEN_WIDTH - 2 * Const.LR_DOOR_W, Const.H * 1.8f);
        earth = new Rectangle(Const.W * 9.92f, 0, 3.3f*Const.W , Const.H * 1.8f);

        backgroundFilename = object.optString(FillType.BACKGROUND);

        setBounds(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
    }

    @Override
    public TextureRegion getFrame(float dt) {
        return background;
    }

    public int getId() {
        return id;
    }

    public Rectangle getFloor() {
        return floor;
    }

    @Override
    public void update(float dt) {
        if (background == null)
            background = TextureManager.getInstance().getRoomBackground(backgroundFilename);

        super.update(dt);

        for (RoomSubject roomSubject : roomSubjects) {
            roomSubject.update(dt);
        }
    }

    public int getHouseX() {
        return houseX;
    }

    public int getHouseY() {
        return houseY;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        for (RoomSubject roomSubject : roomSubjects) {
            if (roomSubject != null)
                roomSubject.draw(batch);
        }
    }

    public RoomSubject getSubject(float x, float y) {
        for (int i = roomSubjects.length - 1; i >= 0; i--) {
            if (roomSubjects[i].isIntersect(x, y))
                return roomSubjects[i];
        }
        return null;
    }

    public boolean isFloorClick(float x, float y) {
        return floor.contains(x, y);
    }
    public boolean isEarthClick(float x, float y) {
        return earth.contains(x, y);
    }
    public RoomSubject getRoomSubject(int rsId) {
        for (RoomSubject roomSubject : roomSubjects) {
            if (roomSubject.getId() == rsId)
                return roomSubject;
        }
        return null;
    }
}
