import java.util.ArrayList;

public class HashMap {
    private class Node {
        public String s;
        public int v;

        public Node(String s, int v) {
            this.s = s;
            this.v = v;
        }
    }

    private int size;
    private Node[] list;

    private int hashCode(String s) {
        int h = 7;
        for (char c : s.toCharArray()) {
            h = 31*h + c;
        }
        return h & Integer.MAX_VALUE;
    }

    public HashMap(int c) {
        size = c;
        list = new Node[c];
    }

    public void put(String s, int v) {
        int h = hashCode(s);
        int i = 0;
        while (list[(h + i*i) % size] != null) {
            i++;
        }
        list[(h + i*i) % size] = new Node(s, v);
    }

    public int get(String s) {
        int h = hashCode(s);
        int i = 0;
        while (list[(h + i*i) % size] != null) {
            if (s.equals(list[(h + i*i) % size].s)) {
                return list[(h + i*i) % size].v;
            }
            i++;
        }
        return -1;
    }
}
