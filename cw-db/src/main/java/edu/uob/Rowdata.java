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

    public String[] getDataSplitWithID(){
        int size=this.getDataSplit().length+2;
        String[] dataBox = new String[size];
        dataBox[0] = Integer.toString(this.id);
        String[] dataSplit = this.getDataSplit();
        for (int i = 0; i < dataSplit.length; i++) {
            dataBox[i + 1] = dataSplit[i];
        }
        return dataBox;
    }


    public void setData(String data){
        this.data=data;
    }


}
