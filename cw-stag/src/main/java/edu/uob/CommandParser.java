package edu.uob;

import edu.uob.Command.GameAction;

import java.util.ArrayList;
import java.util.HashSet;

public class CommandParser {
    //find trigger->
    ArrayList<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    GameAction gameAction;
    public CommandParser(EntityParser entityParser, ActionParser actionParser, GameAction gameAction){
        commands = new ArrayList<String>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        this.gameAction = gameAction;
    }
    private boolean commandParser(String command){
        String[] words = command.split(" ");
        for (String word:words){
            commands.add(word);
        }
        String trigger = findTrigger();

        return true;
    }
    private String findTrigger(){
        int count = 0;
        String triggerWord = "";
        for(String word : this.commands){
            if(gameAction.builtinAction.contains(word)){
                count+=1;
                triggerWord = word;
            }
            if(actionParser.actions.containsKey(word)){
                count+=1;
                triggerWord = word;
            }
        }
        if(count!=1){
            triggerWord = "Multiple Command or no Trigger Command";
        }
        return triggerWord;
    }


}
