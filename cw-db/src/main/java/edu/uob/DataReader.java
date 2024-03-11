package edu.uob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class DataReader {

    /*
    ReadTabFile
    useDatabase
    useTable
     */

    //read a tab file and generate it to a table.
    public static void readTabFile(Database database,Table table, String path) {
        //StringBuilder content = new StringBuilder();
        String filePath=path + File.separator + database.name + File.separator + table.name + ".tab";;
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
                //add the data to the
                Rowdata rowData = new Rowdata(id,data);
                table.datas.add(rowData);
            }
        } catch (IOException e) {
            System.err.println("Error reading the tab file: " + e.getMessage());
            e.printStackTrace();
        }
    }
//-----------------USE, I put use in this class because it is one folder/file to be search for.--------
//USE DATABASE & TABLE
    public void useDatabase(String searchFolder,String path){
        //check whether the folder exists in the folderpath.
        String databaseFolderPath = path + File.separator + searchFolder;
        File databaseFolder = new File(databaseFolderPath);
        if (databaseFolder.exists() && databaseFolder.isDirectory()) {
            //if exists, set the globalstatus to this database.
            Globalstatus.getInstance().setCurrentDatabase(new Database(searchFolder));
            System.out.println("Database '" + searchFolder + "' is now in use.");
            //use the tables in the folder to generate a hashmap
            File[] files = databaseFolder.listFiles();
            if (files != null){
                for(File file:files){
                    //databaseFolder.tables.add("",new tables);
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".tab")){
                        String tableName = file.getName().toLowerCase().replace(".tab", "");
                        Table newTable = new Table();
                        newTable.name = tableName;
                        newTable.tableFilePath = file.getAbsolutePath();
                        Globalstatus.getInstance().getCurrentDatabase().tables.put(tableName, newTable);
                        //use the hashmap to generate two TXT files.
                        Globalstatus.getInstance().getCurrentDatabase().addTableToFile();
                    }
                }

            }
        } else {
            System.err.println("Database folder '" + searchFolder + "' does not exist in the specified path.");
        }
    }
    //INSERT INTO [TABLE]
    public void useTable(String searchFile,String path){
        //check whether the folder exists in the folderpath.
        String FilePath = path + File.separator + Globalstatus.getInstance().getCurrentDatabase()
                + searchFile + ".tab";
        File searchTable = new File(FilePath);
        if (searchTable.exists() && searchTable.isFile()) {
            //if exists, set the globalstatus to this database.
            //don't need a new Table, because it has been done in USE DATABASE
            Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
            Table currentTable=currentDatabase.tables.get(searchFile);
            Globalstatus.getInstance().setCurrentTable(currentTable);
            //Reads in the current Table.
            readTabFile(currentDatabase,currentTable,path);
            System.out.println("Table'" + searchFile + "' is now in use.");
        } else {
            System.err.println("Database folder '" + searchFile + "' does not exist in the specified path.");
        }
    }

    //print an entire table
//    public static void printTabFile(Table table) {
//        System.out.println(table.getAttribute());
//        for (Rowdata rowdata : table.datas) {
//            if (rowdata.flag==true){
//                System.out.println(rowdata.getid() + "\t" +rowdata.getData());
//            }else{
//                System.out.println(rowdata.getid()+"");
//            }
//        }
//    }


}
