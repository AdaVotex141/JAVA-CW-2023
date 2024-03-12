package edu.uob;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Table{
    public String name;
    protected boolean flag;
    private String attribute;
    protected static ArrayList<Rowdata> datas = new ArrayList<>();
    int latestID;
    String tableFilePath;

    public Table() {
        this.name=null;
        this.flag=true;
        this.attribute="";
        //this.datas = new ArrayList<>();
    }

    public void createTable(Database database,String storageFolderPath,String tablename) {
        this.name=tablename.toLowerCase();
        //check if the database has been created or not.
        Database currentdatabase=(Globalstatus.getInstance().getCurrentDatabase());
        if (!database.tables.containsKey(name)) {
            try {
                String filePath = storageFolderPath + File.separator + database.name + File.separator + name + ".tab";
                this.tableFilePath=filePath;
                File tableFile = new File(tableFilePath);
                if (tableFile.createNewFile()) {
                    currentdatabase.tables.put(name, this);
                    currentdatabase.addTableToFile();
                } else {
                    System.err.println("Failed to create table file for '" +name+ "'. File already exists.");
                }
            } catch (IOException e) {
                System.err.println("Failed to create table file for '" +name + "'.");
                //e.printStackTrace();
            }
        }else {
            System.err.println("Table with name '" + name + "' already exists.");
        }
    }
    //add the first line to the tab.
    public void addAttribute(String attribute){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath, true))){
            writer.write(attribute);
            writer.newLine();
            System.out.println("Attribute '" + attribute + "' added to the file.");
        }catch (IOException e) {
            System.err.println("Error adding attribute to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //----------------------------INSERT UPDATE DELETE-------------------------------------
    public void insertRow(String data) throws IOException {
        //find the latestRow from txt
        Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
        int id=currentDatabase.updateTableLatestID(this.name);
        File tableFile = new File(tableFilePath);
        String newRow = id + "\t" + data;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath, true))) {
            // append the new row to the file
            writer.write(newRow);
            writer.newLine();
            System.out.println("Row inserted successfully.");
        } catch (IOException e) {
            System.err.println("Error inserting row: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateRow(String data){

    }

    public void deleteRow(int id){

    }
    //Alter
    public void alterTable(){

    }

    //=================================Drop===================================
    public void dropTable(Database database){
        //update the hashmap in database
        if (database.tables.containsKey(name)){
            database.tables.remove(name);
            database.addTableToFile();
            //delete the file.
            File tableFile = new File(tableFilePath);
            if (tableFile.exists()) {
                if (tableFile.delete()) {
                    System.out.println("Table '" + name + "' deleted successfully.");
                } else {
                    System.err.println("Failed to delete table file for '" + name + "'.");
                }
            } else {
                System.err.println("Table file for '" + name + "' does not exist.");
            }
        } else {
            System.err.println("Table with name '" + name + "' not found in the database.");
        }
    }


    public void setAttribute(String setAttribute){
        this.attribute=setAttribute;
    }

    public String getAttribute(){
        return this.attribute;
    }



}
