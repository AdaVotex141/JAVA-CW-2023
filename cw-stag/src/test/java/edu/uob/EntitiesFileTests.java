package edu.uob;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import com.alexmerz.graphviz.objects.Edge;

import static org.junit.jupiter.api.Assertions.*;

final class EntitiesFileTests {

  // Test to make sure that the basic entities file is readable
  @Test
  void testBasicEntitiesFileIsReadable() {
      try {
          Parser parser = new Parser();
          FileReader reader = new FileReader("config" + File.separator + "basic-entities.dot");
          parser.parse(reader);
          Graph wholeDocument = parser.getGraphs().get(0);
          ArrayList<Graph> sections = wholeDocument.getSubgraphs();

          // The locations will always be in the first subgraph
          ArrayList<Graph> locations = sections.get(0).getSubgraphs();
          Graph firstLocation = locations.get(0);
          Node locationDetails = firstLocation.getNodes(false).get(0);
          // Yes, you do need to get the ID twice !
          String locationName = locationDetails.getId().getId();
          String decription =  locationDetails.getAttribute("description");
          assertEquals("A log cabin in the woods",decription);
          assertEquals("cabin", locationName, "First location should have been 'cabin'");

          // The paths will always be in the second subgraph
          ArrayList<Edge> paths = sections.get(1).getEdges();
          Edge firstPath = paths.get(0);
          Node fromLocation = firstPath.getSource().getNode();
          String fromName = fromLocation.getId().getId();
          Node toLocation = firstPath.getTarget().getNode();
          String toName = toLocation.getId().getId();
          assertEquals("cabin", fromName, "First path should have been from 'cabin'");
          assertEquals("forest", toName, "First path should have been to 'forest'");

      } catch (FileNotFoundException fnfe) {
          fail("FileNotFoundException was thrown when attempting to read basic entities file");
      } catch (ParseException pe) {
          fail("ParseException was thrown when attempting to read basic entities file");
      }
  }

  @Test
    void testEntityParser(){
      try{
          File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
          EntityParser parserTest= new EntityParser(entitiesFile);
          //check path
          assertTrue(parserTest.paths.containsKey("cabin"));
          assertTrue(parserTest.paths.containsValue("forest"));

          assertEquals(parserTest.paths.get("cabin"),"forest");


          assertTrue(parserTest.paths.containsKey("forest"));
          assertTrue(parserTest.paths.containsKey("cellar"));

          assertEquals(parserTest.paths.get("forest"),"cabin");
          assertEquals(parserTest.paths.get("cellar"),"cabin");
          //actually done
          assertTrue(parserTest.locations.containsKey("cabin"));
          assertTrue(parserTest.locations.containsKey("forest"));
          assertTrue(parserTest.locations.containsKey("cellar"));
          assertTrue(parserTest.locations.containsKey("storeroom"));

      }catch(FileNotFoundException fnfe){
          
      }catch(ParseException pe){

      }
  }
}
