package com.mygdx.game.shit.trigger;



public abstract class Trigger {

    private int id;
    private Type type;
    private int ifStep;
    private boolean enabled;

    public Trigger(int id, Type type, int ifStep, boolean enabled) {
        this.id = id;
        this.type = type;
        this.ifStep = ifStep;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public int getIfStep() {
        return ifStep;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract boolean validate(Object[] params);

    public enum Type {
        CLICK, TALK, FIND, CATCH, COME_IN, SEARCH, HIDE
    }
}
