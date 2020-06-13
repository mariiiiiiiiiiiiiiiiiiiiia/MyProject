package com.mygdx.game.shit.trigger;



public class CatchTrigger extends Trigger {

    public CatchTrigger(int id, int ifStep, boolean enabled) {
        super(id, Type.CATCH, ifStep, enabled);
    }

    @Override
    public boolean validate(Object[] params) {
        return true;
    }
}
