package edu.uob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class DataReader {
//use String filePath for reading.
    public static void readTabFile(Database database,Table table) {
        //StringBuilder content = new StringBuilder();
        String filePath="databases" + File.separator + database.name + File.separator + table.name + ".tab";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            table.attribute = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                int id = Integer.parseInt(parts[0]);
                String data;
                if (parts.length > 1) {
                    data = parts[1];
                } else {
                    data = "";
                }
                Rowdata rowData = new Rowdata(id,data);
                table.datas.add(rowData);
            }
        } catch (IOException e) {
            System.err.println("Error reading the tab file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void printTabFile(Table table) {
        System.out.println(table.attribute);
        for (Rowdata rowdata : table.datas) {
            System.out.println(rowdata.id + "\t" +rowdata.data);
        }
    }


}
