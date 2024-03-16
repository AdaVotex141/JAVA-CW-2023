package edu.uob;
import java.util.ArrayList;

public class Rowdata{
    private int id;
    private String data;
    //private ArrayList<Data> dataList;
    protected boolean flag;
    protected boolean selected;
    public Rowdata(int id,String data){
        this.id=id;
        this.data=data;
        this.flag=true;
        this.selected=false;
    }
    public int getid(){
        return this.id;
    }
    public String getData(){
        return this.data;
    }
    public String[] getDataSplit(){
        return this.data.split("\t");
    }
    public void setData(String data){
        this.data=data;
    }

}
