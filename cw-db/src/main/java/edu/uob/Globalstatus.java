package edu.uob;

import javax.xml.crypto.Data;

/* Track current Table and Database the user is dealing with */
public class Globalstatus {
    //make the Globalstatus globally.
    private static Globalstatus instance;
    private Table currentTable;
    private Database currentDatabase;

    public Globalstatus(){
        this.currentTable = null;
        this.currentDatabase = null;
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
}
