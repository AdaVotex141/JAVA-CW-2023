package edu.uob;

import java.util.ArrayList;

public class UpdateHandler extends CommandHandler{
    public UpdateHandler(String path) {
        super(path);
    }
    //<Update> ::=  "UPDATE " [TableName] " SET " <NameValueList> " WHERE " <Condition>
    //UPDATE marks SET age = 35 WHERE name == 'Simon';
    public StringBuilder update(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        //precheck
        boolean preCheckFlag=preCheck(tokens,returnBuilder);
        if(!preCheckFlag){
            return returnBuilder;
        }
//        if(!tokens.get(tokens.size() - 1).equals(";")){
//            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
//            return returnBuilder;
//        }
        if(tokens.size()<7){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        boolean flag=this.reader.useTable(tokens.get(tokenIndex),storageFolderPath);
        if(!flag){
            returnBuilder.append("[ERROR] Table doesn't exist");
            return returnBuilder;
        }
        tokenIndex+=1;
        if(!tokens.get(tokenIndex).equalsIgnoreCase("SET")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;
        while(!tokens.get(tokenIndex).equalsIgnoreCase("WHERE")){
            //TODO: get <NameValueList>



            tokenIndex+=1;
        }
        tokenIndex+=1;
        //TODO: <condition>



        //TODO: Implement update


//        reader.writeTabFile(table,table.tableFilePath);
//        //reintisualised the database:
//        currentDatabase.updateTable(table);
        return returnBuilder;
    }
}
