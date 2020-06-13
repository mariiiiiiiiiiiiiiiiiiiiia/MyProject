package com.mygdx.game.shit.action;

import java.util.Map;

public class StepAction {
    private String actionName;
    private Map<String, String> params;

    public StepAction(String actionName, Map<String, String> params) {
        this.actionName = actionName;
        this.params = params;
    }

    public String getActionName() {
        return actionName;
    }

    public Map<String, String> getParams() {
        return params;
    }
}