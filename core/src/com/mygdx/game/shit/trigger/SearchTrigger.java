package com.mygdx.game.shit.trigger;


public class SearchTrigger extends Trigger {
    private int sid;

    public SearchTrigger(int id, int ifStep, int sid, boolean enabled) {
        super(id, Type.SEARCH, ifStep, enabled);
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    @Override
    public boolean validate(Object[] params) {
        if(params.length != 1)
            throw new IllegalArgumentException("SearchTrigger триггер должен принимать один параметр ROOM_SUBJECT ID");
        return params[0].equals(sid);
    }
}

