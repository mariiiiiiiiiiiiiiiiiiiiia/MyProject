package com.mygdx.game.npc.cutscene;

import com.mygdx.game.Const;
import com.mygdx.game.MyAnimation;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.panels.DialogPanel;
import com.mygdx.game.shit.level.LevelManager;

import java.util.HashMap;
import java.util.Map;


public class Task {

    private String type;
    private Map<String, String> params;
    private boolean complete;

    public Task(int id, String type, Map<String, String> params) {
        int id1 = id;
        this.type = type;
        this.params = params;
    }


    public void update(House house, float dt) {
        listenerMap.get(type).taskTick(dt, house, this, params);
    }

    public void dispose() {

    }


    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public interface OnSceneTaskTickListener {
        void taskTick(float delta, House house, Task task, Map<String, String> params);
    }

    public static Map<String, OnSceneTaskTickListener> listenerMap = new HashMap<String, OnSceneTaskTickListener>() {{
        put("teleport", new OnSceneTaskTickListener() {
            @Override
            public void taskTick(float delta, House house, Task task, Map<String, String> params) {
                int ghost = Integer.parseInt(params.get("ghost"));
                int roomId = Integer.parseInt(params.get("room"));
                int x = Integer.parseInt(params.get("x"));
                Ghost g = house.getGhost(ghost);
                g.setRoom(house.getRoom(roomId));
                g.setX(x * Const.W);
                g.turnToHero(house.getHero());

                task.complete = true;
            }
        });
        put("go", new OnSceneTaskTickListener() {
            @Override
            public void taskTick(float delta, House house, final Task task, Map<String, String> params) {
                String who = params.get("who");
                int x = Integer.parseInt(params.get("x"));
                if(who.equals("GHOST")) {
                    int ghost = Integer.parseInt(params.get("ghost"));
                    Ghost g = house.getGhost(ghost);
                    if(!g.getState().equals(Ghost.State.WALK)) {
                        g.go(x * Const.W, new MyAnimation.OnAnimationEndListener()
                        {
                            @Override
                            public void onAnimationEnd() {
                                task.complete = true;
                            }
                        });
                    }
                }else {
                    Hero h = house.getHero();
                    h.go(x * Const.W, new MyAnimation.OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            task.complete = true;
                        }
                    });
                }
            }
        });
        put("talk", new OnSceneTaskTickListener() {
            @Override
            public void taskTick(float delta, House house, final Task task, Map<String, String> params) {
                int ghost = Integer.parseInt(params.get("ghost"));
                int dialog = Integer.parseInt(params.get("dialog"));

                Ghost g = house.getGhost(ghost);
                if(!g.getState().equals(Ghost.State.TALK)) {
                    LevelManager.getInstance().changeDialog(g.getId(), dialog);
                    DialogPanel.getInstance().startDialog(g, new DialogPanel.OnEndDialogListener() {
                        @Override
                        public void oEndDialog() {
                            task.complete = true;
                        }
                    });
                }
            }
        });
        put("comeIn", new OnSceneTaskTickListener() {
            @Override
            public void taskTick(float delta, House house, Task task, Map<String, String> params) {
                int roomId = Integer.parseInt(params.get("room"));
                int x = Integer.parseInt(params.get("x"));
                Hero h = house.getHero();
                house.setCurrentRoom(house.getRoom(roomId), null);
                h.setX(x * Const.W);
                task.complete = true;
            }
        });
        put("hide", new OnSceneTaskTickListener() {
            @Override
            public void taskTick(float delta, House house, Task task, Map<String, String> params) {
                Hero h = house.getHero();
                h.setVisible(false);
                task.complete = true;
            }
        });
        put("setVis", new OnSceneTaskTickListener() {
            @Override
            public void taskTick(float delta, House house, Task task, Map<String, String> params) {
                Hero h = house.getHero();
                h.setVisible(true);
                task.complete = true;
            }
        });

    }};
}
