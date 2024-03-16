package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    protected ArrayList<Rowdata> datas = new ArrayList<>();
    private int latestID;
    public String tableFilePath;
    public String IDFilePath;

    public Table() {
        this.name=null;
        this.flag=true;
        this.attribute="";
        this.latestID=0;
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

                String fileIDPath = storageFolderPath + File.separator + database.name + File.separator + name + ".id";
                this.IDFilePath=fileIDPath;
                File tableFile = new File(tableFilePath);
                if (tableFile.createNewFile()) {
                    currentdatabase.tables.put(name, this);
                    File fileID=new File(fileIDPath);
                    fileID.createNewFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileIDPath))) {
                        writer.write("1");
                        this.latestID=1;
                        System.out.println("1 has been written to the file.");
                    } catch (IOException e) {
                        System.err.println("Error writing to file: " + e.getMessage());
                        e.printStackTrace();
                    }

                    //write the first id
//                    this.attribute="id"+"\t";
//                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath, true))){
//                        writer.write(attribute);
//                        writer.newLine();
//                        System.out.println("Attribute '" + attribute + "' added to the file.");
//                    }catch (IOException e) {
//                        System.err.println("Error adding attribute to the file: " + e.getMessage());
//                        e.printStackTrace();
//                    }

                    //currentdatabase.addTableToFile();
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

    public void addAttributeID(String attribute){
        //this.attribute=attribute;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath, true))){
            writer.write(attribute);
            //writer.newLine();
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
        String IDfilePath=this.IDFilePath;
        File IDfile = new File(IDfilePath);
        int id=0;
        int temp=0;
        try (BufferedReader reader = new BufferedReader(new FileReader(IDfile))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                id = Integer.parseInt(line.trim());
                temp=id+1;
                this.latestID=temp;
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading from ID file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(IDfile))) {
            writer.write(Integer.toString(temp));
        } catch (IOException e) {
            System.err.println("Error writing to ID file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
    public int getLatestID(){
        return latestID;
    }
    public int AttributeIndexWithoutID(String findAttribute) {
        String[] attributes = this.attribute.split("\t");
        int i;
        for (i = 0; i < attributes.length; i++) {
            if (attributes[i].equals(findAttribute)) {
                return i-1;
                //break;
            }
            int attributeIndex = i - 1;
        }
        return -1;
    }
    //=============================================Alter========================================
    public boolean alterDropTable(String dropAttribute){
        // get the index of dropAttribute in the attribute list
        String[] attributeList = this.attribute.split("\t");
        int indexInAttribute = -1;
        for (int i = 0; i < attributeList.length; i++) {
            if (attributeList[i].equals(dropAttribute)) {
                indexInAttribute = i;
                break;
            }
        }
        if (indexInAttribute != -1) {
            StringBuilder updatedAttribute = new StringBuilder();
            for (int i = 0; i < attributeList.length; i++) {
                if (i != indexInAttribute) {
                    updatedAttribute.append(attributeList[i]).append("\t");
                }
            }
            this.attribute = updatedAttribute.toString().trim();
            int fileIndex = indexInAttribute - 1;
            for (Rowdata data : this.datas) {
                String dataString = data.getData();
                String[] dataBox = dataString.split("\t");
                if (fileIndex >= 0 && fileIndex < dataBox.length) {
                    dataBox[fileIndex] = null;
                }
                StringBuilder updateData = new StringBuilder();
                for (String box : dataBox) {
                    if (box != null) {
                        updateData.append(box).append("\t");
                    }
                }
                data.setData(updateData.toString().trim());
            }
            return true;
        } else {
            System.err.println("Column '" + dropAttribute + "' not found.");
            return false;
        }
    }

    public boolean alterAddTable(String addAttribute){
        //Alter the add Attribute to the end of the current attribute
        this.attribute=this.attribute+addAttribute;
        //modify first line in file system.
        boolean flag=this.modifyFirstAttribute(this.attribute);
        return flag;
    }
    private boolean modifyFirstAttribute(String newAttribute) {
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
            return false;
        }
        return true;
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
    public void dropTable(Database database) {
        String name = this.name;

        if (database.tables.containsKey(name)) {
            database.tables.remove(name);
            File tableFile = new File(tableFilePath);
            String idFilePath=tableFilePath.replace(".tab",".id");

            File idFile = new File(idFilePath);

            if (tableFile.exists()) {
                if (tableFile.delete()) {
                    System.out.println("Table '" + name + "' deleted successfully.");
                } else {
                    System.err.println("Failed to delete table file for '" + name + "'.");
                }
            } else {
                System.err.println("Table file for '" + name + "' does not exist.");
            }

            if (idFile.exists()) {
                if (idFile.delete()) {
                    System.out.println("ID file for '" + name + "' deleted successfully.");
                } else {
                    System.err.println("Failed to delete ID file for '" + name + "'.");
                }
            } else {
                System.err.println("ID file for '" + name + "' does not exist.");
            }
        } else {
            System.err.println("Table with name '" + name + "' not found in the database.");
        }
    }

    public void printAll(){
        for(Rowdata data:this.datas){
            System.out.print(data.getData()+"\t");
        }
    }

}
