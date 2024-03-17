package edu.uob;

import java.util.ArrayList;

public class AlterHandler extends CommandHandler {
    public AlterHandler(String path) {
        super(path);
    }
    //<Alter>::=  "ALTER " "TABLE " [TableName] " " <AlterationType(ADD|DROP)> " " [AttributeName]
    public StringBuilder alter(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        //check last ;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(tokens.size()<6){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        //check the second command->table
        if(!tokens.get(tokenIndex).equalsIgnoreCase("TABLE")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        //check if current database is set.
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        if(currentDatabase==null){
            returnBuilder.append("[ERROR] Hasn't select a database yet!");
            return returnBuilder;
        }

        tokenIndex+=1;//get Tablename
        //
        boolean flag=this.reader.useTable(tokens.get(tokenIndex),storageFolderPath);
        if(!flag){
            returnBuilder.append("[ERROR] Table doesn't exist");
            return returnBuilder;
        }
        Table tableget=currentDatabase.getTable(tokens.get(tokenIndex));
        if(tableget==null){
            returnBuilder.append("ERROR");
            return returnBuilder;
        }
        tokenIndex+=1;
        if(tokens.get(tokenIndex).equalsIgnoreCase("ADD")){
            tokenIndex+=1;//attribute name
            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                System.err.print("invalid name");
                returnBuilder.append("[ERROR]:Invalid name");
                return returnBuilder;
            }
            //attribute name can't be duplicate
            String attributeName=tokens.get(tokenIndex);
            String[] attributeList=tableget.getAttribute().split("\t");
            for (String s : attributeList) {
                if (s.equals(attributeName)) {
                    returnBuilder.append("[ERROR]:Invalid name");
                    return returnBuilder;
                }
            }


            boolean flagAdd=tableget.alterAddTable(attributeName+"\t");
            if(!flagAdd){
                returnBuilder.append("[ERROR] Fail add attribute"+attributeName);
                return returnBuilder;
            }
            currentDatabase.updateTable(tableget);
            reader.writeTabFile(tableget,tableget.tableFilePath);
            returnBuilder.append("[OK]");
        }else if (tokens.get(tokenIndex).equalsIgnoreCase("DROP")){
            tokenIndex+=1;
            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                returnBuilder.append("[ERROR]:Invalid name");
                return returnBuilder;
            }

            String attributeName=tokens.get(tokenIndex);
            if(attributeName.equals("id")){
                returnBuilder.append("[ERROR] can't drop id!");
                return returnBuilder;
            }
            boolean flagDrop=tableget.alterDropTable(attributeName);
            if(!flagDrop){
                returnBuilder.append("[ERROR] Fail drop attribute"+attributeName);
                return returnBuilder;
            }
            //writeTabFile(Table table, String tableFilePath)
            //write back to file
            currentDatabase.updateTable(tableget);
            reader.writeTabFile(tableget,tableget.tableFilePath);
            returnBuilder.append("[OK]");
        }else{
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        return returnBuilder;
    }

}
