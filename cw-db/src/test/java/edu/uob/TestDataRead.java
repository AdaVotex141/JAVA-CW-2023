package edu.uob;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/*
Tests for inner create(without commands) in Database, Table, and DataReader
* */
public class TestDataRead {
    private DBServer server;
    private DataReader reader;
    private Condition condition;
    @BeforeEach
    public void setup() {
        server = new DBServer();
        reader=new DataReader();
        condition= new Condition();
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
//        //Create a database and delete the database;
//        Database testDatabase= new Database("TestDatabase");
//        testDatabase.createDatabase(server.getStorageFolderPath());
//        Globalstatus.getInstance().setCurrentDatabase(testDatabase);
//        Table testTable = new Table();
//        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable");
//        Table testTable2 = new Table();
//        testTable2.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable2");
//
//        Globalstatus.getInstance().setCurrentTable(testTable);
//
//        assertDatabaseExists(server.getStorageFolderPath(), "testdatabase");
//        assertTabExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testtable");
//        assertTabExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testtable2");
//
//        Table tableDelete= Globalstatus.getInstance().getCurrentTable();
//        Database databaseDelete = Globalstatus.getInstance().getCurrentDatabase();
//        tableDelete.dropTable(databaseDelete);
//        assertTabExists(server.getStorageFolderPath() + File.separator + "testdatabase", "testtable2");
//        databaseDelete.dropDatabase(new File(server.getStorageFolderPath() + File.separator+databaseDelete.name));
    }

    //Test the basic create and drop database,tables
    @Test
    public void TestAlterTable(){
//        //default colmun ID is implement in create Table
//        Database testDatabase= new Database("TestDatabase");
//        testDatabase.createDatabase(server.getStorageFolderPath());
//        Globalstatus.getInstance().setCurrentDatabase(testDatabase);
//        Table testTable = new Table();
//        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable");
//        //define if testTable is null:
//        assertNotNull(testTable);
//        testTable.setAttribute("id"+"\t");
//        testTable.addAttribute(testTable.getAttribute());
//
//        testTable.alterAddTable("application");
//        assertEquals(testTable.getAttribute(), "id"+"\t"+"application");
//        //write the result back to file
//        //writeTabFile(Table table, String tableFilePath)
//        reader.writeTabFile(testTable,testTable.tableFilePath);
//        String testTablePath=testTable.tableFilePath;
//        try (BufferedReader reader = new BufferedReader(new FileReader(testTablePath))) {
//            String line;
//            boolean found = false;
//            String firstLine = reader.readLine();
//            boolean isFileEmpty = true;
//            if (firstLine != null && !firstLine.isEmpty()){
//                System.out.print("The attribute is written in!");
//                if(firstLine.contains(testTable.getAttribute())){
//                    found=true;
//                }
//                System.out.println(found);
//            }
//        }catch (IOException e) {
//            System.err.println("Can't read: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        //test Drop:continue added->added "\t"
//        //TODO:continue added "\t"
//        testTable.alterAddTable("\t"+"name");
//        testTable.alterAddTable("\t"+"mark");
//        reader.writeTabFile(testTable,testTable.tableFilePath);
//        try (BufferedReader reader = new BufferedReader(new FileReader(testTablePath))) {
//            String line;
//            boolean found = false;
//            String firstLine = reader.readLine();
//            boolean isFileEmpty = true;
//            if (firstLine != null && !firstLine.isEmpty()){
//                System.out.print("The attribute is written in!");
//                if(firstLine.contains(testTable.getAttribute())){
//                    found=true;
//                }
//                System.out.println(found);
//            }
//        }catch (IOException e) {
//            System.err.println("Can't read: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        testTable.alterDropTable("name");
//        reader.writeTabFile(testTable,testTable.tableFilePath);
//
//
//        Database databaseDelete = Globalstatus.getInstance().getCurrentDatabase();
//        testTable.dropTable(databaseDelete);
//        databaseDelete.dropDatabase(new File(server.getStorageFolderPath() + File.separator+databaseDelete.name));
    }

    @Test
    public void TestuseTable(){
        Database testDatabase= new Database("TestDatabase");
        testDatabase.createDatabase(server.getStorageFolderPath());
        Globalstatus.getInstance().setCurrentDatabase(testDatabase);
        Table testTable = new Table();
        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable1");
        Table testTable2 = new Table();
        testTable.createTable(Globalstatus.getInstance().getCurrentDatabase(),server.getStorageFolderPath(),"TestTable2");
        reader.useTable(testTable2.name, server.getStorageFolderPath());
        assertNotNull(testTable2);
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
