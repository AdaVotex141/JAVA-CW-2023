package edu.uob;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
/*
/<Condition> ::=  "(" <Condition> <BoolOperator> <Condition> ")"
| <Condition> <BoolOperator> <Condition>

| "(" [AttributeName] <Comparator> [Value] ")"
| [AttributeName] <Comparator> [Value]
 */
public class Condition {
    private Set<String> comparisonOperators = new HashSet<>();
    private Set<String> keywords = new HashSet<>();
    private Set<String> boolOperator= new HashSet<>();

    public Condition(){
        //create SET of operators
        Collections.addAll(comparisonOperators, ">", "<", ">=", "<=", "==","!=","LIKE");
        //create SET of keyowords
        Collections.addAll(keywords, "Use" , "Create" , "Drop" , "Alter" ,
                "Insert" , "Select" , "Update" , "Delete" , "Join");
        Collections.addAll(boolOperator,"AND","OR");
    }
    //correct name
    public boolean correctName(String name){
        if(keywords.contains(name) || boolOperator.contains(name) || comparisonOperators.contains(name)){
            return false;
        }
        return true;
    }

    //Arrays.copyOfRange()
    /*
    / priority ( ) > AND OR > comparisonOperator
     */
    public void conditionOperator(String[] conditionalCommand){

    }


}
