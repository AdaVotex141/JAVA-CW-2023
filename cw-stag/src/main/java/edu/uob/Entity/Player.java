package edu.uob.Entity;

import edu.uob.*;
import edu.uob.Command.GameAction;

import java.util.HashSet;

public class Player {
    Location currentlocation;
    HashSet<String> carryings;
    int Health;
    EntityParser entityParser;
    public Player(EntityParser entityParser){
        currentlocation = entityParser.getBornLocation();
    }
    //inv:look at carryings
    public String playerInv(){
        String inventory="";
        if (!carryings.isEmpty()){
            for(String carrying:carryings){
                inventory+=carrying+" ";
            }
        }else{
            inventory = "NULL";
        }
        return inventory;
    }

    //goto
    public void playerGoto(String toLocation){
        String currentLocation = entityParser.findLocationName(this.currentlocation);
        String toLocationCheck = entityParser.paths.get(currentLocation);
        if(toLocationCheck.equals(toLocation)){
            Location finalLocation = entityParser.findLocation(toLocation);
            this.currentlocation = finalLocation;
        }else{
            System.err.print("toLocation doesn't exist");
        }
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
            result.append("Artefacts is empty.\n");
        }

        //furnitures:
        if(!currentlocation.furnituresMap.isEmpty()){
            Iterable<Furniture> furnitureIterator = currentlocation.furnituresMap.values();
            for (Furniture value : furnitureIterator) {
                result.append(value.getName());
                result.append(value.getDescription());
            }
            result.append("\n");
        }else{
            result.append("Furniture is empty");
        }

        //Characters:



        return result;
    }



}
