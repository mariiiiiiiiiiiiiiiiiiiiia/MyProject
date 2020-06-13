package com.mygdx.game.shit.trigger;

public class ComeInTrigger extends Trigger {

    private int roomId;

    public ComeInTrigger(int id, int ifStep, int roomId, boolean enabled) {
        super(id, Type.COME_IN, ifStep, enabled);
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public boolean validate(Object[] params) {
        if(params.length != 1)
            throw new IllegalArgumentException("ComeInTrigger триггер должен принимать один параметр ROOM ID");
        return params[0].equals(roomId);
    }
}
