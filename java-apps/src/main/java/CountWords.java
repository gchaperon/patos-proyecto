import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CountWords {
    public static void main(String[] args) throws IOException {
        // stop words

        Set<String> set = new HashSet<>();
        String stopWordsPath = "/home/gabriel/2019-1/patos/proyecto/data/stop_words.txt";
        try (Stream<String> stream = Files.lines(Paths.get(stopWordsPath))) {
            stream.forEach(set::add);
        }

        String rootsPath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate_fixed.tsv";
        String childsPath = "/home/gabriel/2019-1/patos/proyecto/data/child_all_wrepeat_tsdate_fixed.tsv";

        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        JavaRDD<String> inputRootsRDD = spark.textFile(rootsPath);
        JavaRDD<String> inputChildsRDD = spark.textFile(childsPath);

        System.out.println("\nTop ten palabras en todos lados:");

        inputRootsRDD
                .map(line -> line.split("\t")[1].toLowerCase()) // se extrae el titulo
                .union(inputRootsRDD.map(line -> line.split("\t")[5].toLowerCase())) // se unen con los mensajes
                .union(inputChildsRDD.map(line -> line.split("\t")[5].toLowerCase())) // se unen con los mensajes de los child
                .flatMap(text -> Arrays.asList(text.split(" ")).iterator()) // se separan las palabras
                .filter(word -> !set.contains(word))
                .mapToPair(word -> new Tuple2<>(word, 1L)) // se le asigna un uno a cada palabra
                .reduceByKey(Long::sum) // se suman las ocurrencias
                .mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1)) // se invierte para ordenar
                .sortByKey(false) // de mayor a menor
                .take(10)
                .forEach(System.out::println);

    }
}
