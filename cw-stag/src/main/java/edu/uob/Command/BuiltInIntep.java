package edu.uob.Command;

import edu.uob.ActionParser;
import edu.uob.Entity.Artefact;
import edu.uob.Entity.Location;
import edu.uob.Entity.Player;
import edu.uob.EntityParser;

import java.util.ArrayList;
import java.util.HashSet;

public class BuiltInIntep {
    private ArrayList<String> commands;
    private EntityParser entityParser;
    private Player player;
    public HashSet<String> builtinAction;

    public BuiltInIntep(ArrayList<String> commands,EntityParser entityParser,
                        Player player, HashSet<String> builtinAction    ){
        this.commands = commands;
        this.entityParser = entityParser;
        this.player = player;
        this. builtinAction = builtinAction;

    }
    public StringBuilder builtInIntepreter(String trigger,StringBuilder result){

        if(!entityCheck()){
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
        } else if (trigger.equals("inv") || trigger.equals("inventory")) {
            result.append(player.playerInv());
        }else if(trigger.equals("health")){
            result.append("hp:" + player.getHealth());
        }
        return result;
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

    private Artefact itemParse(ArrayList<String> commands) {
        Artefact itemPlayer = null;
        for(String oneCommand : commands){
            itemPlayer = player.currentlocation.artefactsMap.get(oneCommand);
            if(itemPlayer!=null){
                return itemPlayer;
            }
        }
        return null;
    }

    private boolean entityCheck(){
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
            return false;
        }
        return true;

    }

}
