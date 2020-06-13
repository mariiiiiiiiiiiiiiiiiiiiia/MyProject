package com.mygdx.game.shit.level;

import com.mygdx.game.obj.House;
import com.mygdx.game.shit.Dialog;
import com.mygdx.game.shit.Monolog;
import com.mygdx.game.shit.action.Action;
import com.mygdx.game.shit.action.StepAction;
import com.mygdx.game.shit.action.TriggerListener;
import com.mygdx.game.shit.trigger.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Level {

    private String name;
    private Map<Integer, Dialog> dialogsMap;
    private Map<Integer, Monolog> monologsMap;
    private List<Step> steps;
    private Map<Integer, Integer> ghostDialogs;
    private final List<Trigger> triggers;
    private final List<TriggerListener> triggerListeners;
    private House house;


    public Level(String name, Map<Integer, Dialog> dialogsMap, Map<Integer, Monolog> monologsMap, List<Step> steps,
                 List<Trigger> triggers, List<TriggerListener> triggerListeners, House house) {
        this.name = name;
        this.dialogsMap = dialogsMap;
        this.monologsMap = monologsMap;
        this.steps = steps;
        this.ghostDialogs = new HashMap<>();
        this.triggers = triggers;
        this.triggerListeners = triggerListeners;
        this.house = house;
    }

    @Override
    public String toString() {
        return "Level{" +
                "name='" + name + '\'' +
                ", dialogsMapSize=" + dialogsMap.size() +
                ", monologsMapSize=" + monologsMap.size() +
                ", stepsSize=" + steps.size() +
                '}';
    }

    public Step getStep(int currentStep) {
        return steps.get(currentStep);
    }

    public List<Step> getsteps() {
        return steps;
    }

    public void changeDialog(int ghostId, int newDialog) {
        ghostDialogs.put(ghostId, newDialog);
    }

    public Monolog getMonolog(int id) {
        return monologsMap.get(id);
    }

    public Dialog getDialog(int id) {
        return dialogsMap.get(id);
    }

    public Dialog getDialogByGhost(int id) {
        return  dialogsMap.get(ghostDialogs.get(id));
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void executeTriggerListener(int tid, boolean save) {
        for (TriggerListener listener : triggerListeners) {
            if(listener.getTriggerId() == tid) {
                iterateStepActions(listener.getStepActions(), save);
                return;
            }
        }
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

    public LQuest getQuest(String name) {
        for (Step step : steps) {
            for (LQuest lQuest : step.getlQuests()) {
                if(lQuest.getName().equals(name))
                    return lQuest;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<LQuest> getQuests() {
        List<LQuest> lQuests = new ArrayList<>();
        for (Step step : steps) {
            for (LQuest lQuest : step.getlQuests()) {
                lQuests.add(lQuest);
            }
        }
        return lQuests;
    }

    public House getHouse() {
        return house;
    }
}