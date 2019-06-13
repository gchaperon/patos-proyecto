/* SimpleApp.scala */
import org.apache.spark.sql.SparkSession

object EvenSimplerApp {
  def main(args: Array[String]) {
    // val logFile = "/home/gabriel/2019-1/patos/proyecto/data/child_all_wrepeat_tsdate.tsv" // Should be some file on your system
    val spark = SparkSession.builder.appName("Even Simpler Application").getOrCreate()
    // val logData = spark.read.textFile(logFile).cache()
    // val numAs = logData.filter(line => line.contains("a")).count()
    // val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Hello World")
    spark.stop()
  }
}