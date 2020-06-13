package com.mygdx.game.factory;

import com.mygdx.game.obj.CentralDoor;
import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.StreetDoor;
import com.mygdx.game.utils.FillType;
import org.json.JSONObject;

public class DoorFactory {

    public static Door createDoor(House house, JSONObject object) {
        if (object.optString(FillType.TYPE).equals("street"))
            return new StreetDoor(house, object);
        if (object.optString(FillType.TYPE).equals("central"))
            return new CentralDoor(house, object);
        return new Door(house, object);
    }
}
