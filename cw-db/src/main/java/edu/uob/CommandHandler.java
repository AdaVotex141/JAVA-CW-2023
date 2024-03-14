package edu.uob;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.io.File;

public class CommandHandler {
    private final String storageFolderPath;
    private int tokenIndex;
    private DataReader reader;
    public CommandHandler(String path){
        this.storageFolderPath=path;
        //the first token is handled in DBserver.
        this.tokenIndex=1;
        this.reader=new DataReader();
    }

//TESTED:"USE " [DatabaseName] ;
    public StringBuilder use(ArrayList<String> tokens, StringBuilder returnBuilder) {
        tokenIndex=1;
        //find the database in databases file and set the current to this database.
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR] Missing ';' at the end of the sentence");
            return returnBuilder;
        }else{
            if (tokens.size()!=3) {
                returnBuilder.append("[ERROR] invalid sentence!");
            } else {
                String secondtoken = tokens.get(tokenIndex).toLowerCase();
                String databaseFolderPath = storageFolderPath + File.separator + secondtoken;
                File databaseFolder = new File(databaseFolderPath);
                if (databaseFolder.exists() && databaseFolder.isDirectory()) {
                    //set the currentDatabase to this database
                    //read current files in the database and generate a hashmap for it
                    this.reader.useDatabase(secondtoken,storageFolderPath);
                    returnBuilder.append("[OK]");
                }else{
                    returnBuilder.append("[ERROR]:This Database doesn't exist!");
                }
            }
        }
        return returnBuilder;
    }

/*<CreateDatabase>::=  "CREATE " "DATABASE " [DatabaseName] ;
<CreateTable>::= "CREATE " "TABLE " [TableName] | "CREATE " "TABLE " [TableName] "(" <AttributeList> ")"*/
    public StringBuilder create(ArrayList<String> tokens,StringBuilder returnBuilder){
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
        if (tokens.get(tokenIndex).equals("DATABASE")){
                tokenIndex+=1;
                //create database
                Database database=new Database(tokens.get(tokenIndex));
                boolean flag=database.createDatabase(storageFolderPath);
                if (flag==true){
                    returnBuilder.append("[OK]");
                }else{
                    returnBuilder.append("[ERROR]:Already exists");
                }
                //but if it already exists???
            } else{
            returnBuilder=createTableHelper(tokens,returnBuilder);
        }
        return returnBuilder;
    }

    private StringBuilder createTableHelper(ArrayList<String> tokens,StringBuilder returnBuilder){
        tokenIndex=2;
        if (tokens.get(tokenIndex).equals("TABLE")) {
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
                tokenIndex += 1;
                table.createTable(currentDatabase, storageFolderPath, tokens.get(tokenIndex));
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
                    while (!tokens.get(tokenIndex).equals(")")) {
                        if (!tokens.get(tokenIndex).equals(",")) {
                            //attribute: name \t value \t
                            attributes.append(tokens.get(tokenIndex) + "\t");
                        }
                        tokenIndex += 1;
                    }
                    String attributeString = attributes.toString();
                    table.addAttribute(attributeString);
                    returnBuilder.append("[OK]");
                }
            }
        }return returnBuilder;
    }


//<Drop>  ::=  "DROP " "DATABASE " [DatabaseName] | "DROP " "TABLE " [TableName]
public StringBuilder drop(ArrayList<String> tokens, StringBuilder returnBuilder) {
    tokenIndex=1;
    if(!tokens.get(tokens.size() - 1).equals(";")){
        returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
        return returnBuilder;
    }
    if(tokens.size()!=4){
        returnBuilder.append("[ERROR]:invalid sentence");
        return returnBuilder;
    }
        //tokenIndex+=1;//check keyword DATABASE or TABLE
    {
        if (tokens.get(tokenIndex).equals("DATABASE")) {
            //System.out.print("reach database！");
            tokenIndex += 1;
            String DatabaseName = tokens.get(tokenIndex);
            String DatabasePath = storageFolderPath + File.separator + DatabaseName;
            File databaseToDelete = new File(DatabasePath);
            Database database = new Database(DatabaseName);
            Database currentDatabase = Globalstatus.getInstance().getCurrentDatabase();
            if (databaseToDelete.exists() && databaseToDelete.isDirectory()) {
                if (currentDatabase != null && database.isSameFolder(currentDatabase)) {
                    Globalstatus.getInstance().setCurrentDatabase(null);
                }
                if (database.dropDatabase(databaseToDelete)) {
                    returnBuilder.append("[OK]");
                } else {
                    returnBuilder.append("[ERROR] Failed to drop database");
                }
            } else {
                returnBuilder.append("[ERROR] Database does not exist or is not a directory");
            }
        } else if (tokens.get(tokenIndex).equals("TABLE")) {
            //System.out.print(" reach table！");
            tokenIndex += 1;
            String TableName = tokens.get(tokenIndex);
            Database currentDatabase = Globalstatus.getInstance().getCurrentDatabase();
            if (currentDatabase == null) {
                //tests pass!
                returnBuilder.append("[ERROR] Hasn't selected a Database yet!");
                return returnBuilder;
            }
            Table table = currentDatabase.getTable(TableName);
            if (table == null) {
                returnBuilder.append("[ERROR] Can't find this table in the current Database!");
            } else {
                // Drop the table
                table.dropTable(currentDatabase);
                returnBuilder.append("[OK]");
            }
        }else{
            //the second token is neither DATABASE nor TABLE
            returnBuilder.append("[ERROR] Invalid sentence");
        }
    }
    return returnBuilder;
    }




// <Insert> ::=  "INSERT " "INTO " [TableName] " VALUES" "(" <ValueList> ")"
    public StringBuilder insert(ArrayList<String> tokens,StringBuilder returnBuilder){
        tokenIndex+=1;
        if(tokens.get(tokenIndex).equals("INTO")){
            tokenIndex+=1;
            Database currentdatabase=Globalstatus.getInstance().getCurrentDatabase();
            Table table=currentdatabase.getTable(tokens.get(tokenIndex));
            Globalstatus.getInstance().setCurrentTable(table);
            //TODO:how to insert VALUES
        }else{
            returnBuilder.append("[ERROR] Missing 'INTO'");
        }
        return returnBuilder;
    }

//<Select>::=  "SELECT " <WildAttribList> " FROM " [TableName] |\
// "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
    public void select(ArrayList<String> tokens){
        String secondtoken=tokens.get(1);
        if(tokens.get(tokenIndex).equals("*")){
                //TODO * means select everything
        }else{
                //TODO ranging

        }

        //TODO if there are WHERE condition

    }
//<Update> ::=  "UPDATE " [TableName] " SET " <NameValueList> " WHERE " <Condition>
    public void update(ArrayList<String> tokens){

    }
//<Alter>::=  "ALTER " "TABLE " [TableName] " " <AlterationType(ADD|DROP)> " " [AttributeName]
    public void alter(ArrayList<String> tokens){


    }
//<Delete> ::=  "DELETE " "FROM " [TableName] " WHERE " <Condition>
    public void delete(ArrayList<String> tokens){

    }
//<Join>::=  "JOIN " [TableName] " AND " [TableName] " ON " [AttributeName] " AND " [AttributeName]
    public void join(ArrayList<String> tokens){

    }


}
