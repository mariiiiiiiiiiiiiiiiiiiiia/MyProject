package com.mygdx.game.shit;

import java.util.List;



public class Monolog {

    private int id;
    private List<String> text;
    private int currentIndex;

    public Monolog(int id, List<String> text) {
        this.id = id;
        this.text = text;
        currentIndex = 0;
    }

    public int getId() {
        return id;
    }

    public boolean hasNextText() {
        return currentIndex + 1 != text.size();
    }

    public void setFirstIndex() {
        currentIndex = 0;
    }

    public String nextText() {
        if(currentIndex == text.size())
            return null;
        return text.get(currentIndex++);
    }
}
