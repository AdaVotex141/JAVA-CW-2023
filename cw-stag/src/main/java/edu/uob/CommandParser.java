package edu.uob;

import edu.uob.Command.GameAction;
import edu.uob.Entity.*;
import edu.uob.Entity.Character;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashSet;

//TODO can't get shovel after the elf produced it -> can't detect the produced new product ????
//TODO the 'cut down' and 'cut'



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
        //this.player = player;
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

        String trigger = findTrigger();
        StringBuilder result = new StringBuilder();

        if (trigger.isEmpty()) {
            result.append("[WARNING] Can't resolve command.");
        }

        if(this.builtinAction.contains(trigger)){
            builtInIntepreter(trigger,result);
        }else if(actionParser.actions.containsKey(trigger)){
            this.gameAction = actionParser.actions.get(trigger);
            HashSet<String> entities = new HashSet<>();
            for(String word: commands){
                if(gameAction.subjects.contains(word)){
                    entities.add(word);
                }
            }
            if(entities.size() == 2 || entities.size() == 1){
                actionFileIntepreter(trigger,result,entities);
            }else{
                result.append("[WARNING] no or multiple entities detected");
            }
        }else{
            result.append("[WARNING] no triggerword");
        }
        return result;
    }
    private StringBuilder builtInIntepreter(String trigger,StringBuilder result){

        int count = 0;
        // entity <= 1
        for(String command :commands){
            if(entityParser.locations.containsKey(command)){
                count+=1;
            }
            for(Location locationItem : entityParser.locations.values()){
                if(locationItem.artefactsMap.containsKey(command)
                        || locationItem.furnituresMap.containsKey(command)
                        || locationItem.charactersMap.containsKey(command)){
                    count+=1;
                }
            }
        }
        if(count>1){
            result.append("[WARNING] invalid command");
            return result;
        }

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
                result.append("[WARNING] Can't get");
            } else {
                boolean getFlag = player.playerGet(item);
                if (getFlag) {
                    result.append("[GET SUCCESS]");
                } else {
                    result.append("[WARNING] invalid, get fail");
                }
            }

        } else if (trigger.equals("drop")) {
            Location storeRoom = entityParser.getStoreRoom();
            for(String command:commands){
                if (storeRoom.artefactsMap.containsKey(command)){
                    if(player.carryings.contains(command)){
                        Artefact item = storeRoom.artefactsMap.get(command);
                        player.currentlocation.setArtefact(item);
                        storeRoom.artefactsMap.remove(command);
                        player.carryings.remove(command);
                        result.append("[Drop "+item.getName()+ " success]");
                        return result;
                    }
                }
            }
            result.append("[warning] invalid, drop fail");
//            Artefact item = itemParse(commands);
//            if (item == null) {
//                result.append("[warning] invalid, doesn't have the item in the bag");
//            } else {
//                boolean getFlag = player.playerDrop(item);
//                if (getFlag) {
//                    result.append("");
//                } else {
//                    result.append("[warning] invalid, drop fail");
//                }
//            }

        } else if (trigger.equals("inv") || trigger.equals("inventory")) {
            result.append(player.playerInv());
        }else if(trigger.equals("health")){
            result.append("hp:" + player.getHealth());
        }
        return result;
    }

    private StringBuilder actionFileIntepreter(String trigger,StringBuilder result, HashSet<String> entities){
        gameAction = actionParser.actions.get(trigger);
        //check whether consumed is in the player's carryings or current location
        //TODO only support one consumed for nows
        if (player.carryings.contains(gameAction.consumed)
        || player.currentlocation.artefactsMap.containsKey(gameAction.consumed)
        || gameAction.consumed.equals("health") || player.currentlocation.furnituresMap.containsKey(gameAction.consumed)){
                //if consumed is potion:
            if(gameAction.consumed.equals("potion")){
                if(player.carryings.contains("potion")){
                    player.playerHealthAdd();
                    moveToStoreRoom("potion");
                }else{
                    result.append("[warning] You don't have potion in inventory");
                    return result;
                }
            }else if(gameAction.consumed.equals("health")){
                player.playerHealthMinus();
            }else{
                boolean flag = false;
                for (String item : gameAction.subjects) {
                    if(player.carryings.contains(item)){
                        //check the carryings?
                        moveToStoreRoom(gameAction.consumed);
                        //Produced:
                        for(String produced: gameAction.produced){
                            producedItem(produced);
                        }
                        flag = true;
                    }
                }
                if(flag){
                    result.append(gameAction.narration);
                    return result;
                }else{
                    result.append("[warning]missing");
                    return result;
                }
            }
            result.append(gameAction.narration);
            if(!player.playerHealthdetect()){
                result.append("\n"+"you died and lost all of your items, you must return to the start of the game");
                player.playerReset();
            }
            return result;
        }else{
            result.append("[warning]Can't do it");
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
            }else if (actionParser.actions.containsKey(word)) {
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

    private void moveToStoreRoom(String consumed){
        if(player.carryings.contains(gameAction.consumed)){
            player.carryings.remove(gameAction.consumed);
        }
        //loop all the locations
        for(Location locationCheck : entityParser.locations.values()){
            if(locationCheck.artefactsMap.containsKey(gameAction.consumed)){
                Artefact consumedItem = locationCheck.artefactsMap.get(gameAction.consumed);
                entityParser.getStoreRoom().setArtefact(consumedItem);
                locationCheck.artefactsMap.remove(gameAction.consumed);
            }
            if(locationCheck.furnituresMap.containsKey(gameAction.consumed)){
                Furniture item = locationCheck.furnituresMap.get(gameAction.consumed);
                entityParser.getStoreRoom().setFurniture(item);
                locationCheck.furnituresMap.remove(gameAction.consumed);
            }
        }
    }

    private void producedItem(String produced){
        // loop through all the locations to find this thing, and set it at current location
        for(Location locationCheck : entityParser.locations.values()){
            //if produced is a path, add this path
            if(gameAction.produced.contains(locationCheck.getName())){
                String currentLocationName = this.player.currentlocation.getName();
                String toName = locationCheck.getName();
                entityParser.multiplePaths.put(currentLocationName,new HashSet<String>());
                entityParser.multiplePaths.get(currentLocationName).add(entityParser.paths.get(currentLocationName));
                entityParser.multiplePaths.get(currentLocationName).add(toName);
                entityParser.paths.remove(currentLocationName);
                //entityParser.paths.put(player.currentlocation.getName(),locationCheck.getName());
            }
            //check for items
//            for(String item : gameAction.produced){
//                if(locationCheck.furnituresMap.containsKey(item)){
//                    player.currentlocation.setFurniture(locationCheck.furnituresMap.get(item));
//                }else if(locationCheck.charactersMap.containsKey(item)){
//                    player.currentlocation.setCharacter(locationCheck.charactersMap.get(item));
//                }else if(locationCheck.artefactsMap.containsKey(item)){
//                    player.currentlocation.setArtefact(locationCheck.artefactsMap.get(item));
//                }
//            }
        }
        //check storeRoom for items:
        Location storeRoom = entityParser.getStoreRoom();
        if(storeRoom.artefactsMap.containsKey(produced)){
            Artefact item = storeRoom.artefactsMap.get(produced);
            player.currentlocation.setArtefact(item);
            storeRoom.artefactsMap.remove(produced);
        }
        if(storeRoom.furnituresMap.containsKey(produced)){
            Furniture item = storeRoom.furnituresMap.get(produced);
            player.currentlocation.setFurniture(item);
            storeRoom.furnituresMap.remove(produced);
        }
        if(storeRoom.charactersMap.containsKey(produced)){
            Character item = storeRoom.charactersMap.get(produced);
            player.currentlocation.setCharacter(item);
            storeRoom.charactersMap.remove(item);
        }
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
        }else{
            return null;
        }
    }
}
