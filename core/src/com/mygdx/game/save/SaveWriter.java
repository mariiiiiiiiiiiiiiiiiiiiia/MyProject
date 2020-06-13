package com.mygdx.game.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Const;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Room;
import com.mygdx.game.shit.Dialog;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.shit.level.LQuest;
import com.mygdx.game.shit.level.LevelManager;
import com.mygdx.game.shit.trigger.Trigger;

import org.json.JSONArray;
import org.json.JSONObject;


public class SaveWriter {

    public static void writeSave(House house) {
        JSONObject json = new JSONObject();
        JSONObject hero = new JSONObject();
        hero.put("x", house.getHero().getX() / Const.W);
        hero.put("room", house.getCurrentRoom().getId());
        JSONArray inv = new JSONArray();
        for (Subject subject : house.getHero().getInventar().getSubjects()) {
            inv.put(subject.getId());
        }
        hero.put("inv", inv);
        json.put("hero", hero);

        JSONArray ghosts = new JSONArray();
        for (Ghost ghost : house.getGhosts()) {
            JSONObject gjs = new JSONObject();
            gjs.put("id", ghost.getId());
            gjs.put("x", ghost.getX() / Const.W);
            gjs.put("room", ghost.getRoom().getId());
            Dialog dialogByGhost = LevelManager.getInstance().getDialogByGhost(ghost.getId());
            if(dialogByGhost != null)
                gjs.put("current_dialog", dialogByGhost.getId());
            ghosts.put(gjs);
        }
        json.put("ghosts", ghosts);

        JSONArray doors = new JSONArray();
        for (Door door : house.getDoors()) {
            JSONObject oneDoor = new JSONObject();
            oneDoor.put("id", door.getId());
            if(door.getSpecialDescription() != null)
                oneDoor.put("specialDescription", door.getSpecialDescription());
            oneDoor.put("visible", door.isVisible());
            oneDoor.put("closed", door.isClosed());
            doors.put(oneDoor);
        }
        json.put("doors", doors);

        JSONObject quest = new JSONObject();
        int step = LevelManager.getInstance().getCurrentStep() + 1;
        quest.put("step", step);
        quest.put("level", "quests/"+LevelManager.getInstance().getLevelName());
        JSONArray global_triggers = new JSONArray();
        for (Trigger trigger : LevelManager.getInstance().getTriggers()) {
            JSONObject t = new JSONObject();
            t.put("tid", trigger.getId());
            t.put("enabled", trigger.isEnabled());
            global_triggers.put(t);
        }
        quest.put("global_triggers", global_triggers);

        JSONArray quest_visibility = new JSONArray();
        for (LQuest lQuest : LevelManager.getInstance().getQuests()) {
            JSONObject lq = new JSONObject();
            lq.put("name", lQuest.getName());
            lq.put("visible", lQuest.isVisible());
            quest_visibility.put(lq);
        }
        quest.put("quest_visibility", quest_visibility);
        json.put("quest", quest);

        JSONArray subjects = new JSONArray();
        JSONArray roomSubjectJson = new JSONArray();

        for (Room room : house.getRooms()) {
            for (RoomSubject roomSubject : room.roomSubjects) {
                System.out.println(roomSubject);
                if(roomSubject.getSubject() != null) {
                    for (Subject subject : roomSubject.getSubject()) {
                        JSONObject sub = new JSONObject();
                        sub.put("id", subject.getId());
                        sub.put("rs_id", roomSubject.getId());
                        sub.put("visible", subject.isVisible());
                        subjects.put(sub);
                    }
                }
                JSONObject rsJs = new JSONObject();
                rsJs.put("id", roomSubject.getId());
                rsJs.put("visible", roomSubject.isVisible());
                JSONArray types = new JSONArray();
                for (RoomSubject.Type type : roomSubject.getTypes()) {
                    types.put(type.toString().toLowerCase());
                }
                rsJs.put("types", types);
                if(roomSubject.getRegionName() != null)
                    rsJs.put("textures", roomSubject.getRegionName());
                rsJs.put("specialDescription", roomSubject.getSpecialDescription());
                rsJs.put("specialSearchDescription", roomSubject.getSpecialSearchDescription());
                roomSubjectJson.put(rsJs);

            }
        }
        json.put("subjects", subjects);
        json.put("room_subjects", roomSubjectJson);

        FileHandle handle = Gdx.files.local("walker/save.json");
        if (handle.exists())
            handle.delete();
        handle.writeString(json.toString(), false);
    }
}
