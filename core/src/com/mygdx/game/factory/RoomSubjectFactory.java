package com.mygdx.game.factory;

import com.mygdx.game.obj.AnimatedRoomSubject;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.utils.FillType;

import org.json.JSONObject;

public class RoomSubjectFactory {

    public static RoomSubject createSubject(JSONObject object, House house, int roomId) {
        String type = object.optString(FillType.IS_STATIC_TYPE);
        if (type != null && type.equals(FillType.ANIMATION_TYPE_VALUE))
            return new AnimatedRoomSubject(house, object, roomId);
        return new RoomSubject(house, object, roomId);
    }
}
