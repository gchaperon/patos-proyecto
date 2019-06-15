import org.apache.spark.SparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class PlusOneMinusOne {
    public static void main(String[] args) {
        String filePath = "/home/gabriel/2019-1/patos/proyecto/data/root_all_wrepeat_tsdate_fixed.tsv";
        JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("Count +1/-1"));

        JavaRDD<String> inputRDD= spark.textFile(filePath);
        long countAll = inputRDD.filter(line -> line.split("\t")[1].contains("+1/-1")).count();

        System.out.println("\t\tMira ctm, la wea es asi" + countAll);

        spark.close();
    }
}
