package edu.uob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class TestCommand {
    private DBServer server;
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }
    private String sendCommandToServer(String command) {
        // Try to send a command to the server - this call will timeout if it takes too long (in case the server enters an infinite loop)
        return assertTimeoutPreemptively(Duration.ofMillis(1000), () -> { return server.handleCommand(command);},
                "Server took too long to respond (probably stuck in an infinite loop)");
    }

    @Test
    public void testCreateDrop(){
        sendCommandToServer("CREATE DATABASE " + "testDatabase" + ";");
        sendCommandToServer("CREATE DATABASE " + "useDatabase" + ";");
        sendCommandToServer("USE useDatabase" + ";");
        sendCommandToServer("CREATE TABLE " + "testTab" + ";");
        sendCommandToServer("CREATE TABLE " + "dropTab" + ";");
        sendCommandToServer("ALTER TABLE ");
        //sendCommandToServer("DROP TABLE " + "dropTab" + ";");
        //sendCommandToServer("DROP DATABASE " + "useDatabase" + ";");



    }
}
