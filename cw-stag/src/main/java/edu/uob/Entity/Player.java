package edu.uob.Entity;

import edu.uob.*;
import edu.uob.Command.GameAction;

import java.util.HashSet;
import java.util.Map;

public class Player {
    Location currentlocation;
    HashSet<String> carryings;
    int Health;
    EntityParser entityParser;
    public Player(EntityParser entityParser){
        currentlocation = entityParser.getBornLocation();
    }
    //inv:look at carryings
    public StringBuilder playerInv(){
        StringBuilder result = new StringBuilder();
        if (!carryings.isEmpty()){
            for(String carrying:carryings){
                result.append(carrying+",");
            }
        }else{
            result.append("[WARNING] Doesn't have anything");
        }
        return result;
    }

    //goto
    public StringBuilder playerGoto(String toLocation){
        StringBuilder result = new StringBuilder();
        String currentLocation = entityParser.findLocationName(this.currentlocation);
        String toLocationCheck = entityParser.paths.get(currentLocation);
        if(toLocationCheck.equals(toLocation)){
            Location finalLocation = entityParser.findLocation(toLocation);
            this.currentlocation = finalLocation;
            result.append("reach"+toLocation);
        }else{
            result.append("[WARNING]Can't reach");
            System.err.print("toLocation doesn't exist");
        }
        return result;
    }
    //drop
    public void playerDrop(Artefact item){
        if(!carryings.isEmpty()){
            boolean removeCheck = carryings.remove(item.getName());
            if(removeCheck == true){
                System.out.print("player remove success");
            }else{
                System.out.print("player remove fail");
            }
        }else{
            System.out.print("inventory is empty");
        }

        currentlocation.setArtefact(item);
    }

    //get
    public void playerGet(GameEntity item){
        if(item instanceof Artefact){
            Artefact itemArtefact = (Artefact)item;
            currentlocation.deleteArtefact(itemArtefact);
            if(currentlocation.artefactsMap.containsValue(item)){
                System.err.print("get item fail");
            }
            carryings.add(item.getName());
        }else{
            System.err.print("This item is not a Artefact");
        }
    }

    //look:prints names and descriptions of entities in the current location and lists paths to other locations
    public StringBuilder playerLook(){
        StringBuilder result = new StringBuilder();
        //names and descriptions:
        //Artefacts:
        if(!currentlocation.artefactsMap.isEmpty()){
            result.append("Artefacts:");
            Iterable<Artefact> artefactsIteractor = currentlocation.artefactsMap.values();
            for (Artefact value : artefactsIteractor) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+"), ");
            }
            result.append("\n");
        }else{
            result.append("Doesn't have any artefact at this location\n");
        }

        //furnitures:
        if(!currentlocation.furnituresMap.isEmpty()){
            Iterable<Furniture> furnitureIterator = currentlocation.furnituresMap.values();
            for (Furniture value : furnitureIterator) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+"), ");
            }
            result.append("\n");
        }else{
            result.append("Doesn't have any furniture at this location\n");
        }

        //Characters:
        if(!currentlocation.furnituresMap.isEmpty()){
            Iterable<Character> charactorIterator = currentlocation.charactersMap.values();
            for (Character value : charactorIterator) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+"), ");
            }
            result.append("\n");
        }else{
            result.append("Doesn't have any characters at this location\n");
        }

        //show paths:
        Iterable<Map.Entry<String, String>> entries = entityParser.paths.entrySet();
        for (Map.Entry<String, String> entry : entries){
            String fromPlace = entry.getKey();
            String toPlace =  entry.getValue();
            if(fromPlace.equals(currentlocation.getName())){
                result.append(fromPlace+"->"+toPlace+"\n");
            }
        }
        return result;
    }



}
