package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SelectHandler extends CommandHandler {

    public SelectHandler(String path) {
        super(path);
    }
    //<Select>::=  "SELECT " <WildAttribList> " FROM " [TableName] |\
// "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
    public StringBuilder select(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;

        //precheck
        boolean preCheckFlag=preCheck(tokens,returnBuilder);
        if(!preCheckFlag){
            return returnBuilder;
        }
        if(tokens.size()<4){
            returnBuilder.append("[ERROR] Invalid sentence");
            return returnBuilder;
        }


        Table tempTable=new Table();
        //advanced set table:
        int tempTokenIndex=tokenIndex;
        while(!tokens.get(tempTokenIndex).equalsIgnoreCase("FROM")){
            tempTokenIndex+=1;
        }
        tempTokenIndex+=1;//tableName
        boolean flag=reader.useTable(tokens.get(tempTokenIndex),storageFolderPath);
        if(!flag){
            returnBuilder.append("[ERROR] Can't find Table");
            return returnBuilder;
        }

        boolean flagUseTable;
        Table currentTable= reader.useTableByDatabase((tokens.get(tempTokenIndex)));
        //tempTable=reader.useTableByDatabase((tokens.get(tempTokenIndex)));
        tempTable.name=String.copyValueOf(currentTable.name.toCharArray());
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        reader.readTabFile(currentDatabase,tempTable,storageFolderPath);

        HashSet<String> attributeSet=new HashSet<>();
        for(Rowdata data:tempTable.datas){
            data.selected=true;
        }
        //select attribute
        int tokenAfterSelect=(tokenIndex);
        if(!tokens.get(tokenIndex).equals("*")){
            while(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
                attributeSet.add(tokens.get(tokenIndex));
                tokenIndex+=1;
            }
        }
        tokenIndex=tempTokenIndex+1;
        //WHERE
        if(tokens.get(tokenIndex).equalsIgnoreCase("WHERE")){
            for(Rowdata rowdata:tempTable.datas){
                rowdata.selected=false;
            }
            if(tokens.size()<7){
                returnBuilder.append("[ERROR]Invalid sentence");
                return returnBuilder;
            }
            tokenIndex+=1;
            ArrayList<String> subList = new ArrayList<>(tokens.subList(tokenIndex, tokens.size()));
            //check if it is mark>40 or mark > 40 depends on
            boolean isContinuous=false;
            for(String sub:subList){
                if(condition.comparisonOperators.contains(sub)){
                    isContinuous=true;
                    break;
                }
            }

            Condition.ConditionSelector selectorflag=condition.conditionSelection(subList);
            //returnBuilder.append(subList);
            if(selectorflag == Condition.ConditionSelector.simpleComparison){
                //if it is mark>40 turn it into mark > 40
                if(isContinuous==false){
                    subList=condition.tokenParse(subList);
                }
                if(subList.size()<3){
                    returnBuilder.append("[ERROR] Can't resolve tokenParse");
                    for(String sub:subList){
                        returnBuilder.append(sub+"\t");
                    }
                    return returnBuilder;
                }

                String attribute=subList.get(0);
                int attributeIndex=tempTable.AttributeIndexWithoutID(attribute);
                String oper=subList.get(1);
                String value=subList.get(2);

                if(attribute==null || oper==null || value==null || attributeIndex==-1){
                    returnBuilder.append("[ERROR] Can't deal with the command");
                    return returnBuilder;
                }


                for(int i=0;i<tempTable.datas.size();i++){
                    String[] rowData=tempTable.datas.get(i).getDataSplit();
                    if(condition.comparisonOperator(rowData[attributeIndex],oper,value)){
                        tempTable.datas.get(i).selected=true;
                    }
                }
            }else if(selectorflag== Condition.ConditionSelector.withBool) {
                //this is the index in the command that should be replaced
                boolean flagBool=this.parseBool(subList,tempTable,returnBuilder);
                if(flagBool==false){
                    returnBuilder.append("[ERROR] select ERROR");
                    return returnBuilder;
                }

             }else if (selectorflag== Condition.ConditionSelector.withbrackets) {
                boolean flagBrackets=this.parseBrackets(subList,tempTable,returnBuilder);
                if(flagBrackets==false){
                    returnBuilder.append("[ERROR] select ERROR");
                    return returnBuilder;
                }

            }else{
                returnBuilder.append("[ERROR] Can't resolve condition");
                return returnBuilder;
            }
        }

        //drop notselected colmn:
        //all attributes, select attributes->common retainAll attributeSet
        if(tokens.get(tokenAfterSelect).equals("*")){
            returnBuilder.append("[OK]\n");
            returnBuilder.append(tempTable.getAttribute() + "\n");
            for (Rowdata data : tempTable.datas) {
                if (data.selected == true) {
                    returnBuilder.append(data.getid()+"\t"+data.getData() + "\n");
                }
            }
            return returnBuilder;
        }

        String[] getAttributes=tempTable.getAttribute().split("\t");
        HashSet<String> allAttributes=new HashSet<>();
        allAttributes.addAll(Arrays.asList(getAttributes));
        if(!allAttributes.containsAll(attributeSet)){
            returnBuilder.append("[ERROR]Attribute not exist");
            return returnBuilder;
        }

        allAttributes.removeAll(attributeSet);
        allAttributes.remove("id");
        if(!attributeSet.isEmpty()){
            for (String element : allAttributes){
                boolean dropColmnFlag=tempTable.alterDropTable(element);
                if(!dropColmnFlag){
                    returnBuilder.append("[ERROR]");
                    return returnBuilder;
                }
            }
        }

        //print out to terminal

        returnBuilder.append("[OK]"+"\n");
        if(!attributeSet.contains("id")){
            String[] attributes = tempTable.getAttribute().split("\t");
            ArrayList<String> modifiedAttributes = new ArrayList<>();
            for (String attribute : attributes) {
                if (!attribute.equals("id")) {
                    modifiedAttributes.add(attribute);
                }
            }
            String modifiedAttributeString = String.join("\t", modifiedAttributes);
            returnBuilder.append(modifiedAttributeString + "\n");
        }else if(attributeSet.contains("id")){
            returnBuilder.append(tempTable.getAttribute()+"\n");
        }


//        returnBuilder.append("[OK]"+"\n");
//        returnBuilder.append(tempTable.getAttribute()+"\n");
        for(Rowdata data:tempTable.datas){
            if (attributeSet.contains("id")) {
                if(data.selected==true){
                    returnBuilder.append(data.getid()+"\t"+data.getData()+"\n");
                }
            }else{
                if(data.selected==true){
                    returnBuilder.append(data.getData()+"\n");
                }
            }

        }
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
                    tempTable.datas.get(i).selected = true;
                }
            } else if (oper.equalsIgnoreCase("OR")) {
                if (condition.comparisonOperator(rowData[attributeIndex1], oper1, value1) ||
                        condition.comparisonOperator(rowData[attributeIndex2], oper2, value2)) {
                    tempTable.datas.get(i).selected = true;
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
