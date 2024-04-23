package edu.uob;

import java.nio.file.Path;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;
import edu.uob.Entity.Location;

public class EntityParser {
    public HashMap<String,Location> locations;

    public EntityParser() throws FileNotFoundException, ParseException {
        locations = new HashMap<>();
        EntityParse();
    }
    public void EntityParse() throws FileNotFoundException, ParseException {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader("config" + File.separator + "basic-entities.dot");
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

        if(i==0){
            locationNew.setAttribute(Location.LocationAttribute.first);
        }else if(i == endIndex){
            locationNew.setAttribute(Location.LocationAttribute.transparent);
        }else{
            locationNew.setAttribute(Location.LocationAttribute.usual);
        }

        //subgraphs artefacts, furniture, character and so on
        ArrayList<Graph> otherEntities = location.getSubgraphs();
        otherEntityParse(otherEntities, locationNew);
    }

    private void otherEntityParse(ArrayList<Graph> otherEntities, Location locationNew){
        int i = 0;
        while(i<otherEntities.size()){
            Node entityDetails = otherEntities.get(i).getNodes(false).get(0);
            String entityType = entityDetails.getAttribute("shape");
            if(entityType.equals("diamond")){
                //artefacts

            }else if(entityType.equals("hexagon")){
                //furniture



            }else if(entityType.equals("ellipse")){
                //characters

            }else{
                System.err.print("Invalid type");
            }
            i++;
        }
    }





    private void PathParse(ArrayList<Edge> paths){
        Edge firstPath = paths.get(0);
        Node fromLocation = firstPath.getSource().getNode();
        String fromName = fromLocation.getId().getId();
        Node toLocation = firstPath.getTarget().getNode();
        String toName = toLocation.getId().getId();
    }



}
