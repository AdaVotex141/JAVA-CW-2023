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

        //returnBuilder.append(tokens.get(tokenIndex));

        if(!tokens.get(tokenIndex+1).equalsIgnoreCase("AND")){
            returnBuilder.append("[ERROR] Invalid sentence, not AND");
            return returnBuilder;
        }
        tokenIndex+=2;
        String tableName2=tokens.get(tokenIndex);
        //returnBuilder.append(tokens.get(tokenIndex));

        //get the two table
        Table table1=currentDatabase.getTable(tableName1);
        Table table2=currentDatabase.getTable(tableName2);

        tokenIndex+=1;//ON
        if(!tokens.get(tokenIndex).equalsIgnoreCase("ON")){
            returnBuilder.append("[ERROR] Invalid sentence, not ON");
            return returnBuilder;
        }

        //returnBuilder.append(tokens.get(tokenIndex));

        tokenIndex+=1;
        String attribute1=tokens.get(tokenIndex);
        tokenIndex+=1;//AND
        if(!tokens.get(tokenIndex).equalsIgnoreCase("AND")){
            returnBuilder.append("[ERROR] Invalid sentence, not AND");
            return returnBuilder;
        }
        tokenIndex+=1;
        String attribute2=tokens.get(tokenIndex);
        //table1,table2,attribute1,attribute2
        //print the first line

        //returnBuilder.append(attribute1+"\t"+attribute2);


        returnBuilder=PrintFirstLine(table1, table2, attribute1, attribute2, returnBuilder);

        //start to search through the whole
        int index1=table1.AttributeIndexWithoutID(tableName1);
        int index2=table2.AttributeIndexWithoutID(tableName2);
        //Table table1, int index1, Table table2, int index2,StringBuilder returnBuilder)
        returnBuilder=printTheSelected(table1,index1,table2,index2,returnBuilder);


        //TODO:print and join: don't understand
//        ArrayList<String> printData=new ArrayList<>();
//        int colmunIndex1=table1.AttributeIndexWithoutID(attribute1);
//        int colmunIndex2=table2.AttributeIndexWithoutID(attribute2);
//
//        for(int i=0;i<=latestID;i++){
//            String []wholedata1=table1.datas.get(i).getData().split("\t");
//            String []wholedata2=table2.datas.get(i).getData().split("\t");
//            String exactData1=wholedata1[colmunIndex1];
//            String exactData2=whole-data2[colmunIndex2];
//            //returnBuilder
//        }



        return returnBuilder;
    }

    private StringBuilder PrintFirstLine(Table table1, Table table2, String attribute1, String attribute2, StringBuilder returnBuilder){
        String[] table1Attribute=table1.getAttribute().split("\t");
        String[] table2Attribute=table2.getAttribute().split("\t");
        returnBuilder.append("id"+"\t");
        for(String attribute:table1Attribute){
            if (!attribute.equals("id") && !attribute.equals(attribute1)){
                returnBuilder.append(table1.name+"."+attribute+"\t");
            }else if(attribute.equals(attribute1)){
                for(String attributeElement:table2Attribute){
                    if(!attributeElement.equals("id") && !attributeElement.equals(attribute2)){
                        returnBuilder.append(table2.name+"."+attributeElement+"\t");
                    }
                }
            }
        }
        returnBuilder.append("\n");
        return returnBuilder;
    }
    private StringBuilder printTheSelected(Table table1, int index1, Table table2, int index2,StringBuilder returnBuilder){
        int id=1;
        for(int i=0;i<table1.datas.size();i++){
            returnBuilder.append(id+"\t");
            //set Row data -> OXO 3
            String[] dataTable1=table1.datas.get(i).getDataSplit();
            for(int j=0;j< dataTable1.length;j++){
                if(j!=index1){
                    returnBuilder.append(dataTable1[j]+"\t");

                }else if(j==index1){
                    String findDataTable1=dataTable1[j];
                    //into table 2, find if data in table 2 matches
                    for(int z=0;z<table2.datas.size();z++){
                        String[] dataTable2=table2.datas.get(z).getDataSplit();
                        if(dataTable2[index2].equals(findDataTable1)){
                            for(int a=0;a< dataTable2.length;a++){
                                if(a!=index2){
                                    returnBuilder.append(dataTable2[a]+"\t");
                                }
                            }
                        }

                    }
                }
            }
            id+=1;
            returnBuilder.append("\n");
        }




        return returnBuilder;
    }
}
