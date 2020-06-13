package com.mygdx.game.shit.level;

import java.util.List;


public class Step {

    private int number;
    private List<LQuest> lQuests;

    public Step(int number, List<LQuest> lQuests) {
        this.number = number;
        this.lQuests = lQuests;
    }

    @Override
    public String toString() {
        return "Step{" +
                "number=" + number +
                '}';
    }

    public List<LQuest> getlQuests() {
        return lQuests;
    }
    public int getNumber() {
        return number;
    }
}
