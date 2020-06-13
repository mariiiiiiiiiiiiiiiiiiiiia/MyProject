package com.mygdx.game.obj;


import java.util.HashSet;
import java.util.Set;


public class Inventar {

    private Set<Subject> subjects;
    private int maxSize;

    public Inventar(Hero hero) {
        Hero hero1 = hero;
        subjects = new HashSet<>();
        maxSize = 6;
    }

    public boolean add(Subject subject) {
        if (maxSize == subjects.size())
            return false;
        subjects.add(subject);
        return true;
    }


    public Set<Subject> getSubjects() {
        return subjects;
    }
}
