package edu.uob;

//import edu.uob.Command.GameAction;
import edu.uob.Command.*;
import edu.uob.Entity.*;

import java.lang.Character;
import java.util.ArrayList;
import java.util.HashSet;

//TODO trigger->multiple gameAction???
//TODO consumed -> Paths -> how to consumed
//TODO there is more than one 'open' action possible - which one do you want to perform ?

public class CommandParser {
    //find trigger->
    ArrayList<String> commands;
    EntityParser entityParser;
    ActionParser actionParser;
    Player player;
    public HashSet<String> builtinAction;

    public CommandParser(EntityParser entityParser, ActionParser actionParser) {
        commands = new ArrayList<>();
        this.actionParser = actionParser;
        this.entityParser = entityParser;
        builtinAction = new HashSet<>();
        builtinAction.add("look");
        builtinAction.add("inv");
        builtinAction.add("inventory");
        builtinAction.add("get");
        builtinAction.add("goto");
        builtinAction.add("drop");
        builtinAction.add("health");
    }

    public StringBuilder commandParse(String command) {
        commands.clear();

        String[] words = command.split(" ");
        for (String word : words) {
            commands.add(word.toLowerCase());
        }
        StringBuilder result = new StringBuilder();
        //player->

        String playerNameWith = words[0].replace(":","");
        if(builtinAction.contains(playerNameWith)){
            result.append("[WARNING] invalid player name");
            return result;
        }

        if(!entityParser.playerMap.containsKey(words[0])){
            this.player = new Player(this.entityParser,words[0],"a player");
            entityParser.playerMap.put(this.player.getName(),this.player);
        }else{
            this.player = entityParser.playerMap.get(words[0]);
        }

        HashSet<String> triggerWords = new HashSet<>();
        triggerWords.clear();
        triggerWords = findTrigger();
        String trigger = triggerParser(triggerWords);

        if (triggerWords.isEmpty() || trigger.equals("")){
            result.append("[warning] Can't resolve commands");
            return result;
        }

        interpreter(trigger,result);
        return result;
    }

    private void interpreter(String trigger, StringBuilder result){
        if(this.builtinAction.contains(trigger)){
            BuiltInIntep intep = new BuiltInIntep(commands,entityParser,player,builtinAction);
            intep.builtInIntepreter(trigger,result);

        }else if(actionParser.actions.containsKey(trigger)){
            int gameActionCount = 0;
            GameAction rightGameAction = new GameAction();
            for(GameAction gameAction :actionParser.actions.get(trigger)){
                HashSet<String> entities = new HashSet<>();
                for(String word: commands){
                    entities = entityCount(word,entities);
                }
                if(gameAction.subjects.containsAll(entities) && !entities.isEmpty()){
                    rightGameAction = gameAction;
                    gameActionCount +=1;
                }
            }
            if(gameActionCount == 0){
                result.append("[WARNING] invalid or no entities detected");
            }else if (gameActionCount == 1){
                ActionIntep intep = new ActionIntep(commands,actionParser,player,rightGameAction,entityParser);
                intep.actionFileIntepreter(result);
            }else if (gameActionCount > 1){
                result.append("there is more than one "+ trigger +" action possible - which one do you want to perform ?");
            }
        }
    }

    private HashSet<String>  findTrigger() {
        HashSet<String> triggerWord = new HashSet<>();
        for (String word : this.commands) {
            if (this.builtinAction.contains(word)) {
                triggerWord.add(word);
            }else if (actionParser.actions.containsKey(word)) {
                triggerWord.add(word);
            }
        }
        return triggerWord;
    }

    private HashSet<String> entityCount(String word,HashSet<String> entities){
        for(Location locationCheck : entityParser.locations.values()){
            if(locationCheck.artefactsMap.containsKey(word)){
                entities.add(word);
            }else if(locationCheck.furnituresMap.containsKey(word)){
                entities.add(word);
            }else if(locationCheck.charactersMap.containsKey(word)){
                entities.add(word);
            }
        }
        return entities;
    }

    //find the only trigger depends on commandss
    private String triggerParser(HashSet<String> triggerWords){
        String trigger = "";
        if(triggerWords.size()>1){
            trigger = multipleTriggerParse(triggerWords);
        }else if(triggerWords.size() == 1){
            String[] triggerArray = triggerWords.toArray(new String[0]);
            trigger = triggerArray[0];
        }
        return trigger;
    }

    private String multipleTriggerParse(HashSet<String> triggerWords){
            int count = 0;
            String trigger = "";
            for(String triggerWord : triggerWords){
                if(actionParser.actions.containsKey(triggerWord)){
                    // open unlock -> check for further entity in command
                    HashSet<String> entityCommand = new HashSet<>();
                    for(String command : commands){
                        entityCommand = entityCount(command,entityCommand);
                    }
                    //HashSet<String> entityCommand = findEntityCommands();
                    HashSet<GameAction> gameActionHashSet = actionParser.actions.get(triggerWord);
                    for(GameAction gameAction : gameActionHashSet){
                        if(gameAction.subjects.containsAll(entityCommand) && !entityCommand.isEmpty()){
                            trigger = triggerWord;
                            count+=1;
                        }
                    }

                }else if(builtinAction.contains(triggerWord)){
                    trigger = triggerWord;
                    count+=1;
                }
            }
            if(count != 1){
                trigger = "";
            }
        return trigger;
    }
}
