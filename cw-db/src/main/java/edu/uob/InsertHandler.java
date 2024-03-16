package edu.uob;

import java.io.IOException;
import java.util.ArrayList;

public class InsertHandler extends CommandHandler{
    public InsertHandler(String path) {
        super(path);
    }
    // <Insert> ::=  "INSERT " "INTO " [TableName] " VALUES" "(" <ValueList> ")"
    public StringBuilder insert(ArrayList<String> tokens, StringBuilder returnBuilder) throws IOException {
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(tokens.size()<8){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        if(currentDatabase==null){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        if(tokens.get(tokenIndex).equalsIgnoreCase("INTO")){
            tokenIndex+=1;
            //TODO: reader.useTable->this table doesn't exist????
            //useTable(String searchFile,String path);
            //String FilePath = path + File.separator + Globalstatus.getInstance().getCurrentDatabase()
            //                + searchFile + ".tab";

            Table table=this.reader.useTableByDatabase(tokens.get(tokenIndex));
//            if(!flag){
//                returnBuilder.append("[ERROR] Table doesn't exist");
//                return returnBuilder;
//            }

            //Table table=Globalstatus.getInstance().getCurrentTable();
            //System.out.print(table.name);
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
            if(table==null){
                returnBuilder.append("[ERROR] hasn't assigned attribute yet!");
                return returnBuilder;
            }
            //TODO:actual insert into
            boolean insertflag=table.insertRow(valueData);
            if(!insertflag){
                returnBuilder.append("[ERROR] insert fail");
                return returnBuilder;
            }
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);
            returnBuilder.append("[OK]");
        }else{
            returnBuilder.append("[ERROR] invalid sentence??????????");
        }
        return returnBuilder;
    }

}
