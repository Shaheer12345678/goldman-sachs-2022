import java.io.*; 
import java.util.*;

/** Minimal CSV parser + anomaly detector using rolling z-score. */
public class Main {
    static class Tx {
        String id; long ts; double value;
        Tx(String id, long ts, double value){ this.id=id; this.ts=ts; this.value=value; }
    }

