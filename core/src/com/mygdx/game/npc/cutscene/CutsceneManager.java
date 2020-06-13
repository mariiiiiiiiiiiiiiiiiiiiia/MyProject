package com.mygdx.game.npc.cutscene;

import com.mygdx.game.obj.House;

import java.util.List;


public class CutsceneManager {

    private static CutsceneManager instance;

    private CutsceneManager() {
    }

    public static CutsceneManager getInstance() {
        if(instance == null)
            instance = new CutsceneManager();
        return instance;
    }


    private List<CutScene> cutScenes;
    private CutScene currentScene;
    private House house;

    public void init(List<CutScene> cutScenes, House house) {
        this.cutScenes = cutScenes;
        this.house = house;
        currentScene = null;
    }

    public void startScene(int id) {
        for (CutScene cutScene : cutScenes) {
            if(cutScene.getId() == id) {
                currentScene = cutScene;
                currentScene.start();
//                currentScene.setPlaying(true);
                break;
            }
        }
    }

    public void update(float dt) {
        if(currentScene != null) {
            if(currentScene.isPlaying()) {
                currentScene.update(house, dt);
            }
        }
    }

    public boolean isSomeCutscenePlaying() {
        return currentScene != null && currentScene.isPlaying();
    }
}

