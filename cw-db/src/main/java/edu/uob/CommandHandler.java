package edu.uob;

import java.util.ArrayList;
import java.io.File;

public class CommandHandler {
    private final String storageFolderPath;
    public CommandHandler(String path){
        this.storageFolderPath=path;
    }

//"USE " [DatabaseName]
    public StringBuilder use(ArrayList<String> tokens, StringBuilder returnBuilder) {
        //find the database in databases file and set the current to this database.
        if (tokens.size()<2) {
            returnBuilder.append("[ERROR]:Missing DATABASE NAME!");
        } else {
            String secondtoken = tokens.get(1).toLowerCase();
            String databaseFolderPath = storageFolderPath + File.separator + secondtoken;
            File databaseFolder = new File(databaseFolderPath);
            if (databaseFolder.exists() && databaseFolder.isDirectory()) {
                Globalstatus.getInstance().setCurrentDatabase(new Database(secondtoken));
            }
            returnBuilder.append("[OK!]");
        }
        return returnBuilder;
    }

/*<CreateDatabase>::=  "CREATE " "DATABASE " [DatabaseName]
<CreateTable>::= "CREATE " "TABLE " [TableName] | "CREATE " "TABLE " [TableName] "(" <AttributeList> ")"*/
    public StringBuilder create(ArrayList<String> tokens,StringBuilder returnBuilder){
        if(tokens.size()<2){
            //TODO:ERROR:missing DatabaseName.
            returnBuilder.append("[ERROR]:Missing DATABASE/TABLE!");
        }else{
            String secondtoken=tokens.get(1);
            if (secondtoken.equals("DATABASE")){
                String thirdtoken=tokens.get(2);
                Database database=new Database(thirdtoken);
                database.createDatabase(storageFolderPath);
            }else if(secondtoken.equals("TABLE")){
                String thirdtoken=tokens.get(2);
                Table table=new Table();
                Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
                table.createTable(currentDatabase,storageFolderPath,thirdtoken);
                //TODO： deal with attributelist.
                if(tokens.size()>3 && tokens.get(3).equals("(")){

                }


            }
            returnBuilder.append("[OK!]");
        }
        return returnBuilder;
    }
//<Drop>  ::=  "DROP " "DATABASE " [DatabaseName] | "DROP " "TABLE " [TableName]
    public void drop(ArrayList<String> tokens){
        String secondtoken=tokens.get(1);
        if(tokens.size()<2){
        //TODO:ERROR:missing DatabaseName.
        }else{
            if (secondtoken.equals("DATABASE")){

        //TODO:drop a database with name. move the drop methods to DataReader??


            }else if(secondtoken.equals("TABLE")){

            }
        }
    }

// <Insert> ::=  "INSERT " "INTO " [TableName] " VALUES" "(" <ValueList> ")"
    public void insert(ArrayList<String> tokens){
        String secondtoken=tokens.get(1);
        String thirdtoken=tokens.get(2);
        if(secondtoken.equals("INTO")){
            String tablename=thirdtoken;
            Database currentdatabase=Globalstatus.getInstance().getCurrentDatabase();
            Table table=currentdatabase.getTable(thirdtoken);
            //TODO:how to insert VALUES
        }else{
            //TODO:ERROR handling:missing INTO
        }

    }

//<Select>::=  "SELECT " <WildAttribList> " FROM " [TableName] |\
// "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
    public void select(ArrayList<String> tokens){
        String secondtoken=tokens.get(1);
        if(secondtoken.equals("*")){
//TODO * means select everything
        }else{
            //So the wild arrays
            String token;
            ArrayList<String> wild=new ArrayList<>();
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
