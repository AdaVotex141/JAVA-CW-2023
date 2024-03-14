package edu.uob;
import java.util.Arrays;
import java.util.ArrayList;

//READ command and set to different cases:
/*<Use> | <Create> | <Drop> | <Alter> | <Insert> | <Select> | <Update> | <Delete> | <Join>*/
public class Tokenrise {
    private String query;
    private String[] specialCharacters = {"(", ")", ",", ";"};
    private ArrayList<String> tokens;

    public Tokenrise(String query){
        this.query=query;
        this.tokens=new ArrayList<>();
        tokenizeQuery();
    }
    private void tokenizeQuery(){
        query = query.trim();
        String[] fragments = query.split("'");
        for (int i=0; i<fragments.length; i++) {
            // Every odd fragment is a string literal, so just append it without any alterations
            if (i % 2 != 0) tokens.add("'" + fragments[i] + "'");
                // If it's not a string literal, it must be query characters (which need further processing)
            else {
                // Tokenise the fragments into an array of strings
                String[] nextBatchOfTokens = tokenise(fragments[i]);
                // Then add these to the "result" array list (needs a bit of conversion)
                tokens.addAll(Arrays.asList(nextBatchOfTokens));
            }
        }
        //return tokens;
    }

    public ArrayList<String> getTokens(){
        return tokens;
    }

    private String[] tokenise(String input)
    {
        // Add in some extra padding spaces around the "special characters"
        // so we can be sure that they are separated by AT LEAST one space (possibly more)
        for(int i=0; i<specialCharacters.length ;i++) {
            input = input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
        }
        // Remove all double spaces (the previous replacements may had added some)
        // This is "blind" replacement - replacing if they exist, doing nothing if they don't
        while (input.contains("  ")) input = input.replaceAll("  ", " ");
        // Again, remove any whitespace from the beginning and end that might have been introduced
        input = input.trim();
        // Finally split on the space char (since there will now ALWAYS be a space between tokens)
        return input.split(" ");
    }
}
