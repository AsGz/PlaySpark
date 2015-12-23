/*
mysql> describe gps_location;;
+--------------+--------------+------+-----+---------+----------------+
| Field        | Type         | Null | Key | Default | Extra          |
+--------------+--------------+------+-----+---------+----------------+
| id           | int(11)      | NO   | PRI | NULL    | auto_increment |
| user_id      | int(11)      | YES  | MUL | NULL    |                |
| user_mail    | varchar(255) | YES  | MUL | NULL    |                |
| longitude    | double       | YES  |     | NULL    |                |
| latitude     | double       | YES  |     | NULL    |                |
| country_code | varchar(25)  | YES  | MUL | NULL    |                |
| country_name | varchar(255) | YES  | MUL | NULL    |                |
| created_at   | datetime     | YES  |     | NULL    |                |
| insert_at    | datetime     | YES  |     | NULL    |                |
| drone_type   | int(11)      | YES  |     | NULL    |                |
| province     | varchar(255) | YES  |     |         |                |
| gps_city     | varchar(255) | YES  |     |         |                |
+--------------+--------------+------+-----+---------+----------------+
*/


import org.apache.spark.{Logging, SparkContext, SparkConf}
import org.apache.spark.sql._
import java.sql.{DriverManager,  Connection}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.clustering.KMeans

object FlightApp {
        def main(args: Array[String]) {

                val conf = new SparkConf(true)
                .set("spark.cleaner.ttl", "3600")
                .setMaster("local[12]")
                .setAppName("FlightApp")

                // Connect to the Spark cluster:
                lazy val sc = new SparkContext(conf)

                val sqlContext = new org.apache.spark.sql.SQLContext(sc)
                // this is used to implicitly convert an RDD to a DataFrame.
                import sqlContext.implicits._
                val url="jdbc:mysql://127.0.0.1:3306/geo_info"
                val prop = new java.util.Properties
                prop.setProperty("user","geo")
                prop.setProperty("password","xxxxxxxx")

                println("task start...")

                val cnFlight = sqlContext.read.jdbc(url,"gps_location",Array("country_code='CN'"),prop)
                //val allCitys =  cnFlight.select("gps_city").distinct
                cnFlight.cache()
                cnFlight.registerTempTable("gps_location")

		//must be simply type, otherwise can't save to db
                var hotPonits = scala.collection.mutable.Map[String,String]();

		// first version. 
                /*allCitys.collect.foreach( c =>{
                                val cityName = c.getString(0)
                                val cityPoints = sqlContext.sql(s"select gps_city, longitude,latitude from gps_location where gps_city='$cityName'")
                                //cityPoints.show()
				
				//make vectior KMeans need
                                val vectors = cityPoints.rdd.map(r => Vectors.dense( r.getDouble(1), r.getDouble(2) ) )
                                vectors.cache()
				
                                val kMeansModel = KMeans.train(vectors, 5, 20)
                                println(s"train clusterCenters of $cityName :")
                                //kMeansModel.clusterCenters.foreach(println)
                                var pointsStr = ""
                                kMeansModel.clusterCenters.foreach(p => {pointsStr +=  p.apply(0).toString() + "," + p.apply(1).toString() + ";"})
                                hotPonits(cityName) = pointsStr
                })*/
		
		// here can be optimization use groupBy data
		val allCitys = cnFlight.map(r => (r.getAs[String]("gps_city"),(r.getAs[Double]("longitude"), r.getAs[Double]("latitude")))).groupByKey()
                allCitys.collect.foreach( c =>{
                        val cityName = c._1
                        val cityPoints = c._2.map(r => Vectors.dense( r._1, r._2 ) )
                        val vectors = sc.parallelize(cityPoints.toSeq)
                        vectors.cache()
                        val kMeansModel = KMeans.train(vectors, 5, 20)
                        println(s"train clusterCenters of $cityName :")
                        //kMeansModel.clusterCenters.foreach(println)
                        var pointsStr = ""
                        kMeansModel.clusterCenters.foreach(p => {pointsStr +=  p.apply(0).toString() + "," + p.apply(1).toString() + ";"})
                        hotPonits(cityName) = pointsStr
                })

		//turn to DataFrame for save to mysql table
                var df = sc.parallelize(hotPonits.toList).toDF("city","hot_points")
                df.write.jdbc(url,"city_hot_points_result",prop)
                println("task done..")
        }
}
