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

1. Data Storage
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
2.HandleComments
```JAVA
interface CommandHandler
```