package edu.uob;
//import javax.xml.crypto.Data;
import java.io.*;
import java.util.HashMap;

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
    public void addTableToFile() {
        String filepath=databaseFolderPath+ File.separator + name + ".txt";
        //File textFile = new File(filepath);
        String deletefilePath = databaseFolderPath+ File.separator + name + "deleted" + ".txt";
        if(databaseFolderPath==null){
            filepath=Globalstatus.getInstance().getDatabasesPath()+File.separator+this.name
                    +File.separator+ name + ".txt";
            deletefilePath=Globalstatus.getInstance().getDatabasesPath()+File.separator+this.name+File.separator+ name + "deleted" + ".txt";
        }
        //File deletedtextFile=new File(filepath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath,false))) {
            for (String tableName : tables.keySet()) {
                writer.write(tableName);
                writer.newLine();
            }
            System.out.println("Tables information added to the file.");
        } catch (IOException e) {
            System.err.println("Error adding table information to the file: " + e.getMessage());
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(deletefilePath,false))) {
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
    public int updateTableLatestID(String tablename) throws IOException{
        Table updateTable = getTable(tablename);
        String filePath = databaseFolderPath + File.separator + name + ".txt";
        int currentNumber= -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] parts = line.split("\t");
                //find the tablename in TXT
                if (parts.length > 0 && parts[0].equals(tablename)){
                    if (parts.length>1 && !parts[1].isEmpty()){
                        currentNumber = Integer.parseInt(parts[1]);
                        currentNumber+=1;
                        line = parts[0]+"\t"+currentNumber;
                        return currentNumber;
                    }else{
                        if(updateTable.datas.size()==0){
                            System.err.print("the table is not assigned with attributes");
                        }else{
                            currentNumber=1;
                            line = parts[0]+"\t"+currentNumber;
                        }
                    }
                }

            }
        }
        return currentNumber;
    }

//    public void updateTableDeleteID( String tablename, int id){
//        Table updatetable=getTable(tablename);
//        //find the TXT in the current database.
//        String deletefilePath = databaseFolderPath+ File.separator + name + "deleted" + ".txt";
//        //if there are nothing after the name, find filled with -1.
//
//        //if the INSERT INTO leads to this place, update the current number.
//
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
