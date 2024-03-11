package edu.uob;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Table{
    public String name;
    protected boolean flag;
    public String attribute;
    protected static ArrayList<Rowdata> datas = new ArrayList<>();

    public Table(String name) {
        this.name=name.toLowerCase();
        this.flag=true;
        //this.datas = new ArrayList<>();
    }
    //Create table->create a tab in the repo, and add it to the databases' hashmap
    //Create with attributes
    public void createTable(Database database) {
        try {
            String filePath = "databases" + File.separator + database.name + File.separator + name + ".tab";
            File tableFile = new File(filePath);
            if (tableFile.createNewFile()) {
                System.out.println("Table file for '" + name + "' created successfully.");
                Database.tables.put(name, this);
            } else {
                System.err.println("Failed to create table file for '" + name + "'. File already exists.");
            }
        } catch (IOException e) {
            System.err.println("Failed to create table file for '" + name + "'.");
            //e.printStackTrace();
        }
    }

    public void createTable(Database database,String attribute){

    }





    //Alter

    //Drop



}
