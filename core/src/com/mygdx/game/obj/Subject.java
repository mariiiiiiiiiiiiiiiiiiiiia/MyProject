package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.TextureManager;
import com.mygdx.game.utils.FillType;

import org.json.JSONObject;


public class Subject {

    private int id;
    private boolean visible;
    private String description;
    private TextureRegion region;

    public Subject(JSONObject object) {
        id = object.optInt(FillType.ID);
        visible = object.optBoolean(FillType.VISIBLE, true);
        description = object.optString(FillType.DESCRIPTION, "");
        region = TextureManager.getInstance().region(object.optString(FillType.BACKGROUND));
    }

    public int getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getDescription() {
        return description;
    }

    public TextureRegion getRegion() {
        return region;
    }

}
