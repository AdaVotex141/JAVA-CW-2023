package edu.uob;
import java.util.ArrayList;

public class Rowdata{
    private int id;
    private String data;
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


}
