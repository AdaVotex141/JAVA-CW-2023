package edu.uob;
//import javax.xml.crypto.Data;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;


public class Database {
    public String name;
    protected boolean flag;
    protected static HashMap<String, Table> tables = new HashMap<>();
    public Database(String name){
        this.name=name;
        this.flag=true;
        //this.tables = new HashMap<>();
    }
    //CREATE:mkdir under databases repo.
    public void createDatabase(){
        String databaseFolderPath="databases"+File.separator+name;
        File databaseFolder=new File(databaseFolderPath);

        //check if this exists, if not, create a new one.
        if (!databaseFolder.exists()) {
            boolean created=databaseFolder.mkdirs();
            if(created){
                System.out.println("Database '" + name + "' created successfully.");
            }else{
                System.err.println("Failed to create database '" + name + "'.");
            }
        } else {
            System.err.println("Database '" + name + "' already exists.");
        }
    }


    //USE Enter the databases repo.


    //Drop set this databases flag to false;
    public void drop(){
        flag = false;
        //delete recursively everything in the folder and the folder itself.
    }



}
