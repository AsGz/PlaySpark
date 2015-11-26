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
