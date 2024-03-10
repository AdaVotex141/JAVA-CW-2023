# JAVA-CW-2023
[if23696]

# DB
1. get the server and client connected:
```mvnw exec:java -Dexec.mainClass="edu.uob.DBServer"```
```mvnw exec:java -Dexec.mainClass="edu.uob.DBClient"```
or
```./mvnw clean compile```
```./mvnw exec:java@server```
```./mvnw exec:java@client```
.mvn 
maven相关
src
test

2. Design
READ IN->file
DATABASE,TABLE-> low letter cases
id->self added->Arraylist+"String"
```JAVA
//ArrayList<Line>
/*class Line{
    public int line;
    public String data;
    protected boolean flag;
}
*/
```
1. deal with the file
```JAVA
class DataReader{
    //1. Read Data
    readDataFromFile();
    printData();
    //-----------------


interface Storage{
    String name;//how to deal with names.
    boolean flag;

}

class Databases{
    String databaseName;
    protected boolean flag;
    Hashmap<Integer,String> tables=new Hashmap<Integer,String>();
    //Stirng is for table names. Whenever creates a new ,the Integer should always be 1 or 0
    //CREATE DATABASE;

    //DROP DATABASE;

}

class Table{
    String tableName;
    String attributeName;//the 0th row of the file/table
    public int tableSize;
    ArrayList<RowData> dataList=new ArrayList<RowData>();
    protected boolean flag;
    //CREATE TABLE;
    //DROP TABLE;

}

class RowData{
    //-----------------
    public int id;//starts from 1
    public String data;
    protected boolean flag;
    
}
```
![](2024-03-09-214016.png)
2. Handling the COMMAND

```JAVA
public interface CommandHandler();
```JAVA
public interface CommandHandler();
<CommandType>     ::=  <Use> | <Create> | <Drop> | <Alter>|<Insert> | <Select> | <Update> | <Delete> | <Join>

```


1. dealing with whitespace
2. do the tokens




```