package edu.uob;

import java.nio.file.Path;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import edu.uob.Entity.Artefact;
import edu.uob.Entity.Character;
import edu.uob.Entity.Furniture;
import edu.uob.Entity.Location;

public class EntityParser {
    public HashMap<String,Location> locations;
    public HashMap<String, String> paths;

    public EntityParser(File entitiesFile) throws FileNotFoundException, ParseException {
        locations = new HashMap<>();
        paths = new HashMap<>();
        EntityParse(entitiesFile);
    }
    public void EntityParse(File entitiesFile) {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entitiesFile.getPath());
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();

            // The locations will always be in the first subgraph
            ArrayList<Graph> locations = sections.get(0).getSubgraphs();
            LocationsParse(locations);

            // The paths will always be in the second subgraph
            ArrayList<Edge> paths = sections.get(1).getEdges();
            PathParse(paths);

        } catch (FileNotFoundException fnfe) {
            System.err.print("File error");
        } catch (ParseException pe) {
            System.err.print("Parse error");
        }
    }
    private void LocationsParse(ArrayList<Graph> locations){
        int i=0;
        int endIndex = locations.size()-1;
        while(i < locations.size()){
            ClusterParse(locations.get(i),i ,endIndex);
            i++;
        }
        //TODO: deal with first and last location
    }
    //this refers to one cluster
    private void ClusterParse(Graph location,int i, int endIndex){
        Node locationDetails = location.getNodes(false).get(0);
        String locationName = locationDetails.getId().getId();
        String description = locationDetails.getAttribute("description");

        Location locationNew = new Location(locationName,description);
        locations.put(locationName,locationNew);

        if(i == 0){
            locationNew.setAttribute(Location.LocationAttribute.first);
        }else if(i == endIndex){
            locationNew.setAttribute(Location.LocationAttribute.store);
        }else{
            locationNew.setAttribute(Location.LocationAttribute.usual);
        }

        //subgraphs artefacts, furniture, character and so on
        ArrayList<Graph> otherEntities = location.getSubgraphs();
        otherEntityParse(otherEntities, locationNew);
    }

    private void otherEntityParse(ArrayList<Graph> otherEntities, Location locationNew){
        int i = 0;
        //loop every subgraph artefacts, furnitures, etc.
        while(i<otherEntities.size()){
            // artefacts:
            String entityType = otherEntities.get(i).getId().getId();
            ArrayList<Node> nodes = otherEntities.get(i).getNodes(false);
//            Node firstNode = nodes.get(0);
//            System.out.print("  "+firstNode.getId().getId()+"  ");
//            String entityType = firstNode.getAttribute("shape");
//            System.out.print(entityType);

            for (Node node:nodes){
                String name = node.getId().getId();
                String description = node.getAttribute("description");
                if(entityType.equals("artefacts")){
                    //artefacts
                    Artefact artefactNew = new Artefact(name,description);
                    locationNew.setArtefact(artefactNew);
                }else if(entityType.equals("furniture")){
                    //furniture
                    Furniture furnitureNew = new Furniture(name,description);
                    locationNew.setFurniture(furnitureNew);
                }else if(entityType.equals("characters")){
                    //characters
                    Character characterNew = new Character(name,description);
                    locationNew.setCharacter(characterNew);
                }
            }
            i++;
        }
    }

    private void PathParse(ArrayList<Edge> paths){
        for (Edge path : paths){
            Node fromLocation = path.getSource().getNode();
            String fromName = fromLocation.getId().getId();
            Node toLocation = path.getTarget().getNode();
            String toName = toLocation.getId().getId();
            this.paths.put(fromName,toName);
            System.out.print(fromName+"->"+toName+"\n");
        }
    }

    public Location getBornLocation(){
        if (locations == null || locations.isEmpty()) {
            System.out.print("No locations yet");
            return null;
        }
        Collection<Location> values = locations.values();
        for(Location location: values){
            if(location.getAttribute().equals(Location.LocationAttribute.first)){
                return location;
            }
        }
        return null;
    }

    public Location getStoreRoom(){
        if (locations == null || locations.isEmpty()) {
            System.out.print("No locations yet");
            return null;
        }
        Collection<Location> values = locations.values();
        for(Location location: values){
            if(location.getAttribute().equals(Location.LocationAttribute.store)){
                return location;
            }
        }
        return null;
    }

    public Location findLocation(String location){
        Location findLocation = locations.get(location);
        return findLocation;
    }
    public String findLocationName(Location location){
        for (Map.Entry<String, Location> entry : locations.entrySet()) {
            if (location.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


}
