package com.mygdx.game.save;

import com.mygdx.game.Const;
import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.shit.level.LevelManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class SaveParser {

    public static void parse(House house, JSONObject json) {
        JSONObject qJson = json.optJSONObject("quest");
        int step = qJson.optInt("step");
        String level = qJson.optString("level");
        LevelManager.getInstance().startLevel(house, level, step);

        JSONArray quest_visibility = qJson.optJSONArray("quest_visibility");
        for (int i = 0; i < quest_visibility.length(); i++) {
            JSONObject qv = quest_visibility.getJSONObject(i);
            String name = qv.optString("name");
            boolean visible = qv.optBoolean("visible");
            LevelManager.getInstance().getQuest(name).setVisible(visible);
        }

        JSONArray global_triggers = qJson.optJSONArray("global_triggers");
        for (int i = 0; i < global_triggers.length(); i++) {
            JSONObject trigger = global_triggers.optJSONObject(i);
            int tid = trigger.optInt("tid");
            boolean enabled = trigger.optBoolean("enabled");
            LevelManager.getInstance().getTrigger(tid).setEnabled(enabled);
        }

        JSONArray ghosts = json.optJSONArray("ghosts");
        for (int i = 0; i < ghosts.length(); i++) {
            JSONObject g = ghosts.optJSONObject(i);
            int id = g.optInt("id");
            double x = g.optDouble("x");
            int r = g.optInt("room");
            house.getGhost(id).setX((float) (x * Const.W));
            house.getGhost(id).setRoom(house.getRoom(r));
            if(!g.isNull("current_dialog")) {
                int current_dialog = g.optInt("current_dialog");
                LevelManager.getInstance().changeDialog(id, current_dialog);
            }
        }

        JSONObject hero = json.optJSONObject("hero");
        double hero_x = hero.optDouble("x");
        int room = hero.optInt("room");
        JSONArray inv = hero.optJSONArray("inv");
        int s_ids[] = new int[inv.length()];
        for (int i = 0; i < s_ids.length; i++) {
            s_ids[i] = inv.getInt(i);
        }

        Hero hh = house.getHero();
        hh.setX((float) (hero_x * Const.W));
        house.removeFromSubjectToInventar(s_ids);
        house.setCurrentRoom(house.getRoom(room), null);


        JSONArray doorJson = json.optJSONArray("doors");
        for (int i = 0; i < doorJson.length(); i++) {
            JSONObject dr = doorJson.optJSONObject(i);
            String id = dr.optString("id");
            Door door = house.getDoor(id);
            boolean visible = dr.optBoolean("visible");
            boolean closed = dr.optBoolean("closed");
            if(!dr.isNull("specialDescription")) {
                String specialDescription = dr.optString("specialDescription");
                door.setSpecialDescription(specialDescription);
            }
            door.setVisible(visible);
            door.setClosed(closed);
        }

        JSONArray subjectsJson = json.optJSONArray("subjects");
        for (int i = 0; i < subjectsJson.length(); i++) {
            JSONObject sub = subjectsJson.optJSONObject(i);
            int id = sub.optInt("id");
            int rs_id = sub.optInt("rs_id");
            boolean visible = sub.optBoolean("visible");
            Subject subject = house.findSubject(id);
            if(subject != null) {
                subject.setVisible(visible);
                house.replaceSubject(subject, rs_id);
            }
        }

        JSONArray roomSubjectsJson = json.optJSONArray("room_subjects");
        for (int i = 0; i < roomSubjectsJson.length(); i++) {

            JSONObject rsub = roomSubjectsJson.optJSONObject(i);
            int id = rsub.optInt("id");
            RoomSubject rs = house.findRoomSubject(id);

            boolean visible = rsub.optBoolean("visible");
            if(!rsub.isNull("texture")) {
                String texture = rsub.optString("texture");
                rs.setTexture(texture);
            }

            JSONArray array = rsub.optJSONArray("types");
            rs.removeAllType();
            for (int j = 0; j < array.length(); j++) {
                RoomSubject.Type type = RoomSubject.Type.convert(array.optString(j));
                rs.addType(type);
            }
            rs.setVisible(visible);
        }

        LevelManager.getInstance().onStart();
    }
}
