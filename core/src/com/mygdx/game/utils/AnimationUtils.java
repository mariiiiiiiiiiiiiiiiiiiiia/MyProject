package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.TextureManager;

import org.json.JSONObject;


public class AnimationUtils {

    public static Animation createAnimation(JSONObject json) {
        int w = json.optInt(FillType.ANIMATION_FRAMES_COL);
        int h = json.optInt(FillType.ANIMATION_FRAMES_ROW);
        float frameDuration = (float) json.optDouble(FillType.ANIMATION_FRAME_DURATION);
        String animationFilepath = json.optString(FillType.ATLAS_ANIMATION_FILEPATH);
        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                TextureRegion region = TextureManager.getInstance().region(animationFilepath);
                float frame_w = region.getRegionWidth() / w;
                float frame_h = region.getRegionHeight() / h;
                int x = (int) (i * frame_w);
                int y = (int) (j * frame_h);
                frames.add(new TextureRegion(region, x, y, (int) frame_w, (int) frame_h));
            }
        }

        String animType = json.optString(FillType.ANIMATION_TYPE);
        if (animType.equals(""))
            animType = "NORMAL";
        Animation.PlayMode playMode = Animation.PlayMode.valueOf(animType);
        return new Animation(frameDuration, frames, playMode);
    }
}
