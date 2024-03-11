package edu.uob;
//import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;


public class Database {
    public String name;
    protected boolean flag;
    protected static HashMap<String, Table> tables = new HashMap<>();
    String databaseFolderPath;

    public Database(String name) {
        this.name = name.toLowerCase();
        this.flag = true;
        this.databaseFolderPath=null;
        //this.tables = new HashMap<>();
    }

    //CREATE:mkdir under databases repo.
    public void createDatabase(String storageFolderPath) {
        this.databaseFolderPath = storageFolderPath + File.separator + name;
        File databaseFolder = new File(databaseFolderPath);
        //check if this exists, if not, create a new one.
        if (!databaseFolder.exists()) {
            boolean created = databaseFolder.mkdirs();
            if (created) {
                System.out.println("Database '" + name + "' created successfully.");
                String filePath = databaseFolder.getAbsolutePath() + File.separator + name + ".txt";
                String deletefilePath =  databaseFolder.getAbsolutePath() + File.separator + name + "deleted" + ".txt";
                try {
                    File textFile = new File(filePath);
                    File deletedtextFile = new File(deletefilePath);
                    if (textFile.createNewFile() && deletedtextFile.createNewFile()) {
                        System.out.println("Text file created successfully.");
                    } else {
                        System.err.println("Failed to create text file. File already exists.");
                    }
                } catch (IOException e) {
                    System.err.println("Error creating text file: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.err.println("Failed to create database '" + name + "'.");
            }
        } else {
            System.err.println("Database '" + name + "' already exists.");
        }
    }

    //Use this methods whenever you create a new table.
    public static void addTableToFile(String databaseFolderPath) {

//        String databaseFolderPath = storageFolderPath + File.separator + this.name;
//        File databaseFolder = new File(databaseFolderPath);
//        String filePath = databaseFolder.getAbsolutePath() + File.separator + this.name + ".txt";
//        String deletefilePath = databaseFolder.getAbsolutePath() + File.separator + this.name + "deleted" + ".txt";
//
//        File textFile = new File(filePath);
//        File deletedtextFile = new File(deletefilePath);
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
//            for (String tableName : tables.keySet()) {
//                writer.write(tableName);
//                writer.newLine();
//            }
//            System.out.println("Tables added to file successfully.");
//        } catch (IOException e) {
//            System.err.println("Error writing tables to file: " + e.getMessage());
//            e.printStackTrace();
//        }
    }


    public void updateTableLatestID(String storageFolderPath, Table table){

    }

    public void updateTableDeleteID(String storageFolderPath, Table table){

    }



}
