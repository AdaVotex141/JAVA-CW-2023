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
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
            return returnBuilder;
        }
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
            //write back to file
            reader.writeTabFile(table,table.tableFilePath);
            //reintisualised the database:
            currentDatabase.updateTable(table);

        }else if(selectorflag== Condition.ConditionSelector.withBool){
            //this is the index in the command that should be replaced
            ArrayList<Integer> dataIndex=condition.dataIndex;
            ArrayList<String> attributeSelected=condition.attributeSelected;
            ArrayList<Integer> attributeIndex=new ArrayList<>();
            //this is the index of every attribute selected, can be iterated by Math.max
            for(String attribute:attributeSelected){
                attributeIndex.add(table.AttributeIndexWithoutID(attribute));
            }

            for(Rowdata data: table.datas) {
                String[] rowData = data.getDataSplit();
                //do the replacement
                ArrayList <String> toReplace=new ArrayList<>();
                //ArrayList <String> replaceCommand=new ArrayList<>();
                for(int attributeindex:attributeIndex){
                    toReplace.add(rowData[attributeindex]);
                }
                for(int i=0; i<toReplace.size();i++){
                    int index=dataIndex.get(i);
                    subList.set(index,toReplace.get(i));
                }
                //the whole command
                //TODO:give up for now.
                if(condition.evaluateCondition(subList)){
                    data.flag=false;
                }
            }
        } else if (selectorflag== Condition.ConditionSelector.withbrackets) {




        }else{
            returnBuilder.append("[ERROR] Can't resolve condition");
            return returnBuilder;
        }



        return returnBuilder;
    }
}
