package edu.uob;

import java.util.ArrayList;

public class JoinHandler extends CommandHandler{
    public JoinHandler(String path) {
        super(path);
    }
    //<Join>::=  "JOIN " [TableName] " AND " [TableName] " ON " [AttributeName] " AND " [AttributeName]
    public StringBuilder join(ArrayList<String> tokens, StringBuilder returnBuilder){
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
        if(tokens.size()<9){
            returnBuilder.append("[ERROR]");
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
