package edu.uob;
import java.util.Arrays;
import java.util.ArrayList;

//READ command and set to different cases:
/*<Use> | <Create> | <Drop> | <Alter> | <Insert> | <Select> | <Update> | <Delete> | <Join>*/
public class Tokenrise {
    private String query;
    private String[] specialCharacters = {"(", ")", ",", ";"};
    private ArrayList<String> tokens;

    public Tokenrise(String query) {
        this.query=query.trim();
        this.tokens=new ArrayList<>();
        tokenizeQuery();
    }

    private void tokenizeQuery() {
        String[] fragments=query.split("'");
        for (int i=0; i<fragments.length; i++) {
            if (i%2!=0)
                tokens.add("'" + fragments[i] + "'");
            else {
                String[] nextBatchOfTokens=tokenise(fragments[i]);
                tokens.addAll(Arrays.asList(nextBatchOfTokens));
            }
        }
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    private String[] tokenise(String input) {
        for (int i=0; i<specialCharacters.length; i++) {
            input=input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
        }
        while (input.contains("  "))
            input=input.replaceAll("  ", " ");
        input=input.trim();
//        input = input.replaceAll(">=", " >= ")
//                .replaceAll("<=", " <= ")
//                .replaceAll("==", " == ");
        return input.split(" ");
    }

//    public static void main(String[] args) {
//        String query = "  (name<=get)AND(fire==2); ";
//        query="SELECT name FROM marks WHERE mark>60; ";
//        Tokenrise tokenizer = new Tokenrise(query);
//        ArrayList<String> tokens = tokenizer.getTokens();
//        for (String token : tokens) {
//            System.out.println(token);
//        }
//    }
}
