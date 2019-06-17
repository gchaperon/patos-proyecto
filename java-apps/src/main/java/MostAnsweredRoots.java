import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class MostAnsweredRoots {
    public static void main(String[] args) {
        String rootsPath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate_fixed.tsv";
        String childsPath = "/home/gabriel/2019-1/patos/proyecto/data/child_all_wrepeat_tsdate_fixed.tsv";

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("MostAnsweredRoots"));

        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath);
        JavaRDD<String> inputsRootsRDD = spark.textFile(rootsPath);

        JavaPairRDD<String, Integer> childParentRDD = inputChildsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[1],
                        1
                )
        ).reduceByKey(Integer::sum);

        JavaPairRDD<String, String> idAndTitleRDD = inputsRootsRDD.mapToPair(
                line -> new Tuple2<>(
                        line.split("\t")[0],
                        line.split("\t")[1]
                )
        );

        JavaPairRDD<String, Tuple2<String, Integer>> rootTitleAndOccurrences = idAndTitleRDD.join(childParentRDD);

        JavaPairRDD<String, Integer> r = rootTitleAndOccurrences.mapToPair(tup -> tup._2);

        r.sortByKey(false).foreach(System.out::println);
    }

}
