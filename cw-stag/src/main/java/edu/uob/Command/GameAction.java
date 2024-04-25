package edu.uob.Command;

import java.util.HashSet;

public class GameAction{
    protected HashSet<String> builtinAction;
    protected HashSet<String> subjects;
    protected HashSet<String> consumed;
    protected HashSet<String> produced;
    protected String narration;

    public GameAction(){
        subjects = new HashSet<>();
        consumed = new HashSet<>();
        produced = new HashSet<>();
        builtinAction = new HashSet<>();
        builtinAction.add("look");
        builtinAction.add("inv");
        builtinAction.add("inventory");
        builtinAction.add("get");
        builtinAction.add("goto");
        builtinAction.add("drop");
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