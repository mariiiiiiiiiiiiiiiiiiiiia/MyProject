package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.json.JSONObject;


public abstract class HouseObject extends Sprite {
    private House house;

    public HouseObject(House house, JSONObject object) {
        this.house = house;
    }

    public House getHouse() {
        return house;
    }

    public abstract TextureRegion getFrame(float dt);

    public void update(float dt) {
        setRegion(getFrame(dt));
    }

    public void dispose() {
        Texture texture = getTexture();
        if(texture != null)
            texture.dispose();
    }
}
