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
        //simplist definition
        if(selectorflag == Condition.ConditionSelector.simpleComparison){

            returnBuilder=simpleComparison(subList,returnBuilder,table);
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);

        }else if(selectorflag== Condition.ConditionSelector.withBool){
            returnBuilder=withBool(subList,returnBuilder,table);
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);

        } else if (selectorflag== Condition.ConditionSelector.withbrackets) {
            //TODO: re-tokenrise







        }else{
            returnBuilder.append("[ERROR] Can't resolve condition");
            return returnBuilder;
        }



        return returnBuilder;
    }
    private StringBuilder simpleComparison(ArrayList<String> subList, StringBuilder returnBuilder,Table table){
        String attribute=subList.get(0);
        int attributeIndex=table.AttributeIndexWithoutID(attribute);
        String oper=subList.get(1);
        String value=subList.get(2);
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
}
