package edu.uob;
import java.util.ArrayList;
//Defines the table
public class Table {
    public String name;
    public String attributeName;
    public int size;
    protected boolean flag;
    ArrayList<RowData> datalist=new ArrayList<RowData>();

    public Table(String tableName){
        this.name=tableName;
        this.flag=true;
//        size=0;
        ArrayList<RowData> datalist=new ArrayList<RowData>();
    }
    //Create Table method 1


    //Create Table method 2



    //Alter Table



    //Drop Table
    public void dropTable() {
        flag = false;
    }

}
