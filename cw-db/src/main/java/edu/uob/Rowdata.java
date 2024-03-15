package edu.uob;
import java.util.ArrayList;

public class Rowdata{
    private int id;
    private String data;
    //private ArrayList<Data> dataList;
    protected boolean flag;
    public Rowdata(int id,String data){
        this.id=id;
        this.data=data;
        this.flag=true;
    }
    public int getid(){
        return this.id;
    }
    public String getData(){
        return this.data;
    }
    public void setData(String data){
        this.data=data;
    }

}
