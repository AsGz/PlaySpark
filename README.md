# PlaySpark

##CassandraTest
read and write data from/to  cassandra in spark.

- you need install sbt and install sbt assembly plugin
- run step
```shell
~/CassandraTest $ sbt

> compile
[success] Total time: 0 s, completed 2015-11-26 10:11:50
>> assembly
..................
[warn] Strategy 'first' was applied to a file
[info] SHA-1: d2cb403e090e6a3ae36b08c860b258c79120fc90
[info] Packaging /Users/qpzhang/scala_code/CassandraTest/target/scala-2.10/CassandraTest-assembly-1.0.jar ...

~/project/spark-1.5.2-bin-hadoop2.6 $./bin/spark-submit --class "CassandraTestApp" --master local[4] ~/scala_code/CassandraTest/target/scala-2.10/CassandraTest-assembly-1.0.jar

```

##MLibKMeansExample
read and wirte data from/to mysql, and user MLib KMeansCluster for hot points.

- compile:
```shell
~/scala_code/PlaySpark/MLibkMeansExample $sbt
[info] Loading global plugins from /Users/qpzhang/.sbt/0.13/plugins
[info] Set current project to FlightApp (in build file:/Users/qpzhang/scala_code/PlaySpark/MLibkMeansExample/)
> assembly
[info] Updating {file:/Users/qpzhang/scala_code/PlaySpark/MLibkMeansExample/}mlibkmeansexample...
[info] Resolving org.fusesource.jansi#jansi;1.4 ...
[info] downloading https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.12/mysql-connector-java-5.1.12.jar ...
[info] 	[SUCCESSFUL ] mysql#mysql-connector-java;5.1.12!mysql-connector-java.jar (5797ms)
......................
[info] Done updating.
[info] Compiling 1 Scala source to /Users/qpzhang/scala_code/PlaySpark/MLibkMeansExample/target/scala-2.10/classes...
[info] Including: mysql-connector-java-5.1.12.jar
[info] Including: scala-library.jar
[info] Checking every *.class/*.jar file's SHA-1.
[info] Merging files...
[warn] Merging 'META-INF/INDEX.LIST' with strategy 'discard'
[warn] Merging 'META-INF/MANIFEST.MF' with strategy 'discard'
[warn] Strategy 'discard' was applied to 2 files
[info] SHA-1: e24f1a03a8ef09a2796ac4dd272dd173fe1a8b54
[info] Packaging /Users/qpzhang/scala_code/PlaySpark/MLibkMeansExample/target/scala-2.10/FlightApp-assembly-1.0.jar ...
[info] Done packaging.
[success] Total time: 417 s, completed 2015-12-22 10:16:59

```
- run:
```shell
./bin/spark-submit --driver-class-path ~/YourPath/FlightApp-assembly-1.0.jar --class "FlightApp" --master local[4] ~/YourPath/FlightApp-assembly-1.0.jar
```
PS: here need --driver-class-path to define jdbc class path, or can't find the jdbc driver.

