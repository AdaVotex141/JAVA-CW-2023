package edu.uob;

import java.util.ArrayList;

public class DeleteHandler extends CommandHandler{
    public DeleteHandler(String path) {
        super(path);
    }
    //<Delete> ::=  "DELETE " "FROM " [TableName] " WHERE " <Condition>
    public StringBuilder delete(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(tokens.size()<6){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        if(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
            returnBuilder.append("[ERROR]:Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;
        boolean flag=this.reader.useTable(tokens.get(tokenIndex),storageFolderPath);
        if(!flag){
            returnBuilder.append("[ERROR]:Table doesn't exist");
        }
        tokenIndex+=1;
        //TODO: CONDITION







        return returnBuilder;
    }
}
