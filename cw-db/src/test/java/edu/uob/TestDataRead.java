package edu.uob;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

/*
Tests for inner create(without commands) in Database, Table, and DataReader
* */
public class TestDataRead {
    private DBServer server;
    @BeforeEach
    public void setup() {
        server = new DBServer();
    }

    @Test
    public void TestCreateDatabase(){
        Database testDatabase= new Database("TestDatabase2");
        testDatabase.createDatabase(server.getStorageFolderPath());
        Globalstatus.getInstance().setCurrentDatabase(testDatabase);
        Table testTable = new Table();
        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable");

    }
    @Test
    public void TestDataReader(){

    }




}
