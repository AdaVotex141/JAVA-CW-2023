package edu.uob;
import java.io.*;
public class DataReader {

    /*
    THIS CLASS is for dealing with changes between files\repo and Table\Database
    1/ReadTabFile:Read in a Tab file and turn it into a Table
    //TODO: skip the deleted row and get the latestID
    2/useDatabase:Read in a repo ,set the current database and generate a hashmap <tableName,table>
    3/useTable:Read in a table, set the current table and turn every file-data to ArrayList
    4/writeTabFile:Given a table, write the current ArrayList to file
    //TODO: (.txt:skipped deleted rows)
     */
    public DataReader(){

    }

    //read a tab file and generate it to a table.
    public void readTabFile(Database database,Table table, String path) {
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
    //
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
                        readTabFile(Globalstatus.getInstance().getCurrentDatabase(),newTable,path);
                        Globalstatus.getInstance().getCurrentDatabase().tables.put(tableName, newTable);
                        //use the hashmap to generate two TXT files.
                        Globalstatus.getInstance().getCurrentDatabase().addTableToFile();
                        //update the newest if exists to these Two TXT files.
                    }
                }

            }
        } else {
            System.err.println("Database folder '" + searchFolder + "' does not exist in the specified path.");
        }
    }
    //useTable->set currentTable to this table.
    public boolean useTable(String searchFile,String path){
        String FilePath = path + File.separator + Globalstatus.getInstance().getCurrentDatabase()
                + searchFile + ".tab";
        File searchTable = new File(FilePath);
        if (searchTable.exists() && searchTable.isFile()) {
            //if exists, set the globalstatus to this database.
            //don't need a new Table, because it has been done in USE DATABASE
            Database currentDatabase=Globalstatus.getInstance().getCurrentDatabase();
            Table currentTable=currentDatabase.tables.get(searchFile);
            Globalstatus.getInstance().setCurrentTable(currentTable);
            System.out.println("Table'" + searchFile + "' is now in use.");
        } else {
            System.err.println("Database folder '" + searchFile + "' does not exist in the specified path.");
            return false;
        }
        return true;
    }
    //write the table back to file
    // Write the contents of a table back to its corresponding tab file
    public void writeTabFile(Table table, String tableFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath))) {
            // Write the attribute line
            writer.write(table.getAttribute());
            writer.newLine();
            // Write each row data
            for (Rowdata rowdata : table.datas) {
                if (rowdata.flag) {
                    // If the row is not deleted, write id and data separated by a tab
                    writer.write(rowdata.getid() + "\t" + rowdata.getData());
                } else {
                    //TODO: get the deleted row in txt file, but I don't think it is nessasary.
//                    // If the row is deleted, write only the id
//                    writer.write(rowdata.getid() + "");
                }
                writer.newLine();
            }
            System.out.println("Table '" + table.name + "' written back to file.");
        } catch (IOException e) {
            System.err.println("Error writing the tab file: " + e.getMessage());
            e.printStackTrace();
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
