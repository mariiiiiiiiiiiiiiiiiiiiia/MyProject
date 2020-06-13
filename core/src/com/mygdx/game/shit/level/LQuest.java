package com.mygdx.game.shit.level;

import com.mygdx.game.obj.House;
import com.mygdx.game.shit.action.Action;
import com.mygdx.game.shit.action.StepAction;
import com.mygdx.game.shit.action.TriggerListener;
import com.mygdx.game.shit.trigger.Trigger;

import java.util.List;



public class LQuest {

    private House house;
    public final List<Trigger> triggers;
    private final List<StepAction> onStart;
    private final List<StepAction> onEnd;
    private final List<TriggerListener> triggerListeners;
    private int id;
    private String name;
    private boolean visible;
    private boolean isComplete;

    public LQuest(int id, String name, List<Trigger> triggers,
                  List<TriggerListener> triggerListeners, List<StepAction> onStart,
                  List<StepAction> onEnd, boolean visible, House house) {
        this.id = id;
        this.name = name;
        this.triggers = triggers;
        this.triggerListeners = triggerListeners;
        this.onStart = onStart;
        this.onEnd = onEnd;
        this.visible = visible;
        this.house = house;
        isComplete = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<StepAction> getOnStart() {
        return onStart;
    }

    public List<StepAction> getOnEnd() {
        return onEnd;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "LQuest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", visible=" + visible +
                '}';
    }

    public void executeTriggerListener(int tid, boolean save) {
        for (TriggerListener listener : triggerListeners) {
            if(listener.getTriggerId() == tid) {
                iterateStepActions(listener.getStepActions(), save);
                return;
            }
        }
    }

    public void executeOnStart(boolean save) {
        iterateStepActions(onStart, save);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void executeOnEnd(boolean save) {
        iterateStepActions(onEnd, save);
    }

    //save = true если мы продолжаем игру
    public void iterateStepActions(List<StepAction> stepActions, boolean save) {
        for (StepAction stepAction : stepActions) {
            String actionName = stepAction.getActionName();

            String ignoreActions[] = new String[] {
                    "start-dialog", "start-monolog", "start-cut-scene"
            };
            boolean find = false;
            if(save) {
                for (String action : ignoreActions) {
                    if(action.equals(actionName)) {
                        find = true;
                        break;
                    }
                }
            }
            if(!find)
                Action.doAction(actionName, house, stepAction.getParams());
        }
    }
    public  Trigger getTrigger(int id) {
        for (Trigger trigger : triggers) {
            if(trigger.getId() == id)
                return trigger;
        }
        return null;
    }
}
