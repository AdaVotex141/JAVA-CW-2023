package edu.uob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class DataReader {

    /*
    READ tab file and print tab file to the terminal.
    main
    DataReader.readTabFile(database,table);
    DataReader.printTabFile(table);
     */
    public static void readTabFile(Database database,Table table) {
        //StringBuilder content = new StringBuilder();
        DBServer path=new DBServer();
        String filePath=path.getStorageFolderPath()+ File.separator + database.name + File.separator + table.name + ".tab";;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            table.setAttribute(reader.readLine());
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                int id = Integer.parseInt(parts[0]);
                String data;
                if (parts.length > 1) {
                    data = parts[1];
                } else {
                    data = "";
                }
                //interact with rowdata and table
                Rowdata rowData = new Rowdata(id,data);
                table.datas.add(rowData);
            }
        } catch (IOException e) {
            System.err.println("Error reading the tab file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void printTabFile(Table table) {
        System.out.println(table.getAttribute());
        for (Rowdata rowdata : table.datas) {
            if (rowdata.flag==true){
                System.out.println(rowdata.getid() + "\t" +rowdata.getData());
            }else{
                System.out.println(rowdata.getid()+"");
            }
        }
    }


}
