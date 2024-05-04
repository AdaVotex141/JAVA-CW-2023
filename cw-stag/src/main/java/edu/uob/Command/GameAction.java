package edu.uob.Command;

import java.util.HashSet;

public class GameAction{
    public HashSet<String> subjects;
    public HashSet<String> consumed;
    public HashSet<String> produced;
    public String narration;

    public GameAction(){
        subjects = new HashSet<>();
        consumed = new HashSet<>();
        produced = new HashSet<>();
        narration = null;
    }

    public void setSubjects(String name){
        this.subjects.add(name);
    }

    public void setConsumed(String name){
        this.consumed.add(name);
    }

    public void setProduced(String name){
        this.produced.add(name);
    }

    public void setNarration(String name){
        this.narration=name;
    }



}