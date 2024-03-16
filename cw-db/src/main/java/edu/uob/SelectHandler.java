package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectHandler extends CommandHandler {

    public SelectHandler(String path) {
        super(path);
    }
    //<Select>::=  "SELECT " <WildAttribList> " FROM " [TableName] |\
// "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
    public StringBuilder select(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
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

        //copy currentTable to tempTable
        //Table currentTable=Globalstatus.getInstance().getCurrentTable();
        boolean flagUseTable;
        Table currentTable= reader.useTableByDatabase((tokens.get(tempTokenIndex)));
        //reader.printTabFile(currentTable);

        tempTable.name=String.copyValueOf(currentTable.name.toCharArray());
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        reader.readTabFile(currentDatabase,tempTable,storageFolderPath);

        //select attribute
        if(!tokens.get(tokenIndex).equals("*")){
            HashSet<String> attributeSet=new HashSet<>();
            while(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
                attributeSet.add(tokens.get(tokenIndex));
                tokenIndex+=1;
            }
            //delete not selected attributes.
            //if the attribute is not in the set, then added it to notSelectedAttribute
            String notSelectedAttribute="";
            String[] wholeAttribute=currentTable.getAttribute().split("\t");
            for(String attribute:wholeAttribute){
                if(!attributeSet.contains(attribute)){
                    notSelectedAttribute=notSelectedAttribute+attribute;
                }
            }
//            boolean dropColmnFlag=tempTable.alterDropTable(notSelectedAttribute);
//            if(!dropColmnFlag){
//                returnBuilder.append("[ERROR]");
//                return returnBuilder;
//            }

            //if select * : set all the selected flag -> true
        }else{
            for(Rowdata data:tempTable.datas){
                data.selected=true;
            }
        }

        //TODO very complicated->move to delete instead
        //WHERE
        if(tokens.get(tokens.size()-2).equalsIgnoreCase("WHERE")){
            //TODO:DEAL with WHERE
            ArrayList<String> subList = new ArrayList<>(tokens.subList(tokenIndex, tokens.size()));
            Condition.ConditionSelector selectorflag=condition.conditionSelection(subList);
            if(selectorflag == Condition.ConditionSelector.simpleComparison){
                String attribute=subList.get(0);
                int attributeIndex=tempTable.AttributeIndexWithoutID(attribute);
                String oper=subList.get(1);
                String value=subList.get(2);


            }else if(selectorflag== Condition.ConditionSelector.withBool){

            } else if (selectorflag== Condition.ConditionSelector.withbrackets) {

            }else{
                returnBuilder.append("[ERROR] Can't resolve condition");
                return returnBuilder;
            }
        }



        //print out to terminal
        //tempTable.printAll();
        returnBuilder.append(tempTable.getAttribute()+"\n");
        for(Rowdata data:tempTable.datas){
            //TODO:flag detection
            if(data.selected==true){
                returnBuilder.append(data.getid()+"\t"+data.getData()+"\n");
            }
        }
        returnBuilder.append("[OK]");
        return returnBuilder;
    }


}
