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
        sendCommandToServer("DROP TABLE " + "testTab" + ";");
        sendCommandToServer("DROP DATABASE " + "useDatabase" + ";");
        sendCommandToServer("DROP DATABASE " + "testDatabase" + ";");
    }
    @Test
    public void workFlowTest1(){
        sendCommandToServer("CREATE DATABASE " + "testDatabase" + ";");
        sendCommandToServer("USE testDatabase" + ";");
        sendCommandToServer("CREATE TABLE " + "test1" + ";");
        sendCommandToServer("CREATE TABLE test2 (name,pass);");
        sendCommandToServer("Alter TABLE test2 add mark;");
        sendCommandToServer("Alter TABLE test2 add mark;");
        sendCommandToServer("Alter TABLE test2 add mark;");
        sendCommandToServer("Alter TABLE test2 drop mark;");
        sendCommandToServer("Alter TABLE test2 drop id;");
        //sendCommandToServer("INSERT INTO test1 VALUES ('Simon', 65, TRUE)");
        //sendCommandToServer("INSERT INTO test2 VALUES ('Simon', 65, TRUE)");
        sendCommandToServer("drop database testDatabase;");

    }
    @Test
    public void testDelete(){
        sendCommandToServer("CREATE DATABASE " + "testDatabase2" + ";");
        sendCommandToServer("USE testDatabase2" + ";");
        sendCommandToServer("CREATE TABLE marks (name, mark, pass);");
        sendCommandToServer("INSERT INTO marks VALUES ('Simon', 65, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Sion', 55, TRUE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Rob', 35, FALSE);");
        sendCommandToServer("INSERT INTO marks VALUES ('Chris', 20, FALSE);");

        //delete
        //sendCommandToServer("DELETE FROM marks WHERE name =='Simon';");


        //sendCommandToServer("drop database testDatabase2;");
    }
}
