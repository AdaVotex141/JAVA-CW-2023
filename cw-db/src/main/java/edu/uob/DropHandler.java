package edu.uob;

import java.io.File;
import java.util.ArrayList;

public class DropHandler extends CommandHandler {
    public DropHandler(String path) {
        super(path);
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
                        currentDatabase.tables=null;
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

                //precheck
//                boolean preCheckFlag=preCheck(tokens,returnBuilder);
//                if(!preCheckFlag){
//                    return returnBuilder;
//                }



                String TableName = tokens.get(tokenIndex).toLowerCase();
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
}
