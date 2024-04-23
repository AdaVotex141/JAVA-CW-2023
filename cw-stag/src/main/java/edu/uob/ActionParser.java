package edu.uob;

import edu.uob.Command.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class ActionParser {
    HashMap<String, HashSet<GameAction>> actions;
    public ActionParser(){
        actions = new HashMap<>();

        actionParse();
    }

    public void actionParse(){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse("config" + File.separator + "basic-actions.xml");
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            // Get the first action (only the odd items are actually actions - 1, 3, 5 etc.)






            Element firstAction = (Element)actions.item(1);
            Element triggers = (Element)firstAction.getElementsByTagName("triggers").item(0);
            // Get the first trigger phrase
            String firstTriggerPhrase = triggers.getElementsByTagName("keyphrase").item(0).getTextContent();
        } catch(ParserConfigurationException pce) {
            System.err.print("ParserConfigurationException was thrown when attempting to read basic actions file");
        } catch(SAXException saxe) {
            System.err.print("SAXException was thrown when attempting to read basic actions file");
        } catch(IOException ioe) {
            System.err.print("IOException was thrown when attempting to read basic actions file");
        }
    }
}
