package edu.uob;

import javax.xml.crypto.Data;
import java.nio.file.Paths;

/* Track current Table and Database the user is dealing with */
public class Globalstatus {
    //make the Globalstatus globally.
    private static Globalstatus instance;
    private Table currentTable;
    private Database currentDatabase;
    private String storageFolderPath;

    public Globalstatus(){
        this.currentTable = null;
        this.currentDatabase = null;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();
    }

    public static Globalstatus getInstance() {
        if (instance == null) {
            instance = new Globalstatus();
        }
        return instance;
    }
//Update it in USE
    public void setCurrentDatabase(Database currentDatabase) {
        this.currentDatabase = currentDatabase;
    }
//Update it in USE
    public void setCurrentTable(Table currentTable) {
        this.currentTable = currentTable;
    }
    public Table getCurrentTable(){
        return this.currentTable;
    }
    public Database getCurrentDatabase(){
        return this.currentDatabase;
    }
    public String getDatabasesPath(){
        return this.storageFolderPath;
    }
}
