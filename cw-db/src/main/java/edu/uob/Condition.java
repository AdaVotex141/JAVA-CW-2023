package edu.uob;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
/<Condition> ::=  "(" <Condition> <BoolOperator> <Condition> ")"
| <Condition> <BoolOperator> <Condition>

| "(" [AttributeName] <Comparator> [Value] ")"
| [AttributeName] <Comparator> [Value]
 */
public class Condition {
    public final Set<String> comparisonOperators = new HashSet<>();
    public final Set<String> keywords = new HashSet<>();
    public final Set<String> boolOperator = new HashSet<>();
    public final Set<String> bracketsSet = new HashSet<>();
    public ArrayList<Integer> dataIndex=new ArrayList<>();
    public ArrayList<String> attributeSelected = new ArrayList<>();
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
                "insert", "select", "update", "delete", "join","table","database","like","true","false");
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
        //
        System.out.print("get in where");
        for(int i=0;i<conditionalCommand.size()-1;i++){
            String command=conditionalCommand.get(i).toLowerCase();
            if(comparisonOperators.contains(conditionalCommand.get(i+1))){
                dataIndex.add(i);
                attributeSelected.add(conditionalCommand.get(i));
            }

            //System.out.print(command + "\t");
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
    public boolean boolOperator(String bitOperator, boolean con1, boolean con2) {
        if (bitOperator.equalsIgnoreCase("AND")) {
            return (con1 && con2);
        } else if (bitOperator.equalsIgnoreCase("OR")) {
            return (con1 || con2);
        }
        return false;
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
            data = String.valueOf(data); // Convert to string
            value = String.valueOf(value); // Convert to string
        } else if(comOpr.equals("!=")){
            data = data.replaceAll("'", "");
            value = value.replaceAll("'", "");
            return !data.equals(value);
        }else if(comOpr.equals("==")){
            data = data.replaceAll("'", "");
            value = value.replaceAll("'", "");
            return data.equals(value);
        } else if (comOpr.equalsIgnoreCase("like")) {
            data = data.replaceAll("'", "");
            data= data.replaceAll(" ","");
            value = value.replaceAll("'", "");
            value= value.replaceAll(" ","");
            System.out.print(data+"\t");
            System.out.print(value);
            return data.contains(value);
        }
        return true;
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


    public boolean boolParser(ArrayList<String> conditionalCommand) {
        int tokenIndex = 0;
        ArrayList<String> condition1=new ArrayList<>();
        ArrayList<String> condition2=new ArrayList<>();
        while(!conditionalCommand.get(tokenIndex).equalsIgnoreCase("AND") &&
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
        return boolOperator(boolOper,con1Flag,con2Flag);
    }

    public ArrayList<String> tokenParse(ArrayList<String> subList){
        System.out.print("into tokenParse");
        String condition=subList.get(0);
        //turn mark>40 into mark > 40
        Pattern pattern = Pattern.compile("(\\w+)\\s*([<>]=?|==|!=)\\s*(\\w+)");
        Matcher matcher = pattern.matcher(condition);
        if(matcher.find()){
            String attributeName = matcher.group(1);
            String operator = matcher.group(2);
            String value = matcher.group(3);

            //set it back to subList
            ArrayList<String> resultList = new ArrayList<>();
            resultList.add(attributeName);
            resultList.add(operator);
            resultList.add(value);
            System.out.print("token reparse");
            return resultList;

        }else{
            System.err.print("Can't parse token");
        }
        return subList;
    }



//
//    public boolean evaluateCondition(ArrayList<String> subList) {
//        Stack<String> operandStack = new Stack<>();
//        Stack<String> operatorStack = new Stack<>();
//        for(String token:subList){
//            if(token.equals("(")){
//                operatorStack.push("(");
//            }else if(token.equals(")")){
//                while(!operatorStack.isEmpty() && !operatorStack.peek().equals("(")){
//                    //(String data, String comOpr, String value)
//                    String value= operatorStack.pop();
//                    String oper=operatorStack.pop();
//                    String data=operatorStack.pop();
//                    if(comparisonOperator(data,oper,value)){
//                        //if true
//                        operatorStack.push("1");
//                    }else{
//                        operatorStack.push("0");
//                    }
//                }
//            } else if (boolOperator.contains(token.toLowerCase())) {
//                operandStack.push(token);
//
//
//            }
//        }
//        return true;
//    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean isLetter(String str) {
        return str.matches("[a-zA-Z]+");
    }


//    public boolean evaluateCondition(String condition, Table table) {
//        List<String> tokens = parseCondition(condition);
//
//        Stack<Boolean> operandStack = new Stack<>();
//        Stack<String> operatorStack = new Stack<>();
//
//        for (String token : tokens) {
//            if (isOperand(token)) {
//                boolean operandValue = evaluateOperand(token, table);
//                operandStack.push(operandValue);
//            } else if (isOperator(token)) {
//
//                while (!operatorStack.isEmpty() && hasHigherPrecedence(token, operatorStack.peek())) {
//                    performOperation(operandStack, operatorStack);
//                }
//
//                operatorStack.push(token.charAt(0));
//            } else if (token.equals("(")) {
//
//                operatorStack.push('(');
//            } else if (token.equals(")")) {
//                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
//                    performOperation(operandStack, operatorStack);
//                }
//                operatorStack.pop();
//            }
//        }
//
//        while (!operatorStack.isEmpty()) {
//            performOperation(operandStack, operatorStack);
//        }
//
//        return operandStack.pop();
//    }
//
//    private boolean evaluateOperand(String value, int operandIndex, Table table, String oper) {
//        for (Rowdata data : table.datas) {
//            String[] dataSplit = data.getDataSplit();
//            String dataSelected = dataSplit[operandIndex];
//            if (comparisonOperator(dataSelected, oper, value)) {
//                return true;
//            }
//        }
//        return false;
//    }









//
//    public static void main(String[] args) {
//        boolean flag;
//        Condition condition=new Condition();
//        flag= condition.comparisonOperator("55",">","55");
//        System.out.print(flag+"\n");
//        ArrayList<String> conditionalCommand=new ArrayList<>();
//        conditionalCommand.add("3name");
//        conditionalCommand.add("voice");
//        conditionalCommand.add("<");
//        conditionalCommand.add("And");
//        conditionalCommand.add(">");
//        ConditionSelector result=condition.conditionSelection(conditionalCommand);
//        System.out.print(result.toString()+"\n");
//    }
}
