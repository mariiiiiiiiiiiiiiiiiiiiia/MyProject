package com.mygdx.game.utils;

import com.mygdx.game.Const;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Inventar;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.shit.Quest;
import com.mygdx.game.shit.QuestParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveParserLast {

    private float heroX;
    private float heroY;
    private List<Inventar> inventar;
    private List<Ghost> ghosts;
    private int step;
    private String name;

    //TODO parse
    public static void parse(House house, JSONObject object) {

        JSONObject currentQuest = object.optJSONObject("current_quest");
        int step = currentQuest.optInt("step");
        String name = currentQuest.optString("name");

        JSONArray ghostsJson = object.optJSONArray("ghosts");

        for (int i = 0; i < ghostsJson.length(); i++) {
            JSONObject gh = ghostsJson.getJSONObject(i);
            int ghostId = gh.optInt("id");
            float ghostX = (float) gh.optDouble("x");
            int room = gh.optInt("room");
            Ghost ghost = house.getGhost(ghostId);
            ghost.setX(ghostX * Const.W);
            ghost.setRoom(house.getRoom(room));
        }

        Quest quest = QuestParser.parse(house, "quests/" + name + ".xml");
        quest.setStep(step);
        Quest.setCurrentQuest(quest);
        quest.executeListenersForPreveousSteps(true);

        JSONObject hero = object.optJSONObject("hero");
        int currentRoom = hero.optInt("currentRoom");
        float heroX = (float) hero.optDouble("x");
        JSONArray inventarJson = hero.optJSONArray("inventar");
        int ids[] = new int[inventarJson.length()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = inventarJson.getInt(i);
        }
        Hero hero1 = house.getHero();
        hero1.setX(heroX * Const.W);
        for (int id : ids) {
            hero1.getInventar().add(house.findSubject(id));
        }
        house.setCurrentRoom(house.getRoom(currentRoom), null);

        JSONArray sub_change_logJson = object.optJSONArray("sub_change_log");
        for (int i = 0; i < sub_change_logJson.length(); i++) {
            JSONObject js = sub_change_logJson.optJSONObject(i);
            int roomId = js.optInt("room");
            int rs_id = js.optInt("rs_id");
            JSONArray array = js.optJSONArray("array");
            RoomSubject rs = house.getRoom(roomId).getRoomSubject(rs_id);

            List<Subject> subjects = new ArrayList<>();
            for (int j = 0; j < array.length(); j++) {
                subjects.add(house.findSubject(array.getInt(j)));
            }
            rs.clearAll();
            rs.setSubjects(subjects);
        }

    }
}
