import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._
import java.util._

object TopCommenters {
  def main(args: Array[String]): Unit = {
    val rootsPath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate_fixed.tsv";
    val childsPath = "/home/gabriel/2019-1/patos/proyecto/data/child_all_wrepeat_tsdate_fixed.tsv";
    
    val spark = new SparkContext(new SparkConf().setAppName("Top commenters"));
    
    val rootRDD = spark.textFile(rootsPath);
    val rootWoenAndDateRDD = rootRDD.map(
      line => (
        line.split("\t")(2), 
        (new Date(
          (line.split("\t")(3).toDouble * 1000L).toLong
        )).getYear() + 1900
      )
    );

    val childRDD = spark.textFile(childsPath);
    val childWoenAndDateRDD = childRDD.map(
      line => (
        line.split("\t")(3),
        (new Date(
          (line.split("\t")(4).toDouble * 1000L).toLong
        )).getYear() + 1900
      )
    );

    
    val superWoenAndDateRDD = rootWoenAndDateRDD.union(childWoenAndDateRDD).cache();


//    def seqOp = (accumulator: PriorityQueue, element: (String, Long)) => {
//      if(accumulator.length < 10) {
//        return accumulator.enqueue(element)
//      } else {
//        if accumulator.head._2 < element._2 {
//          accumulator.dequeue()
//          return accumulator.enqueue(element)
//        } else {
//          return accumulator
//        }
//      }
//    }
 

    superWoenAndDateRDD
      .map(pair => (pair, 1L))
      .reduceByKey((u, v) => u + v)
      .map(row => (row._1._2, (row._1._1, row._2)))
      .take(10)
      .foreach(println)

    // superWoenAndDateRDD.take(10).foreach(println)



    // val groupedRDD = superWoenAndDateRDD.groupByKey().map(row => (row._1, row._2.size)).sortBy(_._2, false);

    // groupedRDD.take(30).foreach(println);

    println("Shitterton");

    spark.stop()
  } 
}