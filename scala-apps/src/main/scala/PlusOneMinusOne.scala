import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object PlusOneMinusOne {
  def main(args: Array[String]) {
    val filePath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate.tsv"
    val spark = new SparkContext(new SparkConf().setAppName("Count +1/-1"))

    val rootComments = spark.textFile(filePath)

    val countAll = rootComments.filter(line => line.split("\t").size.equals(1)).count()
    println(s"Numero total de encuestas +1/-1: $countAll")
    spark.stop()
  }
}