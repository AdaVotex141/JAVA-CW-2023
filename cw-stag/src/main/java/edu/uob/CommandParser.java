package edu.uob;

//import edu.uob.Command.GameAction;
import edu.uob.Command.*;
import edu.uob.Entity.*;
import edu.uob.Entity.Character;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashSet;

//TODO can't get shovel after the elf produced it -> can't detect the produced new product ????
//TODO the 'cut down' and 'cut'
/*
Kate:> bridge with log
you bridge the river with the log and can now reach the other side
Kate:> look
location:forest(a deep dark forest)
forest->null
forest->clearing
* */

public class CommandParser {
    //find trigger->
    ArrayList<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    GameAction gameAction;
    Player player;
    public HashSet<String> builtinAction;

    public CommandParser(EntityParser entityParser, ActionParser actionParser, Player player) {
        commands = new ArrayList<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        builtinAction = new HashSet<>();
        builtinAction.add("look");
        builtinAction.add("inv");
        builtinAction.add("inventory");
        builtinAction.add("get");
        builtinAction.add("goto");
        builtinAction.add("drop");
        builtinAction.add("health");
    }

    public StringBuilder commandParse(String command) {
        commands.clear();

        String[] words = command.split(" ");
        for (String word : words) {
            commands.add(word.toLowerCase());
        }

        //player->
        if(!entityParser.playerMap.containsKey(words[0])){
            this.player = new Player(this.entityParser,words[0],"a player");
            entityParser.playerMap.put(this.player.getName(),this.player);
        }else{
            this.player = entityParser.playerMap.get(words[0]);
        }

        StringBuilder result = new StringBuilder();
        HashSet<String> triggerWords = findTrigger();
        String trigger = triggerParser(triggerWords);

        if (triggerWords.isEmpty() || trigger.equals("")){
            result.append("Can't resolve commands");
            return result;
        }

        if(this.builtinAction.contains(trigger)){
            BuiltInIntep intep = new BuiltInIntep(commands,entityParser,player,builtinAction);
            intep.builtInIntepreter(trigger,result);

        }else if(actionParser.actions.containsKey(trigger)){
            this.gameAction = actionParser.actions.get(trigger);
            HashSet<String> entities = new HashSet<>();
            for(String word: commands){
                if(gameAction.subjects.contains(word)){
                    entities.add(word);
                }
            }
            if(entities.size() == 2 || entities.size() == 1){
                ActionIntep intep = new ActionIntep(commands,actionParser,player,gameAction,entityParser);
                intep.actionFileIntepreter(trigger,result,entities);
                //actionFileIntepreter(trigger,result,entities);
            }else{
                result.append("[WARNING] no or multiple entities detected");
            }
        }else{
            result.append("[WARNING] no triggerword");
        }
        return result;
    }

    private HashSet<String>  findTrigger() {
        HashSet<String> triggerWord = new HashSet<>();
        for (String word : this.commands) {
            if (this.builtinAction.contains(word)) {
                triggerWord.add(word);
            }else if (actionParser.actions.containsKey(word)) {
                triggerWord.add(word);
            }
        }
        return triggerWord;
    }

    private String triggerParser(HashSet<String> triggerWords){
        String trigger = "";
        int count = 0;
        if(triggerWords.size()>1) {
            for (String triggerWord : triggerWords) {
                if (actionParser.actions.containsKey(triggerWord)) {
                    //entity check?
                    for(String commandEntityCheck:commands){
                        if(actionParser.actions.get(triggerWord).subjects.contains(commandEntityCheck)){
                            trigger = triggerWord;
                            count+=1;
                        }
                    }
                } else if (builtinAction.contains(triggerWord)) {
                    trigger = triggerWord;
                    count+=1;
                }
            }
        }else if(triggerWords.size() == 1){
            String[] triggerArray = triggerWords.toArray(new String[0]);
            trigger = triggerArray[0];
        }
        if(count > 1){
            trigger = "";
        }
        return trigger;
    }
}
