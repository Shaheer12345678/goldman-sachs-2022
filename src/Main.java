import java.io.*; 
import java.util.*;

/** Minimal CSV parser + anomaly detector using rolling z-score. */
public class Main {
    static class Tx {
        String id; long ts; double value;
        Tx(String id, long ts, double value){ this.id=id; this.ts=ts; this.value=value; }
    }

    public static void main(String[] args) throws Exception {
        if (args.length==0) {
            System.err.println("Usage: java Main <csv>");
            return;
        }
        List<Tx> list = readCsv(args[0]);
        double mean = 0, m2 = 0; // Welford online variance
        int n = 0;
        double threshold = 3.0; // z-score
        System.out.println("id,timestamp,value,z,anomaly");
