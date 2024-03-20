package edu.uob;

import javax.annotation.processing.SupportedOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class UpdateHandler extends CommandHandler{
    public UpdateHandler(String path) {
        super(path);
    }
    //<Update> ::=  "UPDATE " [TableName] " SET " <NameValueList> " WHERE " <Condition>
    //UPDATE marks SET age = 35 WHERE name == 'Simon';
    public StringBuilder update(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        //precheck
        boolean preCheckFlag=preCheck(tokens,returnBuilder);
        if(!preCheckFlag){
            return returnBuilder;
        }
        if(tokens.size()<7){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        boolean flag=this.reader.useTable(tokens.get(tokenIndex),storageFolderPath);
        if(!flag){
            returnBuilder.append("[ERROR] Table doesn't exist");
            return returnBuilder;
        }
        //Table table=Globalstatus.getInstance().getCurrentTable();
        //Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        Table table =reader.useTableByDatabase(tokens.get(tokenIndex));
        tokenIndex+=1;//set
        if(!tokens.get(tokenIndex).equalsIgnoreCase("SET")){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;//name value list
        //use hashmap to store pair data
        HashMap<Integer,String> updateList=new HashMap<>();

        String[] getAttributes=table.getAttribute().split("\t");
        HashSet<String> allAttributes=new HashSet<>();
        allAttributes.addAll(Arrays.asList(getAttributes));
        while(!tokens.get(tokenIndex).equalsIgnoreCase("WHERE")){
            //TODO: get <NameValueList>
            if (!tokens.get(tokenIndex).equals("id")) {
                returnBuilder.append("[ERROR] Can't update ID!");
                return returnBuilder;
            }
            if(allAttributes.contains(tokens.get(tokenIndex))){
                int attributeID=table.AttributeIndexWithoutID(tokens.get(tokenIndex));
                String dataString=tokens.get(tokenIndex+2);
                updateList.put(attributeID,dataString);
            }
            tokenIndex+=1;
        }
        tokenIndex+=1;//where+1
        //TODO: <condition>
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        ArrayList<String> subList = new ArrayList<>(tokens.subList(tokenIndex, tokens.size()));
        Condition.ConditionSelector selectorflag=condition.conditionSelection(subList);
        //simplist definition
        if(selectorflag == Condition.ConditionSelector.simpleComparison){
            String attribute=subList.get(0);
            int attributeIndex=table.AttributeIndexWithoutID(attribute);
            String oper=subList.get(1);
            String value=subList.get(2);

            if(attributeIndex==-1){

                returnBuilder.append(attribute+oper+value);
                returnBuilder.append("[ERROR]");
                return returnBuilder;
            }


            for(int i=0;i<table.datas.size();i++){
                String[] rowData=table.datas.get(i).getDataSplit();
                if(condition.comparisonOperator(rowData[attributeIndex],oper,value)){
                    for (Integer key : updateList.keySet()){
                        rowData[key]=updateList.get(key);
                    }
                }
                table.datas.set(i, new Rowdata(table.datas.get(i).getid(), String.join("\t", rowData)));
            }
            returnBuilder.append("[OK]");
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);

        }else if(selectorflag== Condition.ConditionSelector.withBool){

        } else if (selectorflag== Condition.ConditionSelector.withbrackets) {




        }else{
            returnBuilder.append("[ERROR] Can't resolve condition");
            return returnBuilder;
        }
//        reader.writeTabFile(table,table.tableFilePath);
//        //reintisualised the database:
//        currentDatabase.updateTable(table);
        return returnBuilder;
    }
}
