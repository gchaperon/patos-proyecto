import org.apache.spark.SparkConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.codehaus.janino.Java;
import scala.Tuple2;
import scala.Tuple3;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TopFighters {
    public static void main(String[] args) {
        String rootsPath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate_fixed.tsv";
        String childsPath = "/home/gabriel/2019-1/patos/proyecto/data/child_all_wrepeat_tsdate_fixed.tsv";

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath);
        JavaPairRDD<String, Tuple3<String, String, String>> fatherItselfRDD = inputRootsRDD.mapToPair(
                line -> {

                    return new Tuple2<>(line.split("\t")[0], // id de raiz
                            new Tuple3<>(line.split("\t")[2],"-"+line.split("\t")[0],line.split("\t")[0]));
                }
        );



        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath);
        JavaPairRDD<String, Tuple3<String, String, String>> childAndFatherRDD = inputChildsRDD.mapToPair(
                line -> {

                    return new Tuple2<>(line.split("\t")[0], // id del comentario
                    new Tuple3<>(line.split("\t")[3],line.split("\t")[2],line.split("\t")[1]));
                }
        );

        JavaPairRDD<String, Tuple3<String, String, String>> fatherFirstRDD = childAndFatherRDD.mapToPair(
                row -> new Tuple2<>(row._2._2(), new Tuple3<>(row._2._1(),row._1,row._2._3()))
        );

        JavaPairRDD<String, Tuple3<String, String, String>> allRDD = childAndFatherRDD.union(fatherItselfRDD);

        JavaPairRDD<String, Tuple2<Tuple3<String, String, String>,Tuple3<String, String, String>>> firstJoin = allRDD.join(fatherFirstRDD);

        firstJoin.mapToPair( row -> new Tuple2<>(
                row._2._2._2(),
                new Tuple2<>(
                        row._2._1(),
                        row._2._2()

                )))
                .join(fatherFirstRDD)
                .mapToPair( row -> new Tuple2<>(
                   row._2._2._3(),
                   new Tuple3<>(row._2._1._1._1(), row._2._1._2._1(), row._2._2._1())
                )).take(10).forEach(System.out::println);


    }
}
