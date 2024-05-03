package edu.uob.Command;

import edu.uob.ActionParser;
import edu.uob.Entity.Artefact;
import edu.uob.Entity.Character;
import edu.uob.Entity.Furniture;
import edu.uob.Entity.Location;
import edu.uob.Entity.Player;
import edu.uob.EntityParser;

import java.util.ArrayList;
import java.util.HashSet;

public class ActionIntep {
    ArrayList<String> commands;
    ActionParser actionParser;
    GameAction gameAction;
    Player player;
    EntityParser entityParser;


    public ActionIntep(ArrayList<String> commands,ActionParser actionParser,
                       Player player,GameAction gameAction,EntityParser entityParser    ){
        this.actionParser = actionParser;
        this.commands = commands;
        this.player = player;
        this.gameAction = gameAction;
        this.entityParser = entityParser;
    }

    public StringBuilder actionFileIntepreter(String trigger,StringBuilder result){
        gameAction = actionParser.actions.get(trigger);
        //check whether consumed is in the player's carryings or current location

        if (entityCheck()){
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
                    result.append("[warning]missing item");
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
        }
        //TODO check for items in other rooms
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

    private boolean entityCheck(){
        if(gameAction.consumed.equals("health") || gameAction.consumed.equals("potion")){
            return true;
        }
            int countSubject = 0;
            for(String subject : gameAction.subjects){
                if(player.currentlocation.artefactsMap.containsKey(subject)
                        || player.currentlocation.furnituresMap.containsKey(subject)
                        || player.currentlocation.charactersMap.containsKey(subject)
                        || player.carryings.contains(subject)){
                    countSubject+=1;
                }
            }
            if(gameAction.subjects.size() == countSubject){
                return true;
            }

        return false;
    }

}
