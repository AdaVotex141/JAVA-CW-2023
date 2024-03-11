# JAVA-CW-2023
[if23696]

# DB
## General design
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
```JAVA
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
* 10/3：
1. √ Added the whole database-Table-Rowdata structures, use Datareader to read-in tab file and print them on the terminal

* 11/3:
1. √ Use the Tokenrise given to cope with commands.
2. √ change all the methods dealing with file systems to DBserver
3.  generate a txt.file and a delete.txt.file
      1. √ (tested, during the test, create a Globalstatus to track the current database and table the user is dealing with) generate new files when creating new database
      2. update these two files when creating new tables(used Hashmap)
      3. update these two files when dealing with insert and delete in tables
          1. tables:insert/delete
          2. update the latest ID in these two files
4. √ drop for databases and files

