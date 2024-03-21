package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;

public class DeleteHandler extends CommandHandler{
    public DeleteHandler(String path) {
        super(path);
    }
    //<Delete> ::=  "DELETE " "FROM " [TableName] " WHERE " <Condition>
    //DELETE FROM marks WHERE mark<40;
    //flag->false writeTabFile(!data.flag)->write back to the file with flags
    public StringBuilder delete(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        boolean preCheckFlag=preCheck(tokens,returnBuilder);
        if(!preCheckFlag){
            return returnBuilder;
        }

//        if(!tokens.get(tokens.size() - 1).equals(";")){
//            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
//            return returnBuilder;
//        }
        if(tokens.size()<6){
            returnBuilder.append("[ERROR]");
            return returnBuilder;
        }
        if(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
            returnBuilder.append("[ERROR]:Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;//table name
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        Table table=this.reader.useTableByDatabase(tokens.get(tokenIndex));

        tokenIndex+=1;//where
        if(!tokens.get(tokenIndex).equalsIgnoreCase("WHERE")){
            returnBuilder.append("[ERROR]:Invalid sentence");
            return returnBuilder;
        }
        tokenIndex+=1;
        //TODO: add this here and it actually works if regenerate a DBserver?
        reader.writeTabFile(table,table.tableFilePath);
        currentDatabase.updateTable(table);

        ArrayList<String> subList = new ArrayList<>(tokens.subList(tokenIndex, tokens.size()));
        Condition.ConditionSelector selectorflag=condition.conditionSelection(subList);
        boolean isContinuous=false;
        for(String sub:subList){
            if(condition.comparisonOperators.contains(sub)){
                isContinuous=true;
                break;
            }
        }



        //simplist definition
        if(selectorflag == Condition.ConditionSelector.simpleComparison){

            returnBuilder=simpleComparison(subList,returnBuilder,table,isContinuous);
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);

        }else if(selectorflag== Condition.ConditionSelector.withBool){
            boolean flagBool=this.parseBool(subList,table,returnBuilder);
            if(flagBool==false){
                returnBuilder.append("[ERROR] Delete ERROR");
                return returnBuilder;
            }
            returnBuilder.append("[OK]");
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);
            return returnBuilder;

        } else if (selectorflag== Condition.ConditionSelector.withbrackets) {
            //TODO: re-tokenrise
            boolean flagBrackets=this.parseBrackets(subList,table,returnBuilder);
            if(flagBrackets==false){
                returnBuilder.append("[ERROR] Delete ERROR");
                return returnBuilder;
            }
            returnBuilder.append("[OK]");
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);
            return returnBuilder;
        }else{
            returnBuilder.append("[ERROR] Can't resolve condition");
            return returnBuilder;
        }
        return returnBuilder;
    }
    private StringBuilder simpleComparison(ArrayList<String> subList, StringBuilder returnBuilder,Table table,boolean flag){
        if(flag==false){
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
            String[] rowData=table.datas.get(i).getDataSplit();
            if(condition.comparisonOperator(rowData[attributeIndex],oper,value)){
                table.datas.get(i).flag=false;
            }
        }
        returnBuilder.append("[OK]");
        return returnBuilder;
    }
    private StringBuilder withBool(ArrayList<String> subList, StringBuilder returnBuilder,Table table){
        String attribute=subList.get(0);
        int attributeIndex1=table.AttributeIndexWithoutID(attribute);
        int subIndex=0;
        //logic??
        while(!subList.get(subIndex).equalsIgnoreCase("AND") && !subList.get(tokenIndex).equalsIgnoreCase("OR")){
            subIndex++;
        }
        subIndex++;//one token after AND/OR
        String attribute2=subList.get(subIndex);
        int attributeIndex2=table.AttributeIndexWithoutID(attribute2);

        //replace the String command
        for(int i=0;i<table.datas.size();i++){
            String[] rowData=table.datas.get(i).getDataSplit();
            subList.get(0).replace(attribute,rowData[attributeIndex1]);
            subList.get(subIndex).replace(attribute2,rowData[attributeIndex2]);

            for(int j=0;j<subList.size();j++){
                returnBuilder.append(subList.get(j)+"\t");
            }

            if(condition.boolParser(subList)){
                table.datas.get(i).flag=false;
            }
        }
        returnBuilder.append("[OK]");
        return returnBuilder;
    }

    private boolean parseBool(ArrayList<String> subList, Table tempTable, StringBuilder returnBuilder) {
        ArrayList<String> sublist1 = new ArrayList<>();
        ArrayList<String> sublist2 = new ArrayList<>();
        int index = 0;


        while (!subList.get(index).equalsIgnoreCase("AND") && !subList.get(index).equalsIgnoreCase("OR")) {
            sublist1.add(subList.get(index));
            index += 1;
        }
        if (index >= subList.size()-1) {
            return false;
        }

        String oper = subList.get(index);
        index += 1;
        while (index < subList.size() && !subList.get(index).equalsIgnoreCase(";")) {
            sublist2.add(subList.get(index));
            index += 1;
        }

        if (!oper.equalsIgnoreCase("AND") && !oper.equalsIgnoreCase("OR")) {
            returnBuilder.append("[ERROR] Invalid boolean operator");
            return false;
        }

        if (!parseSubList(sublist1, tempTable, returnBuilder) || !parseSubList(sublist2, tempTable, returnBuilder)) {
            return false;
        }

        boolean isContinuous1 = false;
        boolean isContinuous2 = false;

        for (String sub : sublist1) {
            if (condition.comparisonOperators.contains(sub)) {
                isContinuous1 = true;
                break;
            }
        }
        if (!isContinuous1) {
            sublist1 = condition.tokenParse(sublist1);
        }
        for (String sub : sublist2) {
            if (condition.comparisonOperators.contains(sub)) {
                isContinuous2 = true;
                break;
            }
        }
        if (!isContinuous2) {
            sublist2 = condition.tokenParse(sublist2);
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
            String[] rowData = tempTable.datas.get(i).getDataSplit();
            if (oper.equalsIgnoreCase("AND")) {
                if (condition.comparisonOperator(rowData[attributeIndex1], oper1, value1) &&
                        condition.comparisonOperator(rowData[attributeIndex2], oper2, value2)) {
                    tempTable.datas.get(i).flag = false;
                }
            } else if (oper.equalsIgnoreCase("OR")) {
                if (condition.comparisonOperator(rowData[attributeIndex1], oper1, value1) ||
                        condition.comparisonOperator(rowData[attributeIndex2], oper2, value2)) {
                    tempTable.datas.get(i).flag = false;
                }
            }
        }
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

    private boolean parseBrackets(ArrayList<String> list, Table tempTable, StringBuilder returnBuilder){
        //get the sublist without leftbrackes and right brackets
        ArrayList<String> sublist=new ArrayList<>();
        for(String element:list){
            if(!element.equals("(") && !element.equals(")")){
                sublist.add(element);
            }
        }
        boolean flag;
        flag=parseBool(sublist,tempTable,returnBuilder);
        if(!flag){
            return false;
        }
        return true;
    }


}
