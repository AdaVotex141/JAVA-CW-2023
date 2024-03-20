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
            returnBuilder.append("[ERROR]");
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
        if(!tokens.get(tokenIndex).equals("*")){
            while(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
                attributeSet.add(tokens.get(tokenIndex));
                tokenIndex+=1;
            }
        }
        tokenIndex=tempTokenIndex+1;
        //TODO very complicated->move to delete instead
        //WHERE
        if(tokens.get(tokenIndex).equalsIgnoreCase("WHERE")){
            for(Rowdata rowdata:tempTable.datas){
                rowdata.selected=false;
            }
            //TODO:DEAL with WHERE
            if(tokens.size()<9){
                returnBuilder.append("[ERROR]");
                return returnBuilder;
            }
            tokenIndex+=1;
            ArrayList<String> subList = new ArrayList<>(tokens.subList(tokenIndex, tokens.size()));
            Condition.ConditionSelector selectorflag=condition.conditionSelection(subList);
            returnBuilder.append(subList);
            if(selectorflag == Condition.ConditionSelector.simpleComparison){
                String attribute=subList.get(0);
                if(attribute==null){
                    returnBuilder.append("[ERROR]");
                    return returnBuilder;
                }
                int attributeIndex=tempTable.AttributeIndexWithoutID(attribute);
                String oper=subList.get(1);
                String value=subList.get(2);

                for(int i=0;i<tempTable.datas.size();i++){
                    String[] rowData=tempTable.datas.get(i).getDataSplit();
                    if(condition.comparisonOperator(rowData[attributeIndex],oper,value)){
                        tempTable.datas.get(i).selected=true;
                    }
                }
            }else if(selectorflag== Condition.ConditionSelector.withBool){
                //this is the index in the command that should be replaced
                ArrayList<Integer> dataIndex=condition.dataIndex;
                for(Rowdata data: tempTable.datas){
                    String[] rowData=data.getDataSplit();



                }

            } else if (selectorflag== Condition.ConditionSelector.withbrackets) {

            }else{
                returnBuilder.append("[ERROR] Can't resolve condition");
                return returnBuilder;
            }
        }

        //drop notselected colmn:
        //all attributes, select attributes->common retainAll attributeSet
        String[] getAttributes=tempTable.getAttribute().split("\t");
        HashSet<String> allAttributes=new HashSet<>();
        allAttributes.addAll(Arrays.asList(getAttributes));
        if(!allAttributes.containsAll(attributeSet)){
            returnBuilder.append("[ERROR]Attribute not exist");
            return returnBuilder;
        }
//        if (attributeSet.contains("id")) {
//            attributeSet.remove("id");
//        }

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
            //TODO:flag detection
            if(data.selected==true){
                returnBuilder.append(data.getData()+"\n");
            }
        }
        return returnBuilder;
    }
}
