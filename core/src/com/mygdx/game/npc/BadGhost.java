package com.mygdx.game.npc;

import com.mygdx.game.Const;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;

import org.json.JSONObject;



public class BadGhost extends Ghost {

    public boolean walkingRight;

    public BadGhost(House house, JSONObject object) {
        super(house, object);
        walkingRight = true;
    }

    public boolean isWalkingRight() {
        return walkingRight;
    }

    public void setWalkingRight(boolean walkingRight) {
        this.walkingRight = walkingRight;
    }

    public boolean isIntersect(Hero hero) {
        return getBoundingRectangle().overlaps(hero.getBoundingRectangle());
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (!walkAnimation.isEnabled()) {
            State state = getState();
            if (state == State.AGR) {
                final float velX = 7f * Const.W;
                float k = walkingRight ? 1 : -1;
                float x = getX() + velX * dt * k;
                setX(x);
            }
        }
    }
}
