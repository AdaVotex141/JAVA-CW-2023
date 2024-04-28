package edu.uob;

import com.alexmerz.graphviz.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Extended {

    private GameServer server;

    @BeforeEach
    void setup() throws FileNotFoundException, ParseException {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        server = new GameServer(entitiesFile, actionsFile);
    }

    String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    void testLook() {
        String response = sendCommandToServer("simon: look");
        response = response.toLowerCase();
        assertTrue(response.contains("cabin"), "Did not see the name of the current room in response to look");
        assertTrue(response.contains("axe"), "Did not see a description of the room in response to look");
        assertTrue(response.contains("magic potion"), "Did not see a description of artifacts in response to look");
        assertTrue(response.contains("coin"), "Did not see description of furniture in response to look");
        assertTrue(response.contains("forest"), "Did not see available paths in response to look");
        //System.out.print(response);
    }



}
