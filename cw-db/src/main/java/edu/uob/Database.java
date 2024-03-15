package edu.uob;
//import javax.xml.crypto.Data;
import java.io.*;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 1/createDatabase: if the database hasn't been created, mkdir and generate 2 txt files.
* 2/addTableToFile: update tableName in txt whenever creates a new table
* 3/updateTableLatestID: (related to insert)
*   When insert a new number, get the latest ID and update it.
* 4/getTable
*   given a String tableName, return a Table table
* 5/dropDatabase
*   Recursively delete the whole repo
* */
public class Database {
    public String name;
    protected boolean flag;
    protected HashMap<String, Table> tables = new HashMap<>();
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
    public boolean createDatabase(String storageFolderPath) {
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
            return false;
        }
        return true;
    }


    //update tables in textfiles whenever creates a new table.
//    public void addTableToFile() {
//
//        String filepath=databaseFolderPath+ File.separator + name + ".txt";
//        //File textFile = new File(filepath);
//        String deletefilePath = databaseFolderPath+ File.separator + name + "deleted" + ".txt";
//        if(databaseFolderPath==null){
//            filepath=Globalstatus.getInstance().getDatabasesPath()+File.separator+this.name
//                    +File.separator+ name + ".txt";
//            deletefilePath=Globalstatus.getInstance().getDatabasesPath()+File.separator+this.name+File.separator+ name + "deleted" + ".txt";
//        }
//        //File deletedtextFile=new File(filepath);
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath,false))) {
//            for (String tableName : tables.keySet()) {
//                writer.write(tableName);
//                writer.newLine();
//            }
//            System.out.println("Tables information added to the file.");
//        } catch (IOException e) {
//            System.err.println("Error adding table information to the file: " + e.getMessage());
//            e.printStackTrace();
//        }
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(deletefilePath,false))) {
//            for (String tableName : tables.keySet()) {
//                writer.write(tableName);
//                writer.newLine();
//            }
//            System.out.println("Tables information added to the file.");
//        } catch (IOException e) {
//            System.err.println("Error adding table information to the file: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    //--------------------------------------if the current database exits-------------------------
////--------------------------------------when INSERT DELETE tables-------------------------
//    public int updateTableLatestID(String tablename){
//        //find table
//        int newID=-1;
//        //Table updateTable = getTable(tablename);
//        String filePath = databaseFolderPath + File.separator + name + ".txt";
//        String tempFilePath = filePath + ".temp";
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
//             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath))){
//            String line;
//            while ((line = reader.readLine()) != null) {
//                //find the tablename line
//                if(line.contains(tablename)){
//                    String replacementLine = null;
//                    Pattern pattern = Pattern.compile("(\\b" + tablename + "\\t\\d+\\b)");
//                    Matcher matcherWithNumber = pattern.matcher(line);
//                    //1.situation 1: there are numbers:
//                    if(matcherWithNumber.find()){
//                        String[] parts1 = line.split("\t");
//                        int temp=Integer.parseInt(parts1[1]);
//                        newID=temp;
//                        temp+=1;
//                        replacementLine=parts1[0]+"\t"+temp;
//                    }else{
//                        //2.situation 2: there are not numbers:
//                        String[] parts2 = line.split("\t");
//                        newID=1;
//                        int temp=2;
//                        replacementLine=parts2[0]+"\t"+temp;
//                    }
//                    writer.write(replacementLine);
//                    writer.newLine();
//                }else{
//                    writer.write(line);
//                    writer.newLine();
//                }
//            }
//        }catch(IOException e){
//            System.err.println("Error reading file: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return newID;
//    }

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

    public boolean dropDatabase(File folder) {
        return dropDatabaseHelper(folder);
    }
    private boolean dropDatabaseHelper(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!dropDatabaseHelper(file)) {
                            return false;
                        }
                    } else {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getAbsolutePath());
                        } else {
                            System.err.println("Failed to delete file: " + file.getAbsolutePath());
                            return false;
                        }
                    }
                }
            }
            if (folder.delete()) {
                System.out.println("Deleted folder: " + folder.getAbsolutePath());
                return true;
            } else {
                System.err.println("Failed to delete folder: " + folder.getAbsolutePath());
                return false;
            }
        } else {
            System.err.println("Folder does not exist: " + folder.getAbsolutePath());
            return false;
        }
    }

    public boolean isSameFolder(Database other) {
        if (other == null || other.databaseFolderPath == null) {
            return false;
        }
        return this.databaseFolderPath.equals(other.databaseFolderPath);
    }

}
