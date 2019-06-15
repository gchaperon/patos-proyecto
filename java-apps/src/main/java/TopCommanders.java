import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TopCommanders {
    public static void main(String[] args) {
        String rootsPath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate_fixed.tsv";
        String childsPath = "/home/gabriel/2019-1/patos/proyecto/data/child_all_wrepeat_tsdate_fixed.tsv";

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath);
        JavaPairRDD<String, Integer> rootWoenAndDateRDD = inputRootsRDD.mapToPair(
            line -> new Tuple2<>(
                line.split("\t")[2],
                Utils.yearFromEpochString(line.split("\t")[3])
            )
        );

        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath);
        JavaPairRDD<String, Integer> childWoenAndDateRDD = inputChildsRDD.mapToPair(
            line -> new Tuple2<>(
                line.split("\t")[3],
                Utils.yearFromEpochString(line.split("\t")[4])
            )
        );

        JavaPairRDD<String, Integer> superWoenAndDateRDD = rootWoenAndDateRDD.union(childWoenAndDateRDD);

        superWoenAndDateRDD
            .mapToPair(pair -> new Tuple2<>(pair, 1L))
            .reduceByKey(Long::sum)
            .mapToPair(row -> new Tuple2<>(row._1._2, new Tuple2<>(row._1._1, row._2)))
            .groupByKey()
            .mapToPair(tup -> {
                PriorityQueue<Tuple2<String, Long>> pq = new PriorityQueue<>((t1, t2) -> t1._2.compareTo(t2._2));
                tup._2.forEach(pq::add);
                while (pq.size() > 10) pq.poll();
                List<Tuple2<String, Long>> sup = new ArrayList<>();
                while (!pq.isEmpty()) sup.add(0, pq.poll());
                return new Tuple2<>(tup._1, sup);
            })
            .sortByKey()
            .collect()
            .forEach(System.out::println);
    }
}
