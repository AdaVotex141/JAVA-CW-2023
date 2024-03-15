package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;

public class SelectHandler extends CommandHandler implements Cloneable {

    public SelectHandler(String path) {
        super(path);
    }
//
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }

    //<Select>::=  "SELECT " <WildAttribList> " FROM " [TableName] |\
// "SELECT " <WildAttribList> " FROM " [TableName] " WHERE " <Condition>
    public StringBuilder select(ArrayList<String> tokens, StringBuilder returnBuilder){
        tokenIndex=1;
        if(!tokens.get(tokens.size() - 1).equals(";")){
            returnBuilder.append("[ERROR]:Missing ';' at the end of the sentence");
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
        Table currentTable=Globalstatus.getInstance().getCurrentTable();
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        reader.readTabFile(currentDatabase,tempTable,storageFolderPath);

        //select attribute
        if(!tokens.get(tokenIndex).equals("*")){
            HashSet<String> attributeSet=new HashSet<>();
            while(!tokens.get(tokenIndex).equalsIgnoreCase("FROM")){
                attributeSet.add(tokens.get(tokenIndex));
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
            boolean dropColmnFlag=tempTable.alterDropTable(notSelectedAttribute);
            if(dropColmnFlag==false){
                returnBuilder.append("[ERROR]");
                return returnBuilder;
            }
            //what's left is in is in the
        }


        //WHERE
        if(tokens.get(tokens.size()-2).equalsIgnoreCase("WHERE")){
            //TODO:DEAL with WHERE


        }
        //print out to terminal
        returnBuilder.append("attribute"+"\n");
        for(Rowdata data:tempTable.datas){
            returnBuilder.append(data.getid()+"\t"+data.getData()+"\n");
        }
        reader.printTabFile(tempTable);
        returnBuilder.append("[OK]");
        return returnBuilder;
    }


}
