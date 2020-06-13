package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.Const;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.shit.Quest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;



public class SaveUtils {

    public static void save(House house) {
        JSONObject save = new JSONObject();
        JSONObject hero = new JSONObject();
        Hero houseHero = house.getHero();
        hero.put("x", houseHero.getX() / Const.W);
        hero.put("currentRoom", house.getCurrentRoom().getId());
        JSONArray inv = new JSONArray();
        for (Subject subject : houseHero.getInventar().getSubjects()) {
            inv.put(subject.getId());
        }
        hero.put("inventar", inv);
        JSONArray ghostJson = new JSONArray();
        List<Ghost> ghosts = house.getGhosts();
        for (Ghost ghost : ghosts) {
            JSONObject gg = new JSONObject();
            gg.put("id", ghost.getId());
            gg.put("x", ghost.getX() / Const.W);
            gg.put("room", ghost.getRoom().getId());
            ghostJson.put(gg);
        }
        JSONObject quest = new JSONObject();
        quest.put("name", Quest.getCurrentQuest().getName());
        quest.put("step", Quest.getCurrentQuest().getStep());

        JSONArray subChangeLog = new JSONArray();
        for (House.ChangeLogItem item : house.getItems()) {
            JSONObject cl = new JSONObject();
            cl.put("room", item.roomId);
            cl.put("rs_id", item.rsId);
            JSONArray array = new JSONArray();
            for (Integer i : item.list) {
                array.put(i);
            }
            cl.put("array", array);
            subChangeLog.put(cl);
        }
        save.put("hero", hero);
        save.put("ghosts", ghostJson);
        save.put("current_quest", quest);
        save.put("sub_change_log", subChangeLog);

        FileHandle handle = Gdx.files.local("walker/save.json");
        if (handle.exists())
            handle.delete();
        handle.writeString(save.toString(), false);
    }
}
