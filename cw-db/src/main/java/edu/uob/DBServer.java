package edu.uob;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;

/** This class implements the DB server. */
public class DBServer {

    private static final char END_OF_TRANSMISSION = 4;
    private String storageFolderPath;

    public static void main(String args[]) throws IOException {
        DBServer server = new DBServer();
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature otherwise we won't be able to mark your submission correctly.
    */
    public DBServer() {
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        try {
            // Create the database storage folder if it doesn't already exist !
            Files.createDirectories(Paths.get(storageFolderPath));
        } catch(IOException ioe) {
            System.out.println("Can't seem to create database storage folder " + storageFolderPath);
        }
    }

    public String getStorageFolderPath(){
        return storageFolderPath;
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming DB commands and carries out the required actions.
    */
    public String handleCommand(String command) throws IOException {
        // TODO implement your server logic here
        StringBuilder returnBuilder=new StringBuilder("");
        Tokenrise tokenrise=new Tokenrise(command);
        //CommandHandler commandHandler=new CommandHandler(storageFolderPath);
        ArrayList<String> tokens=tokenrise.getTokens();
        String firstToken = tokens.get(0).toLowerCase();
        if (firstToken.equals("create")){
            CreateHandler createHandler=new CreateHandler(storageFolderPath);
            returnBuilder=createHandler.create(tokens,returnBuilder);
        }else if(firstToken.equals("insert")){
            InsertHandler insertHandler=new InsertHandler(storageFolderPath);
            returnBuilder=insertHandler.insert(tokens,returnBuilder);
        }else if(firstToken.equals("select")){
            SelectHandler selectHandler=new SelectHandler(storageFolderPath);
            returnBuilder=selectHandler.select(tokens,returnBuilder);
            //commandHandler.select(tokens,returnBuilder);
        }else if(firstToken.equals("update")){
            UpdateHandler updateHandler=new UpdateHandler(storageFolderPath);
            returnBuilder=updateHandler.update(tokens,returnBuilder);
            //commandHandler.update(tokens,returnBuilder);
        }else if(firstToken.equals("alter")){
            AlterHandler alterHandler=new AlterHandler(storageFolderPath);
            returnBuilder=alterHandler.alter(tokens,returnBuilder);
        }else if(firstToken.equals("delete")){
            DeleteHandler deleteHandler=new DeleteHandler(storageFolderPath);
            returnBuilder=deleteHandler.delete(tokens,returnBuilder);
            //commandHandler.delete(tokens,returnBuilder);
        }else if(firstToken.equals("join")){
            JoinHandler joinHandler=new JoinHandler(storageFolderPath);
            returnBuilder=joinHandler.join(tokens,returnBuilder);
        }else if(firstToken.equals("drop")){
            DropHandler dropHandler=new DropHandler(storageFolderPath);
            returnBuilder=dropHandler.drop(tokens,returnBuilder);
            //commandHandler.drop(tokens, returnBuilder);
        }else if(firstToken.equals("use")){
            UseHandler useHandler=new UseHandler(storageFolderPath);
            returnBuilder=useHandler.use(tokens,returnBuilder);
        }else{
            returnBuilder.append("[ERROR] Can't resolve command!");
        }
        String returnCommand=returnBuilder.toString();
        return returnCommand;
    }

    //  === Methods below handle networking aspects of the project - you will not need to change these ! ===

    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.err.println("Server encountered a non-fatal IO error:");
                    e.printStackTrace();
                    System.err.println("Continuing...");
                }
            }
        }
    }

    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

            System.out.println("Connection established: " + serverSocket.getInetAddress());
            while (!Thread.interrupted()) {
                String incomingCommand = reader.readLine();
                System.out.println("Received message: " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();
            }
        }
    }
}
