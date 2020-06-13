package com.mygdx.game.shit.action;

import java.util.List;


public class StepListener {

    private int stepNumber;
    private List<StepAction> stepActions;

    public StepListener(int stepNumber, List<StepAction> stepActions) {
        this.stepNumber = stepNumber;
        this.stepActions = stepActions;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public List<StepAction> getStepActions() {
        return stepActions;
    }
}
