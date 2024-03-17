package edu.uob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Rowdata{
    private int id;
    private String data;
    //private ArrayList<Data> dataList;
    protected boolean flag;
    protected boolean selected;
    protected boolean updated;
    public Rowdata(int id,String data){
        this.id=id;
        this.data=data;
        this.flag=true;
        this.selected=false;
        this.updated=true;
    }


    public int getid(){
        return this.id;
    }
    public String getData(){
        return this.data;
    }
    public String[] getDataSplit(){
        System.out.print(data+"\n");
        String[] dataBox=this.data.split("\t");
        for(String data:dataBox){
            System.out.print(data+"\t");
        }
        System.out.print("\n");
        return dataBox;
    }
    public void setData(String data){
        this.data=data;
    }


}
