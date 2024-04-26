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
    ArrayList<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    GameAction gameAction;
    Player player;

    public CommandParser(EntityParser entityParser, ActionParser actionParser, GameAction gameAction, Player player) {
        commands = new ArrayList<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        this.gameAction = gameAction;
        this.player = player;
    }

    public StringBuilder commandParse(String command) {
        commands.clear();

        String[] words = command.split(" ");
        for (String word : words) {
            commands.add(word);
        }

        String trigger = findTrigger();
        StringBuilder result = new StringBuilder();

        if (trigger.isEmpty()) {
            result.append("[WARNING] Can't resolve command.");
        }

        if(gameAction.builtinAction.contains(trigger)){
            builtInIntepreter(trigger,result);
        }else if(actionParser.actions.containsKey(trigger)){
            HashSet<String> entities = new HashSet<>();
            for(String word: commands){
                if(gameAction.subjects.contains(word)){
                    entities.add(word);
                }
            }
            if(entities.size() == 2 || entities.size() == 1){
                actionFileIntepreter(trigger,result,entities);
            }else{
                result.append("[WARNING] multiple entities");
            }
        }
        return result;
    }
    private StringBuilder builtInIntepreter(String trigger,StringBuilder result){
        if (trigger.equals("look")) {
            result.append(player.playerLook());

        } else if (trigger.equals("goto")) {
            //TODO the key thing
            String toLocation = locationParse(commands);
            if (toLocation == null) {
                result.append("[WARNING] invalid");
            } else {
                result.append(player.playerGoto(toLocation));
            }

        } else if (trigger.equals("get")) {
            Artefact item = itemParse(commands);
            if (item == null) {
                result.append("[WARNING]");
            } else {
                boolean getFlag = player.playerGet(item);
                if (getFlag) {
                    result.append("[GET SUCCESS]");
                } else {
                    result.append("[WARNING] invalid, get fail");
                }
            }

        } else if (trigger.equals("drop")) {
            Artefact item = itemParse(commands);
            if (item == null) {
                result.append("[WARNING]");
            } else {
                boolean getFlag = player.playerDrop(item);
                if (getFlag) {
                    result.append("");
                } else {
                    result.append("[WARNING] invalid, drop fail");
                }
            }

        } else if (trigger.equals("inv") || trigger.equals("inventory")) {
            result.append(player.playerInv());
        }
        return result;
    }

    private StringBuilder actionFileIntepreter(String trigger,StringBuilder result, HashSet<String> entities){
        GameAction gameAction = new GameAction();
        gameAction = actionParser.actions.get(trigger);





        return result;
    }


    private String findTrigger() {
        int count = 0;
        int countEntity = 0;
        String triggerWord = "";
        for (String word : this.commands) {
            if (gameAction.builtinAction.contains(word)) {
                count += 1;
                triggerWord = word;
            }
            if (actionParser.actions.containsKey(word)) {
                count += 1;
                triggerWord = word;
                //check for entity number-> 2 or 1
            }
        }
        if (count != 1) {
            triggerWord = "Multiple Command or no Trigger Command";
        }
        return triggerWord;
    }

    private Artefact itemParse(ArrayList<String> commands) {
        Artefact itemPlayer = null;
        Artefact itemLocation = null;
        for(String oneCommand : commands){
            itemPlayer = player.currentlocation.artefactsMap.get(oneCommand);
            itemLocation = player.currentlocation.artefactsMap.get(oneCommand);
            if(itemPlayer!=null){
                return itemPlayer;
            }else if(itemLocation!=null){
                return itemLocation;
            }
        }
        return null;
    }

    private String locationParse(ArrayList<String> commands){
        String location = "";
        int count = 0;
        for(String singleCommand: commands){
            if(entityParser.locations.containsKey(singleCommand)){
                location = singleCommand;
                count+=1;
            }
        }
        if(count == 1){
            return location;
        }
        return "location not found";
    }
}
