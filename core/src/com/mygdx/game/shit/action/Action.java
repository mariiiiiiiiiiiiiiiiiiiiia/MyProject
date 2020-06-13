package com.mygdx.game.shit.action;

import com.mygdx.game.Const;
import com.mygdx.game.MusicManager;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.npc.cutscene.CutsceneManager;
import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Inventar;
import com.mygdx.game.obj.Room;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.Subject;
import com.mygdx.game.obj.panels.BottomPanel;
import com.mygdx.game.obj.panels.DialogPanel;
import com.mygdx.game.obj.panels.MonologPanel;
import com.mygdx.game.shit.Monolog;
import com.mygdx.game.shit.level.LQuest;
import com.mygdx.game.shit.level.LevelManager;
import com.mygdx.game.shit.trigger.Trigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class Action {

    private static Map<String, OnActionListener> listeners = new HashMap<String, OnActionListener>() {{
        put("change-dialog", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int ghost = Integer.parseInt(params.get("ghost"));
                int newDialog = Integer.parseInt(params.get("new_dialog"));
                LevelManager.getInstance().changeDialog(ghost, newDialog);
            }
        });
        put("start-dialog", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int dialog = Integer.parseInt(params.get("id"));
                int ghost = Integer.parseInt(params.get("ghost"));
//                Quest.getCurrentQuest().changeDialog(ghost, dialog);
                LevelManager.getInstance().changeDialog(ghost, dialog);
                DialogPanel.getInstance().startDialog(house.getGhost(ghost));
            }
        });
        put("set-visible", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String type = params.get("type");
                if (type.equals("subject")) {
                    int id = Integer.parseInt(params.get("id"));
                    Subject subject = house.findSubject(id);
                    if (subject == null)
                        throw new IllegalArgumentException("Subject with id = " + id + " not found");

                    boolean visible = Boolean.parseBoolean(params.get("value"));
                    subject.setVisible(visible);
                } else if (type.equals("door")) {
                    String id = params.get("id");
                    Door door = house.getDoor(id);
                    if (door == null)
                        throw new IllegalArgumentException("Door with id = " + id + " not found");
                    boolean visible = Boolean.parseBoolean(params.get("value"));
                    door.setVisible(visible);
                }
            }
        });
        put("open-door", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String doorId = params.get("id");
                house.getDoor(doorId).setClosed(false);
            }
        });
        put("close-door", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String doorId = params.get("id");
                house.getDoor(doorId).setClosed(true);
            }
        });
        put("sub-remove", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int id = Integer.parseInt(params.get("id"));
                Inventar inventar = house.getHero().getInventar();
                Set<Subject> set = inventar.getSubjects();
                Object[] array = set.toArray();
                for (Object obj : array) {
                    if (((Subject) obj).getId() == id) {
                        set.remove(obj);
                    }
                }
            }
        });
        put("set-special-description", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String type = params.get("room_subject_type");
                String text = params.get("text");
                if (text.equals("NULL"))
                    text = null;
                switch (type) {
                    case "DOOR":
                        String id = params.get("sid");
                        house.getDoor(id).setSpecialDescription(text);
                        break;
                    case "ROOM_SUBJECT":
                        int rsId = Integer.parseInt(params.get("sid"));
                        int rid = Integer.parseInt(params.get("room"));
                        house.getRoom(rid).getRoomSubject(rsId).setSpecialDescription(text);
                        break;
                }
            }
        });
        put("teleport-ghost", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int ghostId = Integer.parseInt(params.get("ghost"));
                int roomId = Integer.parseInt(params.get("room"));
                float x = Float.parseFloat(params.get("x"));
                Room room = house.getRoom(roomId);
                Ghost ghost = house.getGhost(ghostId);
                ghost.setRoom(room);
                ghost.setX(Const.W * x);
            }
        });
        put("start-monolog", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int id = Integer.parseInt(params.get("id"));
//                Monolog monolog = Quest.getCurrentQuest().getMonolog(id);
                Monolog monolog = LevelManager.getInstance().getMonolog(id);
                MonologPanel.getInstance().startMonolog(house, monolog);
            }
        });
        put("turn-trigger", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                boolean enabled = Boolean.parseBoolean(params.get("enabled"));
                int id = Integer.parseInt(params.get("tid"));
//                Trigger trigger = Quest.getCurrentQuest().getTrigger(id);
                Trigger trigger = LevelManager.getInstance().getTrigger(id);
//                Trigger trigger = LevelManager.getInstance().getTrigger(id);
                trigger.setEnabled(enabled);
            }
        });

        put("set-room-subject-visibility", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int id = Integer.parseInt(params.get("rsid"));
                int room = Integer.parseInt(params.get("room"));
                boolean visible = Boolean.parseBoolean(params.get("visible"));
                house.getRoom(room).getRoomSubject(id).setVisible(visible);
            }
        });
        put("add-type", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int id = Integer.parseInt(params.get("rsid"));
                int room = Integer.parseInt(params.get("room"));
                RoomSubject.Type type = RoomSubject.Type.convert(params.get("type"));
                house.getRoom(room).getRoomSubject(id).addType(type);
            }
        });
        put("remove-type", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int id = Integer.parseInt(params.get("rsid"));
                int room = Integer.parseInt(params.get("room"));
                RoomSubject.Type type = RoomSubject.Type.convert(params.get("type"));
                house.getRoom(room).getRoomSubject(id).removeType(type);
            }
        });
        put("set-search-description", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int rsId = Integer.parseInt(params.get("rsid"));
                int rid = Integer.parseInt(params.get("room"));
                String text = params.get("text");
                if (text.equals("NULL"))
                    text = null;
                house.getRoom(rid).getRoomSubject(rsId).setSpecialSearchDescription(text);
            }
        });
        put("change-start-point", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int ghostId = Integer.parseInt(params.get("ghost"));
                int rid = Integer.parseInt(params.get("room"));
                float x = Float.parseFloat(params.get("x"));
                house.getGhost(ghostId).changeStartPoint(rid, x);
            }
        });
        put("change-background-sound", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String file = params.get("file");
                MusicManager.playMusic(file, true);
            }
        });
        put("change-background-roomsub", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String file = params.get("name");
                int rid = Integer.parseInt(params.get("room"));
                int rsId = Integer.parseInt(params.get("rsid"));
                house.getRoom(rid).getRoomSubject(rsId).setTexture(file);

            }
        });
        put("change-another-sound", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String file = params.get("file");
                MusicManager.playFonMusic(file, true);
            }
        });
        put("stop-another-sound", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                MusicManager.stopFonMusic();
            }
        });
        put("resume-main-music", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                MusicManager.resumeMusic();
            }
        });
        put("sub-add", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int id = Integer.parseInt(params.get("id"));
                Subject sub = house.findSubject(id);
                Inventar inventar = house.getHero().getInventar();
                Set<Subject> set = inventar.getSubjects();
                boolean find = false;
                for (Subject subject : set) {
                    if(subject.getId() == id) {
                        find = true;
                        break;
                    }
                }
                if(!find)
                    inventar.add(sub);
            }
        });
        put("change-main-music", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String file = params.get("file");
                MusicManager.playMusic(file, true);
            }
        });
        put("pause-main-music", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                MusicManager.pauseMusic();
            }
        });
        put("start-cut-scene", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                BottomPanel.getInstance().setPanelVisible(false);
                int sceneId = Integer.parseInt(params.get("sceneId"));
                CutsceneManager.getInstance().startScene(sceneId);
//                Quest.getCurrentQuest().startScene(sceneId);
            }
        });
        put("start-quest", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                String name = params.get("name");
                LevelManager.getInstance().startQuest(name);
            }
        });
        put("set-quest-visibility", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                int questId = Integer.parseInt(params.get("quest_id"));
                boolean visibility = Boolean.parseBoolean(params.get("visibility"));
                LQuest byName = LevelManager.getInstance().findById(questId);
                if(!visibility)
                    byName.setVisible(false);
                else
                    byName.setVisible(true);

            }
        });
        put("try-increment-step", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                LevelManager.getInstance().tryincrementStep();
            }
        });
        put("turn-girl", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                house.getHero().turn(true);
            }
        });
        put("end-level", new OnActionListener() {
            @Override
            public void doAction(House house, Map<String, String> params) {
                house.endGame();
            }
        });

    }};

    public static void doAction(String actionName, House house, Map<String, String> params) {
        if(LevelManager.getInstance().currentLevel != null)
            listeners.get(actionName).doAction(house, params);
    }

    private interface OnActionListener {
        void doAction(House house, Map<String, String> params);
    }
}
