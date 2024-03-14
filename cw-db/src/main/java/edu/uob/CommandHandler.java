package edu.uob;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class CommandHandler {
    private final String storageFolderPath;
    private int tokenIndex;
    private DataReader reader;
    private Condition condition;
    public CommandHandler(String path){
        this.storageFolderPath=path;
        //the first token is handled in DBserver.
        this.tokenIndex=1;
        this.reader=new DataReader();
    }
    //TODO: invalid NAME: keywords\etc.
    //TODO: set some private variables.

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
        if (tokens.get(tokenIndex).equalsIgnoreCase("database")){
            tokenIndex+=1;
                //create database

            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                returnBuilder.append("[ERROR]:Invalid name");
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
            returnBuilder=createTableHelper(tokens,returnBuilder);
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
            }

            boolean flag;
            flag=table.createTable(currentDatabase, storageFolderPath, tokens.get(tokenIndex));
            if(flag==false){
                returnBuilder.append("[ERROR] Already exists");
                return returnBuilder;
            }
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
                    if (!tokens.get(tokenIndex).equals(",") && !tokens.get(tokenIndex).equals(" ")) {
                        //attribute: name \t value \t
                        attributes.append(tokens.get(tokenIndex)).append("\t");
                    }
                    //attributes.append("\t");
                    tokenIndex += 1;
                }
                String attributeString = attributes.toString();
                table.addAttribute(attributeString);
            }
            returnBuilder.append("[OK]");
        }
    return returnBuilder;
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
        if (tokens.get(tokenIndex).equalsIgnoreCase("DATABASE")) {
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
        } else if (tokens.get(tokenIndex).equalsIgnoreCase("TABLE")) {
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

    //<Alter>::=  "ALTER " "TABLE " [TableName] " " <AlterationType(ADD|DROP)> " " [AttributeName]
    public StringBuilder alter(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(!tokens.get(tokenIndex).equalsIgnoreCase("TABLE")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;
        boolean flag=this.reader.useTable(tokens.get(tokenIndex),storageFolderPath);
        if(!flag){
            returnBuilder.append("[ERROR] Table doesn't exist");
            return returnBuilder;
        }
        Table table=Globalstatus.getInstance().getCurrentTable();
        tokenIndex+=1;
        if(tokens.get(tokenIndex).equalsIgnoreCase("ADD")){
            tokenIndex+=1;//attribute name
            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                returnBuilder.append("[ERROR]:Invalid name");
            }

            String attributeName=tokens.get(tokenIndex);
            table.alterAddTable(attributeName);
            boolean flagAdd=table.alterAddTable(attributeName);;
            if(!flagAdd){
                returnBuilder.append("[ERROR] Fail add attribute"+attributeName);
                return returnBuilder;
            }
            returnBuilder.append("[OK]");
        }else if (tokens.get(tokenIndex).equalsIgnoreCase("DROP")){
            tokenIndex+=1;
            //TODO:dealing with DROP
            //valid name
            boolean validName=condition.correctName(tokens.get(tokenIndex));
            if (!validName){
                returnBuilder.append("[ERROR]:Invalid name");
            }

            String attributeName=tokens.get(tokenIndex);
            boolean flagDrop=table.alterDropTable(attributeName);
            if(!flagDrop){
                returnBuilder.append("[ERROR] Fail drop attribute"+attributeName);
                return returnBuilder;
            }
            //writeTabFile(Table table, String tableFilePath)
            reader.writeTabFile(table,table.tableFilePath);
            returnBuilder.append("[OK]");
        }else{
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        return returnBuilder;
    }


// <Insert> ::=  "INSERT " "INTO " [TableName] " VALUES" "(" <ValueList> ")"
    public StringBuilder insert(ArrayList<String> tokens,StringBuilder returnBuilder) throws IOException {
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(tokens.get(tokenIndex).equalsIgnoreCase("INTO")){
            tokenIndex+=1;
            //TODO: reader.useTable->this table doesn't exist????
            //useTable(String searchFile,String path);
            //String FilePath = path + File.separator + Globalstatus.getInstance().getCurrentDatabase()
            //                + searchFile + ".tab";
            boolean flag=this.reader.useTable(tokens.get(tokenIndex),storageFolderPath);
            if(!flag){
                returnBuilder.append("[ERROR] Table doesn't exist");
                return returnBuilder;
            }

            Table table=Globalstatus.getInstance().getCurrentTable();
            System.out.print(table.name);
            tokenIndex+=1;
            if(!tokens.get(tokenIndex).equalsIgnoreCase("VALUES")){
                returnBuilder.append("[ERROR] invalid sentence, doesn't have VALUES");
                return returnBuilder;
            }
            tokenIndex+=1;
            //check "( )" in pairs
            if(!tokens.get(tokenIndex).equals("(")|| !tokens.get(tokens.size()-2).equals(")")){
                returnBuilder.append("[ERROR] invalid sentence, brackets not in pairs");
                return returnBuilder;
            }
            tokenIndex+=1;
            StringBuilder valueList=new StringBuilder();
            while(!tokens.get(tokenIndex).equals(")")){
                if(!tokens.get(tokenIndex).equals(",") && !tokens.get(tokenIndex).equals(" ")){
                    valueList.append(tokens.get(tokenIndex)).append("\t");
                }
                tokenIndex+=1;
            }
            String valueData=valueList.toString();
            //TODO:actual insert into
            boolean insertflag=table.insertRow(valueData);
            if(!insertflag){
                returnBuilder.append("[ERROR] insert fail");
                return returnBuilder;
            }
            returnBuilder.append("[OK]");
        }else{
            returnBuilder.append("[ERROR] invalid sentence??????????");
        }
        return returnBuilder;
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
//<Update> ::=  "UPDATE " [TableName] " SET " <NameValueList> " WHERE " <Condition>
    public StringBuilder update(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
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



            return returnBuilder;
    }
//<Delete> ::=  "DELETE " "FROM " [TableName] " WHERE " <Condition>
    public StringBuilder delete(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
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
//<Join>::=  "JOIN " [TableName] " AND " [TableName] " ON " [AttributeName] " AND " [AttributeName]
    public StringBuilder join(ArrayList<String> tokens, StringBuilder returnBuilder){
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        String tableName1=tokens.get(tokenIndex);
        if(!tokens.get(tokenIndex+1).equalsIgnoreCase("AND")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=2;
        String tableName2=tokens.get(tokenIndex);
        Table table1=currentDatabase.getTable(tableName1);
        Table table2=currentDatabase.getTable(tableName2);
        tokenIndex+=1;//ON
        if(!tokens.get(tokenIndex+1).equalsIgnoreCase("ON")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;
        String attribute1=tokens.get(tokenIndex);
        tokenIndex+=1;//AND
        if(!tokens.get(tokenIndex+1).equalsIgnoreCase("AND")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;
        String attribute2=tokens.get(tokenIndex);
        //table1,table2,attribute1,attribute2
        //TODO:print and join
        int latestID=Math.max(table1.getLatestID(),table2.getLatestID());
        boolean table1IDsmaller;
        if(table1.getLatestID()<=table2.getLatestID()){
            table1IDsmaller=true;
        }else{
            table1IDsmaller=false;
        }

        //TODO:print and join: don't understand
        ArrayList<String> printData=new ArrayList<>();
        int colmunIndex1=table1.AttributeIndexWithoutID(attribute1);
        int colmunIndex2=table2.AttributeIndexWithoutID(attribute2);

        for(int i=0;i<=latestID;i++){
            String []wholedata1=table1.datas.get(i).getData().split("\t");
            String []wholedata2=table2.datas.get(i).getData().split("\t");
            String exactData1=wholedata1[colmunIndex1];
            String exactData2=wholedata2[colmunIndex2];
            //returnBuilder
        }



        return returnBuilder;
    }
}
