package com.mygdx.game.shit.trigger;

public class FindTrigger extends Trigger {
    private int sid;

    public FindTrigger(int id, int ifStep, int sid, boolean enabled) {
        super(id, Type.FIND, ifStep, enabled);
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    @Override
    public boolean validate(Object[] params) {
        if(params.length != 1)
            throw new IllegalArgumentException("ComeInTrigger триггер должен принимать один параметр SUBJECT ID");
        return params[0].equals(sid);
    }
}
