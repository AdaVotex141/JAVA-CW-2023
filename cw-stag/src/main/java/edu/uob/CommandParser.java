package edu.uob;

import edu.uob.Command.GameAction;
import edu.uob.Entity.Artefact;
import edu.uob.Entity.GameEntity;
import edu.uob.Entity.Player;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashSet;


public class CommandParser {
    //find trigger->
    ArrayList<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    GameAction gameAction;
    Player player;

    public CommandParser(EntityParser entityParser, ActionParser actionParser, GameAction gameAction) {
        commands = new ArrayList<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        this.gameAction = gameAction;
        this.player = new Player(entityParser);
    }

    public StringBuilder commandParse(String command) {
        commands.clear();

        String[] words = command.split(" ");
        for (String word : words) {
            commands.add(word);
        }

        String trigger = findTrigger();
        StringBuilder result = new StringBuilder();

        if (trigger.isEmpty()) {
            result.append("[WARNING] Can't resolve command.");
        }

        if (trigger.equals("look")) {
            result.append(player.playerLook());

        } else if (trigger.equals("goto")) {
            //TODO the key thing
            String toLocation = locationParse(commands);
            //System.out.print("This is goto :" + toLocation + "\n");
            if (toLocation == null) {
                result.append("[WARNING] invalid");
            } else {
                result.append(player.playerGoto(toLocation));
            }
//            System.out.print(result.toString());
//            System.out.print(player.currentlocation.getName());

        } else if (trigger.equals("get")) {
            Artefact item = itemParse(commands);
            if (item == null) {
                result.append("[WARNING]");
            } else {
                boolean getFlag = player.playerGet(item);
                if (getFlag) {
                    result.append("[GET SUCCESS]");
                } else {
                    result.append("[WARNING] invalid, get fail");
                }
            }
            //System.out.print(result.toString());

        } else if (trigger.equals("drop")) {
            Artefact item = itemParse(commands);
            if (item == null) {
                result.append("[WARNING]");
            } else {
                boolean getFlag = player.playerDrop(item);
                if (getFlag) {
                    result.append("");
                } else {
                    result.append("[WARNING] invalid, drop fail");
                }
            }
        } else if (trigger.equals("inv") || trigger.equals("inventory")) {
            result.append(player.playerInv());
        }
        return result;
    }

    private String findTrigger() {
        int count = 0;
        String triggerWord = "";
        for (String word : this.commands) {
            if (gameAction.builtinAction.contains(word)) {
                count += 1;
                triggerWord = word;
            }
            if (actionParser.actions.containsKey(word)) {
                count += 1;
                triggerWord = word;
            }
        }
        if (count != 1) {
            triggerWord = "Multiple Command or no Trigger Command";
        }
        return triggerWord;
    }

    private Artefact itemParse(ArrayList<String> commands) {
        Artefact itemPlayer = null;
        Artefact itemLocation = null;
        int count = 0;
        for(String oneCommand : commands){
            itemPlayer = player.currentlocation.artefactsMap.get(oneCommand);
            itemLocation = player.currentlocation.artefactsMap.get(oneCommand);
            if(itemPlayer!=null){
                return itemPlayer;
            }else if(itemLocation!=null){
                return itemLocation;
            }
        }
        return null;
    }

    private String locationParse(ArrayList<String> commands){
        String location = "";
        int count = 0;
        for(String singleCommand: commands){
            if(entityParser.locations.containsKey(singleCommand)){
                location = singleCommand;
                count+=1;
            }
        }
        if(count==1){
            return location;
        }
        return "location not found";
    }
}
