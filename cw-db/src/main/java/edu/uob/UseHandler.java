package edu.uob;

import java.io.File;
import java.util.ArrayList;

public class UseHandler extends CommandHandler{

    public UseHandler(String path) {
        super(path);
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
                    //Database currentDatabase=new Database(secondtoken);
                    //set the currentDatabase
                    //Globalstatus.getInstance().setCurrentDatabase(currentDatabase);
                    returnBuilder.append("[OK]");
                }else{
                    returnBuilder.append("[ERROR]:This Database doesn't exist!");
                }
            }
        }
        return returnBuilder;
    }
}
