package com.mygdx.game.shit;

import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.Room;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.shit.trigger.ClickTrigger;
import com.mygdx.game.shit.trigger.Trigger;


public class BigListener {

    private static BigListener instance;

    private BigListener() {

    }

    public static BigListener getInstance() {
        if (instance == null)
            instance = new BigListener();
        return instance;
    }

    private boolean validateAndExecute(Trigger trigger, Quest quest) {
        if(trigger.getIfStep() == -1) {
            quest.executeTriggerListener(trigger.getId(), false);
            return true;
        } else if(trigger.getIfStep() == quest.getStep()) {
            quest.incStep();
            System.out.println("step = " + quest.getStep());
            return true;
        }
        return false;
    }

    public void trigger(Trigger.Type type, Object ... params) {
    }

    public void doorClick(Door door, int x, int y) {
        System.out.println("BigListener[doorClick] DoorId = " + door.getId() + " x = " + x + " y = " + y);
        trigger(Trigger.Type.CLICK, door.getId(), x, y, ClickTrigger.SubjectType.DOOR);
    }

    public void catchMe() {
        System.out.println("BigListener[catchMe]");
        trigger(Trigger.Type.CATCH);
    }

    public void roomSubjectClick(RoomSubject roomSubject, int x, int y) {
        System.out.println("BigListener[roomSubjectClick] roomSubjectId = " + roomSubject.getId() + " x = " + x + " y = " + y);
        trigger(Trigger.Type.CLICK, roomSubject.getId(), x, y, ClickTrigger.SubjectType.SUBJECT);
    }

    public void talkDialogGroup(Dialog.DialogGroup group) {
        System.out.println("BigListener[talkDialogGroup] groupId = " + group.getId() + " dialogId = " + group.getDialog().getId());
        trigger(Trigger.Type.TALK, group.getDialog().getId(), group.getId());
    }

    public void findSubject(Subject subject) {
        System.out.println("BigListener[findSubject] subjectId = " + subject.getId());
        trigger(Trigger.Type.FIND, subject.getId());
    }

    public void searchRoomSubject(RoomSubject subject) {
        System.out.println("BigListener[searchRoomSubject] subjectId = " + subject.getId());
        trigger(Trigger.Type.SEARCH, subject.getId());
    }

    public void hideRoomSubject(RoomSubject subject) {
        System.out.println("BigListener[hideRoomSubject] subjectId = " + subject.getId());
        trigger(Trigger.Type.HIDE, subject.getId());
    }

    public void comeInRoom(Room room) {
        System.out.println("BigListener[comeInRoom] roomId = " + room.getId());
        trigger(Trigger.Type.COME_IN, room.getId());
    }
}
