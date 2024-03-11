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
                String data = parts.length > 1 ? parts[1] : "";
                Rowdata rowData = new Rowdata(id,data);
                table.datas.add(rowData);
                //content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading the tab file: " + e.getMessage());
            e.printStackTrace();
        }
        //return content.toString();
    }
//    public static void printTabFile(String filePath) {
//        String fileContent = readTabFile(filePath);
//        System.out.println(fileContent);
//    }


}
