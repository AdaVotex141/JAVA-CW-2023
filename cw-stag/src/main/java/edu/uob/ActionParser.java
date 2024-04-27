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
import java.util.ArrayList;
import java.util.HashMap;

public class ActionParser {
    HashMap<String, GameAction> actions;
    public ActionParser(File actionsFile){
        actions = new HashMap<>();
        actionParse(actionsFile);
    }

    public void actionParse(File actionsFile){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionsFile.getPath());
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();
            // Get the first action (only the odd items are actually actions - 1, 3, 5 etc.)
            for(int i = 0; i < actions.getLength();i++){
                if(i % 2 ==1){
                    actionsParse(actions,i);
                }
            }
        } catch(ParserConfigurationException pce) {
            System.err.print("ParserConfigurationException was thrown when attempting to read basic actions file");
        } catch(SAXException saxe) {
            System.err.print("SAXException was thrown when attempting to read basic actions file");
        } catch(IOException ioe) {
            System.err.print("IOException was thrown when attempting to read basic actions file");
        }
    }

    private void actionsParse(NodeList actions, int index){
        Element action = (Element) actions.item(index);
        Element triggers = (Element)action.getElementsByTagName("triggers").item(0);
        ArrayList<String> triggersName=triggerParse(triggers);
        GameAction gameAction = new GameAction();

        Element subjects = (Element)action.getElementsByTagName("subjects").item(0);
        String tag = "subjects";
        subParse(subjects, gameAction, tag);

        NodeList consumedList = action.getElementsByTagName("consumed");
        if (consumedList.getLength()>1){
            Element consumed = (Element)action.getElementsByTagName("consumed");
            tag = "consumed";
            subParse(consumed, gameAction, tag);
        }

        NodeList producedList = action.getElementsByTagName("produced");
        if (producedList.getLength()>1){
            Element produced = (Element)action.getElementsByTagName("produced");
            tag = "produced";
            subParse(produced, gameAction, tag);
        }


        NodeList narration = action.getElementsByTagName("narration");
        Element narrationElement = (Element) narration.item(0);
        String narrationString = narrationElement.getTextContent();
        gameAction.setNarration(narrationString);

        for(String trigger : triggersName){
            this.actions.put(trigger, gameAction);
        }
    }



    private ArrayList triggerParse(Element triggers){
        ArrayList<String> triggerName=new ArrayList<>();
        int i = 0;
        while(triggers.getElementsByTagName("keyphrase").item(i)!=null){
            String firstTriggerPhrase = triggers.getElementsByTagName("keyphrase").item(i).getTextContent();
            triggerName.add(firstTriggerPhrase);
            i++;
        }
        return triggerName;
    }

    private void subParse(Element nodes, GameAction gameAction, String tag){
        int i = 0;
        while(nodes.getElementsByTagName("entity").item(i)!=null){
            String elementName = nodes.getElementsByTagName("entity").item(i).getTextContent();
            if(tag.equals("subjects")){
                gameAction.setSubjects(elementName);
            }else if(tag.equals("consumed")){
                gameAction.setConsumed(elementName);
            }else if(tag.equals("produced")){
                gameAction.setProduced(elementName);
            }else{
                System.err.print("Invalid tagName");
            }
            i++;
        }
    }
}
