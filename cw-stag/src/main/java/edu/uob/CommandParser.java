package edu.uob;

import java.util.HashSet;

public class CommandParser {
    //find trigger->
    HashSet<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    public CommandParser(EntityParser entityParser, ActionParser actionParser){
        commands = new HashSet<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
    }
    private boolean commandParser(String command){
        String[] words = command.split(" ");
        for (String word:words){
            commands.add(word);
        }
        findTrigger(commands);

        return true;
    }
    private String findTrigger(HashSet<String> commands){

        return "";
    }


}
