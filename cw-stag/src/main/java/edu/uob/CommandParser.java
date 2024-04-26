package edu.uob;

import edu.uob.Command.GameAction;
import edu.uob.Entity.Artefact;
import edu.uob.Entity.GameEntity;
import edu.uob.Entity.Player;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashSet;


public class CommandParser {
    //find trigger->
    HashSet<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    GameAction gameAction;
    Player player;
    public CommandParser(EntityParser entityParser, ActionParser actionParser, GameAction gameAction){
        commands = new HashSet<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        this.gameAction = gameAction;
        this.player = new Player(entityParser);
    }
    public StringBuilder commandParser(String command){
        String[] words = command.split(" ");
        for (String word:words){
            commands.add(word);
        }

        String trigger = findTrigger();
        StringBuilder result = new StringBuilder();

        if(trigger.isEmpty()){
            result.append("[WARNING] Can't resolve command.");
        }

        if(trigger.equals("look")){
            result = player.playerLook();

        }else if(trigger.equals("goto")){
            //TODO the key thing
            String toLocation = locationParse(commands);
            if(toLocation == null){
                result.append("[WARNING] invalid");
            }else{
                result = player.playerGoto(toLocation);
            }

        }else if(trigger.equals("get")){
            Artefact item = itemParse(commands);
            if(item == null){
                result.append("[WARNING]");
            }else{
                boolean getFlag = player.playerGet(item);
                if (getFlag){
                    result.append("");
                }else{
                    result.append("[WARNING] invalid, get fail");
                }
            }

        }else if(trigger.equals("drop")){
            Artefact item = itemParse(commands);
            if(item == null){
                result.append("[WARNING]");
            }else{
                boolean getFlag = player.playerDrop(item);
                if (getFlag){
                    result.append("");
                }else{
                    result.append("[WARNING] invalid, drop fail");
                }
            }
        }else if(trigger.equals("inv") || trigger.equals("inventory")){
            result = player.playerInv();
        }
        return result;
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

    private Artefact itemParse(HashSet<String> commands){
        Artefact item = null;
        if(commands.size() == 2){
            if(commands.contains("get")){
                commands.remove("get");
            }
            if(commands.contains("drop")){
                commands.remove("drop");
            }
            String itemName = getElementFromSingletonSet(commands);
            if (player.currentlocation.artefactsMap.containsKey(itemName)){
                item = player.currentlocation.artefactsMap.get(itemName);
            }
        }
        return item;
    }

    private String locationParse(HashSet<String> commands){
        String location = "";
        if(commands.size()==2){
            commands.remove("goto");
            location = getElementFromSingletonSet(commands);
        }else{
            System.err.print("[invalid sentence]");
        }
        return location;
    }

    private String getElementFromSingletonSet(HashSet<String> commands){
        return commands.size() == 1 ? commands.iterator().next() : null;
    }



}
