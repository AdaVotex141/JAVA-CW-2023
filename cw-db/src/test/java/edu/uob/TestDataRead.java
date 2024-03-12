package edu.uob;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
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
    private static void assertDatabaseExists(String parentFolder, String folderName) {
        File folder = new File(parentFolder + File.separator + folderName);
        assert folder.exists() && folder.isDirectory() : "Folder does not exist: " + folder.getAbsolutePath();
    }
    private static void assertTabExists(String parentFolder, String filename) {
        File file = new File(parentFolder + File.separator + filename + ".tab");
        assert file.exists() && file.isFile() : "File does not exist: " + file.getAbsolutePath();
    }
    private static void assertTXTExists(String parentFolder, String foldername) {
        File file = new File(parentFolder + File.separator + foldername + ".txt");
        File deletedfile = new File(parentFolder + File.separator + "deleted"+ foldername + ".txt");
        assert file.exists() && file.isFile() : "File does not exist: " + file.getAbsolutePath();
        assert deletedfile.exists() && deletedfile.isFile() : "File does not exist: " + file.getAbsolutePath();
    }

    //Test the basic create and drop database,tables
    @Test
    public void TestCreateDatabase(){
        //Create a database and delete the database;
        Database testDatabase= new Database("TestDatabase");
        testDatabase.createDatabase(server.getStorageFolderPath());
        Globalstatus.getInstance().setCurrentDatabase(testDatabase);
        Table testTable = new Table();
        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable");
        Table testTable2 = new Table();
        testTable2.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable2");

        Globalstatus.getInstance().setCurrentTable(testTable);


        assertDatabaseExists(server.getStorageFolderPath(), "testdatabase");
        assertTabExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testtable");
        assertTabExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testtable2");

        Table tableDelete= Globalstatus.getInstance().getCurrentTable();
        Database databaseDelete = Globalstatus.getInstance().getCurrentDatabase();
        tableDelete.dropTable(databaseDelete);
        assertTabExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testtable2");
        databaseDelete.dropDatabase();
    }




//Test the txt files are updated.
//    @Test
//    public void TestTrackedTXT(){
//        Database testDatabase= new Database("TestDatabase");
//        testDatabase.createDatabase(server.getStorageFolderPath());
//        Globalstatus.getInstance().setCurrentDatabase(testDatabase);
//        Table testTable = new Table();
//        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable");
//        //assertTXTExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testdatabase");
//
//
//        Table testTable2 = new Table();
//        testTable2.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable2");
//
//
//    }
//    @BeforeEach
////    public void deletefiles(){
//        Database databaseDelete = Globalstatus.getInstance().getCurrentDatabase();
//        System.out.print(databaseDelete.name);
//        databaseDelete.dropDatabase();
////
////    }




}
