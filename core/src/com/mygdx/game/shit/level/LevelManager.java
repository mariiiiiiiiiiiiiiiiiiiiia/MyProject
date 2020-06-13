package com.mygdx.game.shit.level;

import com.mygdx.game.MusicManager;
import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Room;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.save.SaveWriter;
import com.mygdx.game.shit.Dialog;
import com.mygdx.game.shit.Monolog;
import com.mygdx.game.shit.trigger.ClickTrigger;
import com.mygdx.game.shit.trigger.Trigger;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class LevelManager {

    private static LevelManager instance;

    public static LevelManager getInstance() {
        if (instance == null)
            instance = new LevelManager();
        return instance;
    }

    public Level currentLevel;
    private int currentStep;

    private LevelManager() {

    }

    public void startLevel(House house, String filepath) {
        this.currentLevel = LevelParser.parse(house, filepath);
        currentStep = 0;
    }

    public void startLevel(House house, String filepath, int step) {
        this.currentLevel = LevelParser.parse(house, filepath);
        currentStep = step;

    }


    private boolean validateAndExecute(Trigger trigger, LQuest lQuest) {
        if(trigger.getIfStep() == -1) {
            lQuest.executeTriggerListener(trigger.getId(), false);
            endQuest(lQuest.getId());
            return true;
        }
        return false;
    }
    public Trigger getTrigger(int id) {
        for (Trigger trigger : currentLevel.getTriggers()) {
            if(trigger.getId() == id)
                return trigger;
        }
        return null;
    }

    public void trigger(Trigger.Type type, Object ... params) {
        List<Trigger> triggers = currentLevel.getTriggers();


        for (Trigger trigger : triggers) {
            if(trigger.isEnabled() && trigger.getType() == type) {
                if (trigger.validate(params)) {
                    currentLevel.executeTriggerListener(trigger.getId(), false);
                    break;
                }
            }
        }

        Step step = currentLevel.getStep(currentStep);
        List<LQuest> lQuests = step.getlQuests();
        for (LQuest lQuest : lQuests) {
            if(lQuest.isVisible() && !lQuest.isComplete()) {
                for (Trigger trigger : lQuest.getTriggers()) {
                    if(trigger.isEnabled() && trigger.getType() == type) {
                        if (trigger.validate(params) && validateAndExecute(trigger, lQuest)) {
                            lQuest.setVisible(false);
                            lQuest.setComplete(true);
                            break;
                        }
                    }
                }
            }
        }


    }

    public LQuest getQuest(String name) {
        return currentLevel.getQuest(name);
    }

    public void doorClick(Door door, int x, int y) {
        System.out.println("LevelManager[doorClick] DoorId = " + door.getId() + " x = " + x + " y = " + y + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.CLICK, door.getId(), x, y, ClickTrigger.SubjectType.DOOR);
    }

    public void catchMe() {
        System.out.println("LevelManager[catchMe]" + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.CATCH);
    }

    public void roomSubjectClick(RoomSubject roomSubject, int x, int y) {
        System.out.println("LevelManager[roomSubjectClick] roomSubjectId = " + roomSubject.getId() + " x = " + x + " y = " + y + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.CLICK, roomSubject.getId(), x, y, ClickTrigger.SubjectType.SUBJECT);
    }

    public void talkDialogGroup(Dialog.DialogGroup group) {
        System.out.println("LevelManager[talkDialogGroup] groupId = " + group.getId() + " dialogId = " + group.getDialog().getId() + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.TALK, group.getDialog().getId(), group.getId());
    }

    public void findSubject(Subject subject) {
        System.out.println("LevelManager[findSubject] subjectId = " + subject.getId() + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.FIND, subject.getId());
    }

    public void searchRoomSubject(RoomSubject subject) {
        System.out.println("LevelManager[searchRoomSubject] subjectId = " + subject.getId() + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.SEARCH, subject.getId());
    }

    public void hideRoomSubject(RoomSubject subject) {
        System.out.println("LevelManager[hideRoomSubject] subjectId = " + subject.getId() + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.HIDE, subject.getId());
    }

    public void comeInRoom(Room room) {
        System.out.println("LevelManager[comeInRoom] roomId = " + room.getId() + "; STEP = " + currentLevel.getStep(currentStep));
        trigger(Trigger.Type.COME_IN, room.getId());
    }

    public void endQuests() {
        for (LQuest lQuest : currentLevel.getStep(currentStep).getlQuests()) {
            if(lQuest.isVisible() && !lQuest.isComplete()) {
                lQuest.setComplete(true);
                lQuest.setVisible(false);
                lQuest.executeOnEnd(false);
            }
        }
    }

    public LQuest findByName(String name) {
            for (LQuest lQuest : currentLevel.getStep(currentStep).getlQuests()) {
                if(lQuest.getName().equals(name.substring(0, name.length() - 4))) {
//                    lQuest.executeOnStart(false);
                    return lQuest;

                }
            }
            return null;
        }


    public LQuest findById(int questId) {
        for (Step step : currentLevel.getsteps()){
        for (LQuest lQuest : currentLevel.getStep(step.getNumber()).getlQuests()) {
            if(lQuest.getId() == questId) {
//               lQuest.executeOnStart(false);
                return lQuest;
            }
        }}
        return null;
    }

    public void startQuest(String name) {
        LQuest byName = findByName(name);
        if((byName != null) && (byName.isVisible())) {
            byName.setComplete(false);
            byName.executeOnStart(false);
            return;
        }
        //Если мы ничего не нашли значит мы начинаем новый шаг
//        incrementStep();
    }

    public void startQuest(int questId) {
        LQuest byId = findById(questId);
        if (!byId.isVisible()){
        if(!byId.isComplete()) {
            byId.executeOnStart(false);
        }}
    }

    public void incrementStep() {
        endQuests();
        SaveWriter.writeSave(currentLevel.getHouse());
        currentStep++;
        MusicManager.playSound("songs/save.wav", false);
        for (LQuest lQuest : currentLevel.getStep(currentStep).getlQuests()) {
            if(lQuest.isVisible() && !lQuest.isComplete()) {
                startQuest(lQuest.getName());
                lQuest.executeOnStart(false);
            }
        }
    }

    public void tryincrementStep() {
        int k = 0;
        for (LQuest lQuest : currentLevel.getStep(currentStep).getlQuests()) {
            k = k + 1;
            if (!lQuest.isVisible()) {
                k = k-1;
            }
        }
        if (k == 1) {
            incrementStep();
        }
    }

    public void endQuest(int questId) {
        for (LQuest lQuest : currentLevel.getStep(currentStep).getlQuests()) {
            if(lQuest.getId() == questId && lQuest.isVisible() &&!lQuest.isComplete()) {
                lQuest.executeOnEnd(false);
                lQuest.setVisible(false);
                lQuest.setComplete(true);
                break;
            }
        }
    }

    public void changeDialog(int ghost, int newDialog) {
        currentLevel.changeDialog(ghost, newDialog);
    }

    public Monolog getMonolog(int id) {
        return currentLevel.getMonolog(id);
    }


    public Dialog getDialogByGhost(int id) {
        return currentLevel.getDialogByGhost(id);
    }

    public void endLevel(House house) {
        currentStep = 0;
        currentLevel = null;
        house.myGame.gameScreen.dispose();
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public String getLevelName() {
        return currentLevel.getName();
    }

    public List<Trigger> getTriggers() {
        return currentLevel.getTriggers();
    }

    public List<LQuest> getQuests() {
        return currentLevel.getQuests();
    }

    public void onStart() {
        for (LQuest lQuest : currentLevel.getStep(currentStep).getlQuests()) {
            if(lQuest.isVisible())
                lQuest.executeOnStart(false);
        }
    }
}
