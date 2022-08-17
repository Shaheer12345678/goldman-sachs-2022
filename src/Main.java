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
        for (Tx t : list) {
            n++;
            double delta = t.value - mean;
            mean += delta / n;
            m2 += delta * (t.value - mean);
            double variance = (n>1) ? m2/(n-1) : 0.0;
            double std = Math.sqrt(variance);
            double z = (std>0) ? (t.value-mean)/std : 0.0;
            boolean anomaly = Math.abs(z) >= threshold && n>20;
            System.out.printf("%s,%d,%.2f,%.3f,%s%n", t.id, t.ts, t.value, z, anomaly);
        }
    }

    static List<Tx> readCsv(String path) throws Exception {
        List<Tx> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line; boolean headerSkipped=false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped){ headerSkipped=true; continue; }
