package edu.uob;

import javax.annotation.processing.SupportedOptions;
import javax.xml.crypto.Data;
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
            returnBuilder.append("[ERROR]Invalid Sentence");
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

        HashSet<Character> OperListChar=new HashSet<>();
        String[] getAttributes=table.getAttribute().split("\t");
        HashSet<String> allAttributes=new HashSet<>();
        allAttributes.addAll(Arrays.asList(getAttributes));
        //select
        boolean isContinuousSET=false;
        for (int i=0; i<tokens.get(tokenIndex).length(); i++) {
            char ch = tokens.get(tokenIndex).charAt(i);
            if(ch==('=')){
                isContinuousSET=true;
                break;
            }
        }//if this flag==true, means that the next pass=true->pass = true
        if(isContinuousSET==true){
            int index = tokens.get(tokenIndex).indexOf('=');
            if (index !=-1) {
                String attributeName = tokens.get(tokenIndex).substring(0, index).trim();
                String value = tokens.get(tokenIndex).substring(index + 1).trim();
                if(attributeName.equals("id")){
                    returnBuilder.append("[ERROR] Can't update ID!");
                    return returnBuilder;
                }
                if(allAttributes.contains(attributeName)){
                    int attributeID=table.AttributeIndexWithoutID(attributeName);
                    String dataString=value;
                    updateList.put(attributeID,dataString);
                }
            }
            tokenIndex+=1;//where
        }else if(isContinuousSET==false){
            while(!tokens.get(tokenIndex).equalsIgnoreCase("WHERE")){
                //TODO: get <NameValueList>
                if (tokens.get(tokenIndex).equals("id")) {
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
        }

        if(updateList==null){
            returnBuilder.append("[ERROR] Attribute doesn't exist");
            return returnBuilder;
        }
        tokenIndex+=1;//where+1

        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        ArrayList<String> subList = new ArrayList<>(tokens.subList(tokenIndex, tokens.size()));

        boolean isContinuous=false;
        for(String sub:subList){
            if(condition.comparisonOperators.contains(sub)){
                isContinuous=true;
                break;
            }
        }

        Condition.ConditionSelector selectorflag=condition.conditionSelection(subList);
        //simplist definition
        if(selectorflag == Condition.ConditionSelector.simpleComparison){
            if(isContinuous==false){
                subList=condition.tokenParse(subList);
            }

            String attribute=subList.get(0);
            int attributeIndex=table.AttributeIndexWithoutID(attribute);
            String oper=subList.get(1);
            String value=subList.get(2);

            if(attribute==null || oper==null || value==null || attributeIndex==-1){
                returnBuilder.append("[ERROR] Can't deal with the command");
                return returnBuilder;
            }


            for(int i=0;i<table.datas.size();i++){
                ArrayList<String> rowData = new ArrayList<>(Arrays.asList(table.datas.get(i).getDataSplit()));
                if(condition.comparisonOperator(rowData.get(attributeIndex),oper,value)){
                    for (Integer key : updateList.keySet()){
                        if(key>rowData.size()-1){
                            rowData.add(updateList.get(key));
                        }else {
                            rowData.set(key, updateList.get(key));
                        }
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
            boolean flagBool=this.parseBool(subList,table,returnBuilder,updateList);
            if(flagBool==false){
                returnBuilder.append("[ERROR] select ERROR");
                return returnBuilder;
            }
            returnBuilder.append("[OK]");
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);
            return returnBuilder;
        } else if (selectorflag== Condition.ConditionSelector.withbrackets) {
            boolean flagBrackets=this.parseBrackets(subList,table,returnBuilder,updateList);
            if(flagBrackets==false){
                returnBuilder.append("[ERROR] select ERROR");
                return returnBuilder;
            }
            returnBuilder.append("[OK]");
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);
            return returnBuilder;
        }else{
            returnBuilder.append("[ERROR] Can't resolve condition");
            return returnBuilder;
        }
//        reader.writeTabFile(table,table.tableFilePath);
//        //reintisualised the database:
//        currentDatabase.updateTable(table);
        return returnBuilder;
    }

    private boolean parseBool(ArrayList<String> subList, Table tempTable, StringBuilder returnBuilder,HashMap<Integer,String> updateList) {
        ArrayList<String> sublist1 = new ArrayList<>();
        ArrayList<String> sublist2 = new ArrayList<>();
        int index = 0;
        while(!subList.get(index).equalsIgnoreCase("AND") && !subList.get(index).equalsIgnoreCase("OR")) {
            sublist1.add(subList.get(index));
            index += 1;
        }
        if(index>=subList.size()-1) {
            return false;
        }
        String oper=subList.get(index);
        index += 1;
        while(index < subList.size() && !subList.get(index).equalsIgnoreCase(";")) {
            sublist2.add(subList.get(index));
            index += 1;
        }
        if(!oper.equalsIgnoreCase("AND") && !oper.equalsIgnoreCase("OR")) {
            returnBuilder.append("[ERROR] Invalid boolean operator");
            return false;
        }
        if(!parseSubList(sublist1, tempTable, returnBuilder) || !parseSubList(sublist2, tempTable, returnBuilder)) {
            return false;
        }
        boolean isContinuous1=false;
        boolean isContinuous2=false;

        for(String sub:sublist1){
            if (condition.comparisonOperators.contains(sub)) {
                isContinuous1=true;
                break;
            }
        }
        if(!isContinuous1){
            sublist1=condition.tokenParse(sublist1);
        }
        for(String sub:sublist2){
            if (condition.comparisonOperators.contains(sub)) {
                isContinuous2 = true;
                break;
            }
        }
        if(!isContinuous2){
            sublist2=condition.tokenParse(sublist2);
        }
        String attribute1 = sublist1.get(0);
        int attributeIndex1 = tempTable.AttributeIndexWithoutID(attribute1);
        String oper1 = sublist1.get(1);
        String value1 = sublist1.get(2);

        String attribute2 = sublist2.get(0);
        int attributeIndex2 = tempTable.AttributeIndexWithoutID(attribute2);
        String oper2 = sublist2.get(1);
        String value2 = sublist2.get(2);

        if (attributeIndex1 == -1 || attributeIndex2 == -1) {
            return false;
        }

        if (!condition.comparisonOperators.contains(oper1) || !condition.comparisonOperators.contains(oper2)) {
            return false;
        }

        for (int i = 0; i < tempTable.datas.size(); i++) {
            //String[] rowData = tempTable.datas.get(i).getDataSplit();
            ArrayList<String> rowData = new ArrayList<>(Arrays.asList(tempTable.datas.get(i).getDataSplit()));
            if (oper.equalsIgnoreCase("AND")) {
                if (condition.comparisonOperator(rowData.get(attributeIndex1), oper1, value1) &&
                        condition.comparisonOperator(rowData.get(attributeIndex2), oper2, value2)) {

                    for (Integer key : updateList.keySet()){
                        if(key>rowData.size()-1){
                            rowData.add(updateList.get(key));
                        }else {
                            rowData.set(key, updateList.get(key));
                        }
                    }
                }
                tempTable.datas.set(i, new Rowdata(tempTable.datas.get(i).getid(), String.join("\t", rowData)));

            } else if (oper.equalsIgnoreCase("OR")) {
                if (condition.comparisonOperator(rowData.get(attributeIndex1), oper1, value1) ||
                        condition.comparisonOperator(rowData.get(attributeIndex2), oper2, value2)) {

                    for (Integer key : updateList.keySet()){
                        if(key>rowData.size()-1){
                            rowData.add(updateList.get(key));
                        }else {
                            rowData.set(key, updateList.get(key));
                        }
                    }
                }
                tempTable.datas.set(i, new Rowdata(tempTable.datas.get(i).getid(), String.join("\t", rowData)));
            }
        }

        //write back to file
        reader.writeTabFile(tempTable,tempTable.tableFilePath);
        //reintisualised the database:
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        currentDatabase.updateTable(tempTable);
        return true;
    }

    private boolean parseSubList(ArrayList<String> subList, Table tempTable, StringBuilder returnBuilder) {
        boolean isContinuous = false;
        for (String sub : subList) {
            if (condition.comparisonOperators.contains(sub)) {
                isContinuous = true;
                break;
            }
        }
        if (!isContinuous) {
            subList = condition.tokenParse(subList);
        }
        return true;
    }

    private boolean parseBrackets(ArrayList<String> list, Table tempTable,
                                  StringBuilder returnBuilder,HashMap<Integer,String> update){
        //get the sublist without leftbrackes and right brackets
        ArrayList<String> sublist=new ArrayList<>();
        for(String element:list){
            if(!element.equals("(") && !element.equals(")")){
                sublist.add(element);
            }
        }
        boolean flag;
        flag=parseBool(sublist,tempTable,returnBuilder,update);
        if(!flag){
            return false;
        }
        return true;
    }


}
