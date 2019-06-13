/* SimpleApp.scala */
import org.apache.spark.sql.SparkSession

object PlusOneMinusOne {
    def main(args: Array[String]) {
        val filePath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate.tsv"
        val spark = SparkSession.builder.appName("Count +1/-1").getOrCreate()
        val rootComments = spark.read.textFile(filePath).cache()

        val countAll = rootComments.map(line => line.split("\t")(1))
                        .filter(title => title.contains("+1/-1"))
                        .count()
        println(s"Numero total de encuestas +1/-1: $countAll")
        spark.stop()
    }    
}