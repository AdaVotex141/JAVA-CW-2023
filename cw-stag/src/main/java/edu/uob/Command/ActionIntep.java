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

    public StringBuilder actionFileIntepreter(StringBuilder result){
        if (entityCheck()){
                result.append(doConsumed(result));
            if(!gameAction.subjects.isEmpty()){
                for (String produced : gameAction.produced) {
                    producedItem(produced);
                }
            }
            if(!player.playerHealthdetect()){
                result.append("\n"+"you died and lost all of your items, you must return to the start of the game");
                player.playerReset();
            }
            result.append(gameAction.narration);
        }else{
            result.append("[warning]Can't do it");
        }
            return result;
    }

    private StringBuilder doConsumed(StringBuilder result) {
        if (!gameAction.consumed.isEmpty()) {
            for (String consumed : gameAction.consumed) {
                if (consumed.equals("potion")) {
                    result.append(drinkPotion(result));
                } else if (consumed.equals("health")) {
                    player.playerHealthMinus();
                } else if (locationCheck()) {
                    consumedPaths(consumed);
                } else {
                    moveToStoreRoom(consumed);
                }
            }
        }
        return result;
    }


    private void moveToStoreRoom(String consumed){
        if(player.carryings.contains(consumed)){
            player.carryings.remove(consumed);
        }
        //loop all the locations
        for(Location locationCheck : entityParser.locations.values()){
            if(locationCheck.artefactsMap.containsKey(consumed)){
                Artefact consumedItem = locationCheck.artefactsMap.get(consumed);
                entityParser.getStoreRoom().setArtefact(consumedItem);
                locationCheck.artefactsMap.remove(consumed);
            }
            if(locationCheck.furnituresMap.containsKey(consumed)){
                Furniture item = locationCheck.furnituresMap.get(consumed);
                entityParser.getStoreRoom().setFurniture(item);
                locationCheck.furnituresMap.remove(consumed);
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
// check whether subject needed for this gameAction is in currentSpace
    private boolean entityCheck(){

            HashSet<String> entitySet = new HashSet<>();
            for(String subject : gameAction.subjects){
                if(player.currentlocation.furnituresMap.containsKey(subject)
                        || player.currentlocation.charactersMap.containsKey(subject)
                        || player.carryings.contains(subject)){
                    entitySet.add(subject);
                }
            }
            if(gameAction.subjects.equals(entitySet)){
                return true;
            }
        return false;
    }

    private boolean locationCheck(){
        for (Location locationCheck : entityParser.locations.values()){
            if(gameAction.consumed.equals(locationCheck.getName())){
                return true;
            }
        }
        return false;
    }

    private void consumedPaths(String toName){
        String currentLocationName = this.player.currentlocation.getName();
        if(entityParser.paths.containsKey(currentLocationName)
                && entityParser.paths.get(currentLocationName).equals(toName)){
            entityParser.paths.remove(currentLocationName,toName);
        }else if(entityParser.multiplePaths.containsKey(currentLocationName)
                && entityParser.multiplePaths.get(currentLocationName).equals(toName)){
            entityParser.multiplePaths.get(currentLocationName).remove(toName);
            if(entityParser.multiplePaths.get(currentLocationName).size() == 1){
                entityParser.paths.put(currentLocationName,toName);
                entityParser.multiplePaths.remove(currentLocationName);
            }
        }
    }

    private StringBuilder drinkPotion(StringBuilder result){
        if(player.carryings.contains("potion")){
            player.playerHealthAdd();
            moveToStoreRoom("potion");
        }else{
            result.append("[warning] You don't have potion in inventory");
            return result;
        }
        return result;
    }

}
