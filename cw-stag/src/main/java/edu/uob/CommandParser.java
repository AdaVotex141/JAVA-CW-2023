package edu.uob;

import edu.uob.Command.GameAction;
import edu.uob.Entity.Artefact;
import edu.uob.Entity.GameEntity;
import edu.uob.Entity.Location;
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
    public HashSet<String> builtinAction;

    public CommandParser(EntityParser entityParser, ActionParser actionParser, Player player) {
        commands = new ArrayList<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        //this.gameAction = gameAction;
        this.player = player;
        builtinAction = new HashSet<>();
        builtinAction.add("look");
        builtinAction.add("inv");
        builtinAction.add("inventory");
        builtinAction.add("get");
        builtinAction.add("goto");
        builtinAction.add("drop");
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

        if(this.builtinAction.contains(trigger)){
            builtInIntepreter(trigger,result);
        }else if(actionParser.actions.containsKey(trigger)){
            GameAction gameAction = actionParser.actions.get(trigger);
            HashSet<String> entities = new HashSet<>();
            for(String word: commands){
                if(gameAction.subjects.contains(word)){
                    entities.add(word);
                }
            }
            if(entities.size() == 2 || entities.size() == 1){
                result.append(actionFileIntepreter(trigger,result,entities));
            }else{
                result.append("[WARNING] no or multiple entities detected");
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
                result.append("[WARNING] invalid, you can't goto this location");
            } else {
                result.append(player.playerGoto(toLocation));
            }

        } else if (trigger.equals("get")) {
            Artefact item = itemParse(commands);
            if (item == null) {
                result.append("[WARNING] the item isn't appear in this location");
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
                result.append("[WARNING] invalid, doesn't have the item in the bag");
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
        //check whether consumed is in the player's carryings or heath
        if (player.carryings.contains(gameAction.consumed)){
                //if consumed is potion:
            if(gameAction.consumed.equals("potion")){
                player.playerHealthAdd();
                player.carryings.remove(gameAction.consumed);
            }else{
                //move the produced from storeroom to here
                Location storeRoom = entityParser.locations.get("storeroom");
                for(String item : gameAction.produced){
                    if(storeRoom.furnituresMap.containsKey(item)){
                        player.currentlocation.setFurniture(storeRoom.furnituresMap.get(item));
                    }else if(storeRoom.charactersMap.containsKey(item)){
                        player.currentlocation.setCharacter(storeRoom.charactersMap.get(item));
                    }else if(storeRoom.artefactsMap.containsKey(item)){
                        player.carryings.add(item);
                    }
                }
            }
            result.append(gameAction.narration);
        }else if(gameAction.consumed.equals("health")){
            //if consumed is health
            player.playerHealthMinus();
            result.append(gameAction.narration);
        }else{
            result.append("[warning] missing item in bag");
        }
        return result;
    }


    private String findTrigger() {
        int count = 0;
        int countEntity = 0;
        String triggerWord = "";
        for (String word : this.commands) {
            if (this.builtinAction.contains(word)) {
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
