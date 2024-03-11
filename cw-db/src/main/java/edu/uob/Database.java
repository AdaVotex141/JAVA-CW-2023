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
    //----------------------------CREATE Databases, CREATE Tables-------------------------
    //Assume the databases is not exist.But what if it exists????
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




    //update tables in textfiles whenever creates a new table.
    public void addTableToFile() {
        String filepath=databaseFolderPath+ File.separator + name + ".txt";
        //File textFile = new File(filepath);
        String deletefilePath = databaseFolderPath+ File.separator + name + "deleted" + ".txt";
        //File deletedtextFile=new File(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath,true))) {
            for (String tableName : tables.keySet()) {
                writer.write(tableName);
                writer.newLine();
            }
            System.out.println("Tables information added to the file.");
        } catch (IOException e) {
            System.err.println("Error adding table information to the file: " + e.getMessage());
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(deletefilePath,true))) {
            for (String tableName : tables.keySet()) {
                writer.write(tableName);
                writer.newLine();
            }
            System.out.println("Tables information added to the file.");
        } catch (IOException e) {
            System.err.println("Error adding table information to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //--------------------------------------if the current database exits-------------------------
//--------------------------------------when INSERT DELETE tables-------------------------
    public void updateTableLatestID(String storageFolderPath, String tablename, int id){
        Table updatetable=getTable(tablename);

    }

    public void updateTableDeleteID(String storageFolderPath, String tablename, int id){
        Table updatetable=getTable(tablename);
    }
    //Be aware of this return null//which table is selected in the database's hashmap,(get name and return table)
    public Table getTable(String tablename){
        if(tables.containsKey(tablename)){
            return tables.get(tablename);
        }else{
            System.err.println("Table with name '" + tablename + "' does not exist.");
            return null;
        }
    }

    //=================================Drop===================================
    //Recursively delete all the files in the database and the database itself
    public void dropDatabase() {
        File folder = new File(databaseFolderPath);
        dropDatabaseHelper(folder);
    }
    private void dropDatabaseHelper(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        dropDatabaseHelper(file);
                    } else {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } else {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                        }
                    }
                }
            }
            //delete the folder itself.
            if (folder.delete()) {
                System.out.println("Deleted folder: " + folder.getAbsolutePath());
            } else {
                System.err.println("Failed to delete folder: " + folder.getAbsolutePath());
            }
        } else {
            System.err.println("Folder does not exist: " + folder.getAbsolutePath());
        }
    }
}
