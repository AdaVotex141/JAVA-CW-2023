package edu.uob.Entity;

import edu.uob.*;
import edu.uob.Command.GameAction;

import java.util.HashSet;
import java.util.Map;

public class Player {
    public Location currentlocation;
    public HashSet<String> carryings;
    int Health;
    public EntityParser entityParser;
    public Player(EntityParser entityParser){
        this.entityParser =  entityParser;
        currentlocation = entityParser.getBornLocation();
        this.carryings = new HashSet<>();
    }
    //inv:look at carryings
    public StringBuilder playerInv(){
        StringBuilder result = new StringBuilder();
        //result.append("Current Location:"+this.currentlocation.getName());
        if (!carryings.isEmpty()){
            for(String carrying:carryings){
                result.append(carrying+" ");
            }
        }else{
            result.append("[WARNING] Doesn't have anything");
        }
        return result;
    }

    //goto
    public StringBuilder playerGoto(String toLocation){
        StringBuilder result = new StringBuilder();
        String currentlocationName = this.currentlocation.getName();
        //check wether toLocation is in the paths
        String toLocationCheck = entityParser.paths.get(currentlocationName);

        if(toLocationCheck.equals(toLocation)){
            Location finalLocation = entityParser.findLocation(toLocation);
            this.currentlocation = finalLocation;
            result.append("reach "+toLocation);
        }else{
            result.append("[WARNING]Can't reach");
            System.err.print("toLocation doesn't exist");
        }
        return result;
    }
    //drop
    public boolean playerDrop(Artefact item){
        if(!carryings.isEmpty()){
            boolean removeCheck = carryings.remove(item.getName());
            if(removeCheck == true){
                //System.out.print("player remove success");
                currentlocation.setArtefact(item);
                return true;
            }else{
                //System.out.print("player remove fail");
                return false;
            }
        }else{
            //System.out.print("inventory is empty");
            return false;
        }
    }

    //get
    public boolean playerGet(Artefact item){
        if(item instanceof Artefact){
            Artefact itemArtefact = item;
            currentlocation.artefactsMap.remove(itemArtefact.getName());
            carryings.add(itemArtefact.getName());
            return true;
        }return false;
    }

    //look:prints names and descriptions of entities in the current location and lists paths to other locations
    public StringBuilder playerLook(){
        StringBuilder result = new StringBuilder();
        //names and descriptions:
        //locations:
        result.append("Location:");
        result.append(currentlocation.getName()+"("+currentlocation.getDescription() +")");
        result.append("\n");
        //Artefacts:
        if(!currentlocation.artefactsMap.isEmpty()){
            result.append("Artefacts:");
            Iterable<Artefact> artefactsIteractor = currentlocation.artefactsMap.values();
            for (Artefact value : artefactsIteractor) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+") ");
            }
            result.append("\n");
        }
        //furnitures:
        if(!currentlocation.furnituresMap.isEmpty()){
            result.append("Furniture:");
            Iterable<Furniture> furnitureIterator = currentlocation.furnituresMap.values();
            for (Furniture value : furnitureIterator) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+") ");
            }
            result.append("\n");
        }

        //Characters:
        if(!currentlocation.charactersMap.isEmpty()){
            result.append("Character:");
            Iterable<Character> charactorIterator = currentlocation.charactersMap.values();
            for (Character value : charactorIterator) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+"), ");
            }
            result.append("\n");
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
