package edu.uob;
import java.util.*;

/*
/<Condition> ::=  "(" <Condition> <BoolOperator> <Condition> ")"
| <Condition> <BoolOperator> <Condition>

| "(" [AttributeName] <Comparator> [Value] ")"
| [AttributeName] <Comparator> [Value]
 */
public class Condition {
    private Set<String> comparisonOperators = new HashSet<>();
    private Set<String> keywords = new HashSet<>();
    private Set<String> boolOperator = new HashSet<>();
    private Set<String> bracketsSet = new HashSet<>();
    //private Set<String> = new HashSet<>();

    public Condition() {
        //create SET of operators
        Collections.addAll(comparisonOperators, ">", "<", ">=", "<=", "==", "!=", "LIKE");
        //create SET of keyowords
        Collections.addAll(keywords, "use", "create", "drop", "alter",
                "insert", "select", "update", "delete", "join");
        Collections.addAll(boolOperator, "and", "or");
        Collections.addAll(bracketsSet, "(", ")");
    }

    //correct name
    public boolean correctName(String name) {
        name=name.toLowerCase();
        if (keywords.contains(name) || boolOperator.contains(name) || comparisonOperators.contains(name)) {
            return false;
        }
        return true;
    }

    //Arrays.copyOfRange()
    /*
    / priority ( ) > AND OR > comparisonOperator
     */
    public void conditionSelector(ArrayList<String> conditionalCommand) {
        for (String command : conditionalCommand) {

            if (bracketsSet.contains(command)) {
                //Complex->brackets
            } else if (boolOperator.contains(command)) {
                //contains boolOperator
            } else if (comparisonOperators.contains(command)) {
                //Simple one
                simpleParser(conditionalCommand);
            } else {
                System.err.print("invalid");
            }
        }
    }

    private boolean simpleParser(ArrayList<String> conditionalCommand) {
        int tokenIndex = 0;
        if(conditionalCommand.size()!=4){
            System.out.print("invalid");
            return false;
        }
        String data=conditionalCommand.get(0);
        String oper=conditionalCommand.get(1);
        String value=conditionalCommand.get(2);
        boolean condition=comparisonOperator(oper,data,value);
        return condition;
    }

    private boolean boolParser(ArrayList<String> conditionalCommand) {
        int tokenIndex = 0;
        ArrayList<String> condition1=new ArrayList<>();
        ArrayList<String> condition2=new ArrayList<>();
        while(!conditionalCommand.get(tokenIndex).equals("AND")||
                !conditionalCommand.get(tokenIndex).equals("OR")){
            condition1.add(conditionalCommand.get(tokenIndex));
            tokenIndex+=1;
        }
        boolean con1Flag=simpleParser(condition1);
        String boolOper=conditionalCommand.get(tokenIndex);
        while(!conditionalCommand.get(tokenIndex).equals(";")){
            condition2.add(conditionalCommand.get(tokenIndex));
            tokenIndex+=1;
        }
        boolean con2Flag=simpleParser(condition2);
        //String bitOperator, boolean con1, boolean con2
        return comparisonOperator(boolOper,con1Flag,con2Flag);
    }


    private boolean comparisonOperator(String comOpr, String data, String value) {
        if (isNumeric(data) && isNumeric(value)) {
            int dataInt = Integer.parseInt(data);
            int valueInt = Integer.parseInt(value);
            if (comOpr.equals(">")) {
                return dataInt > valueInt;
            } else if (comOpr.equals("<")) {
                return dataInt < valueInt;
            } else if (comOpr.equals("=")) {
                return dataInt == valueInt;
            } else if (comOpr.equals(">=")) {
                return (dataInt > valueInt || dataInt == valueInt);
            } else if (comOpr.equals("<=")) {
                return (dataInt < valueInt || dataInt == valueInt);
            }
        } else if (isNumeric(data) || isNumeric(value)) {
            System.err.print("can't compare");
        } else if (isLetter(data) || isLetter(value)) {
            //== like
            if (comOpr.equals("==")) {
                return data.equals(value);
            } else if (comOpr.equalsIgnoreCase("like")) {
                return data.contains(value);
            }
        }
        return true;
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean isLetter(String str) {
        return str.matches("[a-zA-Z]+");
    }

    private boolean comparisonOperator(String bitOperator, boolean con1, boolean con2) {
        if (bitOperator.equalsIgnoreCase("AND")) {
            return (con1 && con2);
        } else if (bitOperator.equalsIgnoreCase("OR")) {
            return (con1 || con2);
        }
        return false;
    }
}
