package edu.uob;

import java.util.ArrayList;

public class CreateHandler extends CommandHandler{

    public CreateHandler(String path) {
        super(path);
    }
    /*<CreateDatabase>::=  "CREATE " "DATABASE " [DatabaseName] ;
<CreateTable>::= "CREATE " "TABLE " [TableName] | "CREATE " "TABLE " [TableName] "(" <AttributeList> ")"*/
    public StringBuilder create(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        //return in if to avoid these nested if-else.
        if(!tokens.get(tokens.size()-1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(tokens.size()<4){
            returnBuilder.append("[ERROR]:invalid sentence");
            return returnBuilder;
        }
        if (tokens.get(tokenIndex).equalsIgnoreCase("database")){
            tokenIndex+=1;
            //create database

//            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                returnBuilder.append("[ERROR]:Invalid name");
                return returnBuilder;
            }
            Database database=new Database(tokens.get(tokenIndex));
            boolean flag=database.createDatabase(storageFolderPath);
            if (flag){
                returnBuilder.append("[OK]");
            }else{
                returnBuilder.append("[ERROR]:Already exists");
            }
            //but if it already exists???
        } else if(tokens.get(tokenIndex).equalsIgnoreCase("TABLE")){
            createTableHelper(tokens, returnBuilder);
        }else{
            returnBuilder.append("[ERROR]:invalid sentence");
        }
        return returnBuilder;
    }

    private StringBuilder createTableHelper(ArrayList<String> tokens,StringBuilder returnBuilder){
        tokenIndex=2;
        //Hasn't select a database
        if (Globalstatus.getInstance().getCurrentDatabase() == null) {
            returnBuilder.append("[ERROR] Hasn't select a database yet!");
            return returnBuilder;
        } else {
            //can create a table.
            //tokenIndex += 1;//third token
            Table table = new Table();
            Database currentDatabase = Globalstatus.getInstance().getCurrentDatabase();
            //search if the table exist.
            if(currentDatabase.tables.containsKey(tokens.get(tokenIndex))){
                returnBuilder.append("[ERROR] Already exists");
                return returnBuilder;
            }
            //tokenIndex += 1;
            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                returnBuilder.append("[ERROR]:Invalid name");
                return returnBuilder;
            }

            boolean flag;
            flag=table.createTable(currentDatabase, storageFolderPath, tokens.get(tokenIndex));
            if(!flag){
                returnBuilder.append("[ERROR] Already exists");
                return returnBuilder;
            }
            //TODO:test the default
            table.setAttribute("id"+"\t");
            table.addAttributeID(table.getAttribute());

            tokenIndex += 1;//fourth token->CREATE TABLE tablename {;} or CREATE TABLE tablename {(}   );

            //table with attributes
            if (tokens.get(tokenIndex).equals("(")) {
                //check for ( ) in pairs
                if (!tokens.get(tokens.size() - 2).equals(")")) {
                    returnBuilder.append("[ERROR] invalid sentence");
                    return returnBuilder;
                }
                tokenIndex += 1;
                StringBuilder attributes = new StringBuilder();
                //attributes.append("id").append("\t");
                while (!tokens.get(tokenIndex).equals(")")) {
                    if (!tokens.get(tokenIndex).equals(",") && !tokens.get(tokenIndex).equals(" ")) {
                        //attribute: name \t value \t
                        //table.alterAddTable(tokens.get(tokenIndex));
                        attributes.append(tokens.get(tokenIndex)).append("\t");
                    }
                    //attributes.append("\t");
                    tokenIndex += 1;
                }
                String attributeString = attributes.toString();
                table.addAttribute(attributeString);
                table.setAttribute(attributeString);
                //table.addAttributeID(table.getAttribute());
            }
            currentDatabase.updateTable(table);
            reader.writeTabFile(table, table.tableFilePath);
            returnBuilder.append("[OK]");
        }
        return returnBuilder;
    }
}
