package com.mygdx.game.shit;

import com.mygdx.game.obj.House;
import com.mygdx.game.shit.action.Action;
import com.mygdx.game.shit.action.StepAction;
import com.mygdx.game.shit.action.StepListener;
import com.mygdx.game.shit.action.TriggerListener;
import com.mygdx.game.shit.trigger.Trigger;
import com.mygdx.game.utils.SaveUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {

    private static Quest currentQuest;
    private final List<StepListener> stepListeners;
    private final List<TriggerListener> triggerListeners;
    private int step;
    private Map<Integer, Dialog> dialogs;
    private Map<Integer, Monolog> monologsMap;
    private List<Trigger> triggers;
    private Map<Integer, Integer> ghostDialogs;
    private String name;
    private House house;

    public boolean isCutScenePlaying;
    public Quest(House house, Map<Integer, Dialog> dialogs, Map<Integer, Monolog> monologsMap, List<Trigger> triggers, List<StepListener> stepListeners, List<TriggerListener> triggerListeners, String name) {
        this.house = house;
        this.name = name;
        this.dialogs = dialogs;
        this.monologsMap = monologsMap;
        this.triggers = triggers;
        this.stepListeners = stepListeners;
        this.triggerListeners = triggerListeners;
        step = 0;
        ghostDialogs = new HashMap<>();
        ghostDialogs.put(1, 1);
        isCutScenePlaying = false;
    }


    public House getHouse() {
        return house;
    }

    public static Quest getCurrentQuest() {
        return currentQuest;
    }

    public static void setCurrentQuest(Quest currentQuest) {
        Quest.currentQuest = currentQuest;
    }

    public Dialog getDialogById(int id) {
        return dialogs.get(id);
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void changeDialog(int ghostId, int newDialog) {
        ghostDialogs.put(ghostId, newDialog);
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Monolog getMonolog(int id) {
        return monologsMap.get(id);
    }

    public void incStep() {
        step++;
        executeStepListener(step, false);
        SaveUtils.save(house);
    }

    public boolean isCutScenePlaying() {
        return isCutScenePlaying;
    }

    public void executeListenersForPreveousSteps(boolean save) {
        for (int ms = 0; ms <= step; ms++) {
            executeStepListener(ms, save);
        }
    }

    public String getName() {
        return name;
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
    public void iterateStepActions(List<StepAction> stepActions, boolean save) { //сохранение в старом формате работы квестов. В будущем будет переделано для нового формата.
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

    public void executeStepListener(int step, boolean save) {
        for (StepListener listener : stepListeners) {
            if (listener.getStepNumber() == step) {
                iterateStepActions(listener.getStepActions(), save);
                return;
            }
        }
    }

    public Dialog getDialogByGhost(int ghostId) {
        return dialogs.get(ghostDialogs.get(ghostId));
    }



}
