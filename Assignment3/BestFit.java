import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

public class BestFit {
    private class Obj implements Comparable<Obj> {
        @Override
        public int compareTo(Obj obj) {
            return Integer.compare(this.id, obj.id);
        }

        public int id;
        public int size;
        public int binId;

        public Obj(int id, int size, int binId) {
            this.id = id;
            this.size = size;
            this.binId = binId;
        }
    }

    private class Bin implements Comparable<Bin> {
        @Override
        public int compareTo(Bin bin) {
            return Integer.compare(this.id, bin.id);
        }

        public int id;
        public int cap;
        public TreeMap<Integer, Obj> objs;

        public Bin(int id, int cap) {
            this.id = id;
            this.cap = cap;
            this.objs = new TreeMap<Integer, Obj>();
        }
    }

    private TreeMap<Integer, Bin> binsById;
    private TreeMap<Integer, Bin> binsByCap;
    private TreeMap<Integer, Obj> objs;

    public BestFit() {
        binsById = new TreeMap<Integer, Bin>();
        binsByCap = new TreeMap<Integer, Bin>();
        objs = new TreeMap<Integer, Obj>();
    }

    public void add_bin(int id, int cap) {
        Bin bin = new Bin(id, cap);
        binsById.put(id, bin);
        binsByCap.put(cap, bin);
    }

    public int add_object(int id, int size) {
        Bin bin = binsByCap.getMax();
        if (bin.cap >= size) {
            Obj obj = new Obj(id, size, bin.id);
            objs.put(id, obj);
            bin.objs.put(id, obj);
            System.out.println(bin.id);

            binsByCap.remove(bin.cap, bin);
            bin.cap -= size;
            binsByCap.put(bin.cap, bin);

            return bin.id;
        } else {
            System.out.println("No bin to hold obj of size: " + size);
        }
        return -1;
    }

    public int delete_object(int id) {
        Obj obj = objs.get(id);
        if (obj != null) {
            Bin bin = binsById.get(obj.binId);
            objs.remove(id, obj);
            bin.objs.remove(id, obj);
            System.out.println(bin.id);

            binsByCap.remove(bin.cap, bin);
            bin.cap += obj.size;
            binsByCap.put(bin.cap, bin);

            return bin.id;
        } else {
            System.out.println("No obj with id: " + id);
        }
        return -1;
    }

    public List<Pair<Integer, Integer>> contents(int id) {
        Bin bin = binsById.get(id);
        if (bin != null) {
            List<Pair<Integer, Integer>> l = new LinkedList<Pair<Integer, Integer>>();
            Pair<Integer, Integer> p;
            for (Obj obj : bin.objs.getAll()) {
                System.out.println(obj.id + " " + obj.size);
                p = new Pair<Integer, Integer>(obj.id, obj.size);
                l.add(p);
            }
            return l;
        } else {
            System.out.println("No bin with id: " + id);
        }
        return null;
    }

    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(args[0]));
        } catch (IOException e) {
            return;
        }

        BestFit bf = new BestFit();
        String[] line;
        for (String i : lines) {
            line = i.split(" ");
            switch (Integer.valueOf(line[0])) {
                case 1:
                        bf.add_bin(Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                        break;
                case 2:
                        bf.add_object(Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                        break;
                case 3:
                        bf.delete_object(Integer.valueOf(line[1]));
                        break;
                case 4:
                        bf.contents(Integer.valueOf(line[1]));
                        break;
            }
        }
    }
}
