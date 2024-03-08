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

2. **Task 1**
 reads in the data from the sample data file using the Java File IO API
 make use of the File.separator constant
invalid formatting