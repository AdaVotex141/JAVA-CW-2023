package edu.uob;
import java.util.*;

/*
/<Condition> ::=  "(" <Condition> <BoolOperator> <Condition> ")"
| <Condition> <BoolOperator> <Condition>

| "(" [AttributeName] <Comparator> [Value] ")"
| [AttributeName] <Comparator> [Value]
 */
public class Condition {
    private final Set<String> comparisonOperators = new HashSet<>();
    private final Set<String> keywords = new HashSet<>();
    private final Set<String> boolOperator = new HashSet<>();
    private final Set<String> bracketsSet = new HashSet<>();
    //private Set<String> = new HashSet<>();

    enum ConditionSelector {
        simpleComparison,
        withBool,
        withbrackets,
        failSelect
    }

    public Condition() {
        //create SET of operators
        Collections.addAll(comparisonOperators, ">", "<", ">=", "<=", "==", "!=", "LIKE");
        //create SET of keyowords
        Collections.addAll(keywords, "use", "create", "drop", "alter",
                "insert", "select", "update", "delete", "join");
        Collections.addAll(boolOperator, "and", "or","AND","OR");
        Collections.addAll(bracketsSet, "(", ")");
    }

    //correct name
    public boolean correctName(String name) {
        name=name.toLowerCase();
        if (!keywords.contains(name) || !boolOperator.contains(name) || !comparisonOperators.contains(name)) {
            for (int i = 0; i < name.length(); i++) {
                char ch = name.charAt(i);
                if(Character.isLetter(ch) || Character.isDigit(ch)){
                    return true;
                }
            }
        }
        return false;
    }

    //Arrays.copyOfRange()
    /*
    / priority ( ) > AND OR > comparisonOperator
     */
    //TODO: flaw work flow
    public ConditionSelector conditionSelection(ArrayList<String> conditionalCommand) {
        boolean hasBrackets = false;
        boolean hasBoolOper = false;

        for (String command : conditionalCommand) {
            command=command.toLowerCase();
            System.out.print(command + "\t");
            if (bracketsSet.contains(command)) {
                // Complex->brackets
                hasBrackets = true;
            } else if (boolOperator.contains(command)) {
                // Contains boolOperator
                hasBoolOper = true;
            }
        }

        if (hasBrackets) {
            return ConditionSelector.withbrackets;
        } else if (hasBoolOper) {
            return ConditionSelector.withBool;
        } else {
            return ConditionSelector.simpleComparison;
        }
    }


    public boolean simpleParser(ArrayList<String> conditionalCommand) {
        int tokenIndex = 0;
        if(conditionalCommand.size()<3){
            System.out.print("invalid");
            return false;
        }
        String attribute=conditionalCommand.get(0);
        String oper=conditionalCommand.get(1);
        String value=conditionalCommand.get(2);
        boolean condition=comparisonOperator(oper,attribute,value);
        return condition;
    }

    public boolean comparisonOperator(String data, String comOpr, String value) {
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
                data = data.replaceAll("'", "");
                System.out.print(data.equals(value));
                return data.equals(value);
            } else if (comOpr.equalsIgnoreCase("like")) {
                return data.contains(value);
            }
        }
        return true;
    }
    public boolean boolParser(ArrayList<String> conditionalCommand) {
        int tokenIndex = 0;
        ArrayList<String> condition1=new ArrayList<>();
        ArrayList<String> condition2=new ArrayList<>();
        while(!conditionalCommand.get(tokenIndex).equalsIgnoreCase("AND")||
                !conditionalCommand.get(tokenIndex).equalsIgnoreCase("OR")){
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

    public static void main(String[] args) {
        boolean flag;
        Condition condition=new Condition();
        flag= condition.comparisonOperator("55",">","55");
        System.out.print(flag+"\n");
        ArrayList<String> conditionalCommand=new ArrayList<>();
        conditionalCommand.add("3name");
        conditionalCommand.add("voice");
        conditionalCommand.add("<");
        conditionalCommand.add("And");
        conditionalCommand.add(">");
        ConditionSelector result=condition.conditionSelection(conditionalCommand);
        System.out.print(result.toString()+"\n");
    }
}
