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
        Collections.addAll(keywords, "Use", "Create", "Drop", "Alter",
                "Insert", "Select", "Update", "Delete", "Join");
        Collections.addAll(boolOperator, "AND", "OR");
        Collections.addAll(bracketsSet, "(", ")");
    }

    //correct name
    public boolean correctName(String name) {
        if (keywords.contains(name) || boolOperator.contains(name) || comparisonOperators.contains(name)) {
            return false;
        }
        return true;
    }

    //Arrays.copyOfRange()
    /*
    / priority ( ) > AND OR > comparisonOperator
     */
    public void conditionOperator(String[] conditionalCommand) {
        for (String command : conditionalCommand){
            if (bracketsSet.contains(command)){
                //Complex->brackets
            }else if(boolOperator.contains(command)){
                //contains boolOperator
            }else if(comparisonOperators.contains(command)){
                //Simple one
            }else{
                System.err.print("invalid");
            }
        }
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
