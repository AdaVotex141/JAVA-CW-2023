package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectHandler extends CommandHandler {

    public SelectHandler(String path) {
        super(path);
    }
    //<Select>::=  "SELECT " <WildAttribList> " FROM " [TableName] |\
// "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
    public StringBuilder select(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        //tokenIndex+=1;
        if(tokens.get(tokenIndex).equals("*")){
            //TODO: select all




        }else{
            //get the set of attributes
            HashSet<String> attributeSet=new HashSet<>();
            while(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
                attributeSet.add(tokens.get(tokenIndex));
            }
            tokenIndex+=1;
            boolean flag=reader.useTable(tokens.get(tokenIndex),storageFolderPath);
            if(!flag){
                returnBuilder.append("[ERROR] Can't find Table");
                return returnBuilder;
            }
            //TODO: Implement SELECT





            //WHERE
            if(tokens.get(tokens.size()-2).equalsIgnoreCase("WHERE")){
                //TODO:DEAL with WHERE




            }
        }
        return returnBuilder;
    }
}
