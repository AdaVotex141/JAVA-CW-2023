package edu.uob;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
/*
* 1/createTable: create a .tab, add the table name to Database's hashmap
*      and update the name in repo's txt
* 2/addAttribute: write the attribute to .tab
* 3/setAttribute: set this.attribute
* 4/alterDropTable:
* 5/alterAddTable:
* 6/modifyFirstAttribute:
* 7/dropTable:delete table and update in Database's hashmap
* */
public class Table{
    public String name;
    protected boolean flag;
    private String attribute;
    protected static ArrayList<Rowdata> datas = new ArrayList<>();
    int latestID;
    public String tableFilePath;

    public Table() {
        this.name=null;
        this.flag=true;
        this.attribute="";
        //this.datas = new ArrayList<>();
    }

    public boolean createTable(Database database,String storageFolderPath,String tablename) {
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
                    return false;
                }
            } catch (IOException e) {
                System.err.println("Failed to create table file for '" +name + "'.");
                return false;
                //e.printStackTrace();
            }
        }else {
            System.err.println("Table with name '" + name + "' already exists.");
            return false;
        }
        return true;
    }
    //add the first line to the table.
    public void addAttribute(String attribute){
        //this.attribute=attribute;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath, true))){
            writer.write(attribute);
            writer.newLine();
            System.out.println("Attribute '" + attribute + "' added to the file.");
        }catch (IOException e) {
            System.err.println("Error adding attribute to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void setAttribute(String setAttribute){
        this.attribute=setAttribute;
    }

    public String getAttribute(){
        return this.attribute;
    }
    //----------------------------INSERT UPDATE DELETE-------------------------------------
    public boolean insertRow(String data) throws IOException {
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
            return false;
        }
        return true;
    }

    public void updateRow(String data){

    }

    public void deleteRow(int id){

    }
    //=============================================Alter========================================
    public void alterDropTable(String dropAttribute){
        //get the first row in the file->find the index of dropAttribute
//        ArrayList<String> attributeList=new ArrayList<>();
//        attributeList=attribute.split("\t");


        //Go through every line, delete the data and write the whole table back to file



    }
    public void alterAddTable(String addAttribute){
        //Alter the add Attribute to the end of the current attribute
        this.attribute=this.attribute+addAttribute;
        //modify first line in file system.
        this.modifyFirstAttribute(this.attribute);
    }
    public void modifyFirstAttribute(String newAttribute) {
        List<String> attributes = readAttributes();
        if (!attributes.isEmpty()) {
            attributes.set(0, newAttribute);  // Modify the first attribute
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath, false))) {
            for (String attr : attributes) {
                writer.write(attr);
                writer.newLine();
            }
            System.out.println("First attribute modified to '" + newAttribute + "' in the file.");
        } catch (IOException e) {
            System.err.println("Error modifying first attribute in the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private List<String> readAttributes() {
        List<String> attributes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tableFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                attributes.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading attributes from the file: " + e.getMessage());
            e.printStackTrace();
        }
        return attributes;
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

}
