import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bucket {
    private static class Node {
        public List<Long> list = new ArrayList<Long>();
    }

    public static void main(String[] args) {
        int n = Integer.valueOf(args[0]);
        Node[] list = new Node[n];

        for (int i = 0; i < n; i++) {
            list[i] = new Node();
        }

        List<Long> input = new ArrayList<Long>();
        long x;
        int y;
        for (int i = 0; i < n; i++) {
            x = (long) (Math.random()*(Math.pow(n, 2) + 1));
            y = (int) x/n;
            input.add(x);
            list[y].list.add(x);
        }

        //Collections.sort(input);

        int max = 0;
        List<Long> output = new ArrayList<Long>();
        for (int i = 0; i < n; i++) {
            Collections.sort(list[i].list);
            output.addAll(list[i].list);
            max = Math.max(max, list[i].list.size());
        }

        System.out.println(max);
    }
}
