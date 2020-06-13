package com.mygdx.game.shit.trigger;



public class TalkTrigger extends Trigger {

    private int dialogId;
    private int dialogGroupId;

    public TalkTrigger(int id, int ifStep, int dialogId, int dialogGroupId, boolean enabled) {
        super(id, Type.TALK, ifStep, enabled);
        this.dialogId = dialogId;
        this.dialogGroupId = dialogGroupId;
    }

    public int getDialogId() {
        return dialogId;
    }

    public int getDialogGroupId() {
        return dialogGroupId;
    }

    @Override
    public boolean validate(Object[] params) {
        if(params.length != 2)
            throw new IllegalArgumentException("TalkTrigger триггер должен принимать " +
                    "два параметра dialogId и dialogGroupId");
        return params[0].equals(dialogId) && params[1].equals(dialogGroupId);
    }
}
