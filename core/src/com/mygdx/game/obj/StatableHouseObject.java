package com.mygdx.game.obj;

import org.json.JSONObject;


public abstract class StatableHouseObject<S extends Enum> extends HouseObject {

    public StatableHouseObject(House house, JSONObject object) {
        super(house, object);
    }

    public abstract S getState();

    public void dispose() {
        getTexture().dispose();
    }

}
