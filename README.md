# JAVA-CW-2023
[if23696]

# DB
## General design

[] [] USE
[] [] CREATE
[] [] INSERT
[] [] SELECT
[] [] UPDATE
[] [] ALTER
[] [] DELETE
[] [] DROP
[] [] JOIN


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

### Data Storage
```JAVA
interface Storage{
    String name;
    boolean flag;
    public void create;
    public void drop;
}

class Databases implements Storage{
    HashMap<Integer, String> tables;

}
class Table implements Storage{
    ArrayList<Rowdata> datas;
}
class Rowdata implements Storage{
    public int id;
    public String data;

}


class DataReader{
    //reads in the current data 

    //print them on the terminal
}

```
### HandleComments
```
interface CommandHandler
USE: switches the database against which the following queries will be run
CREATE: constructs a new database or table (depending on the provided parameters)
INSERT: adds a new record (row) to an existing table
SELECT: searches for records that match the given condition
UPDATE: changes the existing data contained within a table
ALTER: changes the structure (columns) of an existing table
DELETE: removes records that match the given condition from an existing table
DROP: removes a specified table from a database, or removes the entire database
JOIN: performs an inner join on two tables (returning all permutations of all matching records)

USE\CREATE\INSERT\UPDATE\ALTER\DELETE\DROP->related to file system
SELECT->JOIN: only output, but doesn't change the file.

```



![](2024-03-11-150434.png)
1.delete, regenerate the table:
generate a txt.file:```database.txt```
Hashmap<String>(table name): current id
generate a txt.file:```database_delete.txt```
Hashmap<String>(table name): deleted id

## Progress
### 10/3：
1. √ Added the whole database-Table-Rowdata structures, use Datareader to read-in tab file and print them on the terminal

### 11/3:
1. √ Use the Tokenrise given to cope with commands.
2. √ change all the methods dealing with file systems to DBserver
3.  generate a txt.file and a delete.txt.file
      1. √ (tested, during the test, create a Globalstatus to track the current database and table the user is dealing with) generate new files when creating new database
      2. √ update these two files when creating new tables(used Hashmap)
    When testing, when created a new table, instead of append the new ones in the file, it overwrite it.
      3. update these two files when dealing with insert and delete in tables
          1. tables:insert/delete
          2. update the latest ID in these two files
4. √ drop for databases and files
5. √ Tests for generating folders and files and drop
6. USE methods in DATAreader
    1. √ USE->USE DATABASE,check if it is exists, set the globalstatus, generate a new hashmap based on the current files. generate new TXT based on the hashmap.
    2. √ INSERT INTO->USE table, check if it is exists on globalDATABASE, set the globalstatus.
 7. √ insert data in Tables and update in TXT
 8. delete data in Tables and update in TXT

What BNF is DONE:
1. CREATE DATABASE/CREATE TABLE
2. DROP DATABASE/DROP TABLE
3. USE database, refer to TABLE
UPDATE, INSERT INTO, Alter, Delete refers to TABLE
```
Database:
//insert into function
updateTableLatestID(String tablename){
    1. parts[1]:empty
    ->if table-file line[0].equals("")->error:"Missing attributes"
    ->if table.file lines[1].equals("")->update to 1
    -> find the biggest index in table.datas.biggest Index
    2. parts[1]: contains number->update to parts[1]+1
}
// //given a tablename, return the id
// public int getTableID(String tablename){
//     getTable.(tablename);

//     //search for the table name in the txt:


// }

public void updateTableDeleteID( String tablename, int id){



}
public Table getTable(String tablename){
    //given a tablename, return its table using the hashmap

}
```
USE DATABASE->initialize anycurrent files in hashmap
table->
1. empty
2. only contains attribute
3. have contents





### 12/3:
Added CommandHandler.
Developing Engineering's game.


### 13/3:
1. Dealing with Command line.
    1. USE: done
    2. CREATE:
        '''create;''' will caused exit ERROR?
        Hasn't debug yet?
    3. DROP:Hasn't tested yet?
2. Alter
ADD: 
DROP: delete 
->DataReader->write a function to write current table back to file.
3. Today sticked with CREATE and DROP debugs!!!!!!!!!!!!!!!!!
Change the logic of dropDatabase->boolean->fixed!
**USE**->can't select a existed folder: Database folder 'ghmyrqfeyg' does not exist in the specified path.
    ->dataReader->useDatabase->>>Path ERROR
                ->addTableToFile() the databasePath was not set.--->FIXED
**DROP**--->Fixed drop database
**CREATED**->Add already exist flag->DATABASE OK
            -> create table->doesn't do anything->Fixed
Mostly Done, small bugs

### 14/3:
  UPDATE, INSERT INTO, DELETE
```
<Insert>  ::=  "INSERT " "INTO " [TableName] " VALUES" "(" <ValueList> ")"
```



SELECT 
JOIN
ALTER