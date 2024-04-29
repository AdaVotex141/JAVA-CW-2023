package edu.uob.Entity;

import edu.uob.*;
import edu.uob.Command.GameAction;

import java.util.HashSet;
import java.util.Map;

public class Player extends GameEntity{
    public Location currentlocation;
    public HashSet<String> carryings;
    private int health;
    public EntityParser entityParser;

    public Player(EntityParser entityParser,String name, String description){
        super(name,description);
        this.entityParser =  entityParser;
        currentlocation = entityParser.getBornLocation();
        this.carryings = new HashSet<>();
        this.health = 3;
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
        boolean multipPathFlag = false;
        if(toLocationCheck == null){
            HashSet<String> valuesForMultplePaths = entityParser.multiplePaths.get(this.currentlocation.getName());
            multipPathFlag = valuesForMultplePaths.contains(toLocation);
            toLocationCheck = "check";
        }

        if(toLocationCheck.equals(toLocation) || multipPathFlag == true){
            Location finalLocation = entityParser.findLocation(toLocation);
            this.currentlocation = finalLocation;
            result.append("reach "+toLocation);
        }else{
            result.append("[WARNING]Can't reach");
        }
        return result;
    }
    //drop
    public boolean playerDrop(Artefact item){
        if(!carryings.isEmpty()){
            boolean removeCheck = carryings.remove(item.getName());
            if(removeCheck == true){
                currentlocation.setArtefact(item);
                return true;
            }else{
                return false;
            }
        }else{
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
        if(!currentlocation.charactersMap.isEmpty() || entityParser.playerMap.size() > 1){
            result.append("Character:");
            Iterable<Character> charactorIterator = currentlocation.charactersMap.values();
            for (Character value : charactorIterator) {
                result.append(value.getName()+"(");
                result.append(value.getDescription()+")");
            }
            //check for other player
            for(Player otherPlayer : entityParser.playerMap.values()){
                if(otherPlayer.currentlocation.getName().equals(this.currentlocation.getName())
                         && !otherPlayer.getName().equals(this.getName())){
                    result.append(otherPlayer.getName()+"("+otherPlayer.getDescription()+")");
                }
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
        //show paths in multiple Paths:
        Iterable<Map.Entry<String, HashSet<String>>> multipleEntries = entityParser.multiplePaths.entrySet();
        for (Map.Entry<String, HashSet<String>> entry : multipleEntries) {
            String fromPlace = entry.getKey();
            HashSet<String> toPlaces = entry.getValue();
            for (String toPlace : toPlaces) {
                if (fromPlace.equals(currentlocation.getName())) {
                    result.append(fromPlace + "->"+toPlace+"\n");
                }
            }
        }

        return result;
    }

    public void playerHealthAdd(){
        health+=1;
    }
    public void playerHealthMinus(){
        health-=1;
    }
    public boolean playerHealthdetect(){
        if(health == 0){
            return false;
        }
        return true;
    }
    public void playerReset(){
        this.entityParser =  entityParser;
        currentlocation = entityParser.getBornLocation();
        this.carryings = new HashSet<>();
        this.health = 3;
    }



}
