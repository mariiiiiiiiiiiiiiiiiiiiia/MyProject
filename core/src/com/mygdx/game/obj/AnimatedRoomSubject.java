package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.utils.AnimationUtils;
import com.mygdx.game.utils.FillType;

import org.json.JSONObject;

public class AnimatedRoomSubject extends RoomSubject {

    private float animationTimer;
    private Animation subjectAnimation;

    public AnimatedRoomSubject(House house, JSONObject object, int roomId) {
        super(house, object, roomId);
        JSONObject animation = object.optJSONObject(FillType.ANIMATION_PARAMS);
        subjectAnimation = AnimationUtils.createAnimation(animation);
        System.out.println();
    }

    @Override
    public TextureRegion getFrame(float dt) {
        TextureRegion region = (TextureRegion) subjectAnimation.getKeyFrame(animationTimer, true);
        animationTimer += dt;
        return region;
    }
}
