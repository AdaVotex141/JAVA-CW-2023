package edu.uob;
import java.util.ArrayList;
//import java.lang.StringBuilder;

public class RowData {
    public int id;
    public String data;
    protected boolean flag;
    public RowData(int id,String data,boolean flag){
        this.id=id;
        this.data=data;
        this.flag=flag;
    }
}
