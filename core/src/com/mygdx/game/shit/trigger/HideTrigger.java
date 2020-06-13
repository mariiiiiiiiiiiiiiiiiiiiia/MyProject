package com.mygdx.game.shit.trigger;


public class HideTrigger extends Trigger {
    private int rsid;

    public HideTrigger(int id, int ifStep, int sid, boolean enabled) {
        super(id, Type.HIDE, ifStep, enabled);
        this.rsid = sid;
    }

    public int getRsid() {
        return rsid;
    }

    @Override
    public boolean validate(Object[] params) {
        if(params.length != 1)
            throw new IllegalArgumentException("HideTrigger триггер должен принимать один параметр ROOM_SUBJECT ID");
        return params[0].equals(rsid);
    }
}

