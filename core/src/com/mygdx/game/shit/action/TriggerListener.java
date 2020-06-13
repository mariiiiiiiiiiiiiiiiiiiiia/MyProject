package com.mygdx.game.shit.action;

        import java.util.List;



public class TriggerListener {
    private int triggerId;
    private List<StepAction> stepActions;

    public TriggerListener(int triggerId, List<StepAction> stepActions) {
        this.triggerId = triggerId;
        this.stepActions = stepActions;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public List<StepAction> getStepActions() {
        return stepActions;
    }
}
