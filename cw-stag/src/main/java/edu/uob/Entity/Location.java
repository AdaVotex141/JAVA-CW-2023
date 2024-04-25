package edu.uob.Entity;

import java.util.HashMap;

public class Location extends GameEntity{
    protected HashMap<String, Furniture> furnituresMap;
    protected HashMap<String, Character> charactersMap;
    protected HashMap<String, Artefact> artefactsMap;

    private LocationAttribute attribute = null;


    public enum LocationAttribute{
        first,
        usual,
        store
    }
    //TODO: Paths


    public Location(String name, String description) {
        super(name, description);
        artefactsMap= new HashMap<>();
        furnituresMap = new HashMap<>();
        charactersMap = new HashMap<>();
    }

    public void setAttribute(LocationAttribute attribute){
        this.attribute = attribute;
    }
    public LocationAttribute getAttribute(){
        return attribute;
    }

    public void setFurniture(Furniture furniture) {
        furnituresMap.put(furniture.getName(),furniture);
    }
    public Furniture getFurniture(String furnitureName){
        Furniture furniture = furnituresMap.get(furnitureName);
        return furniture;
    }

    public void setCharacter(Character character){
        charactersMap.put(character.getName(),character);
    }
    public Character getCharacter(String characterName){
        Character character = charactersMap.get(characterName);
        return character;
    }

    public void setArtefact(Artefact artefact){
        artefactsMap.put(artefact.getName(),artefact);
    }
    public Artefact getArtefact(String artefactName){
        Artefact artefact = artefactsMap.get(artefactName);
        return artefact;
    }
    public void deleteArtefact(Artefact artefact){
        String artefactName = artefact.getName();
        artefactsMap.remove(artefactName,artefact);
    }



}
