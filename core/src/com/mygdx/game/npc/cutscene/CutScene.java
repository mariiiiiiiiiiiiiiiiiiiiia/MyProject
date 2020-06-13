package com.mygdx.game.npc.cutscene;

import com.mygdx.game.obj.House;

import java.util.List;


public class CutScene {

    private int id;
    private List<Task> tasks;
    private int currentTaskIndex;
    private boolean isPlaying;

    public CutScene(int id, List<Task> tasks) {
        this.id = id;
        this.tasks = tasks;
        currentTaskIndex = -1;
    }

    public void start() {
        currentTaskIndex = 0;
        isPlaying = true;
        for (Task task : tasks) {
            task.setComplete(false);
            task.dispose();
        }
    }

    public void update(House house, float dt) {
        if(currentTaskIndex != -1) {
            Task task = tasks.get(currentTaskIndex);
            if (task.isComplete()) {
                currentTaskIndex++;
                if (currentTaskIndex == tasks.size()) {
                    isPlaying = false;
                    currentTaskIndex = -1;
                }
            } else {
                task.update(house, dt);

            }
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getId() {
        return id;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}
