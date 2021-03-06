import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SuffixTree {
    private static class Node {
        int u, v;
        List<Node> c;

        public Node(int u, int v, List<Node> c) {
            this.u = u;
            this.v = v;
            this.c = c;
        }

        public Node(int u, int v) {
            this(u, v, new ArrayList<>());
        }
    }

    private static Node root = new Node(0, 0);
    private static String text;

    private static void addSuffix(Node r, int u) {
        int v = text.length();
        Node q = null;
        while (v > u) {
            for (Node n : r.c) {
                if (text.substring(n.u, n.v).startsWith(text.substring(u, v))) {
                    if (text.substring(n.u, n.v).equals(text.substring(u, v))) {
                        addSuffix(n, v);
                    } else {
                        q = new Node(n.u, n.u + v - u);
                        q.c.add(new Node(v, text.length()));
                        q.c.add(new Node(n.u + v - u, n.v, n.c));
                        r.c.remove(n);
                    }
                    v = u;
                    break;
                }
            }
            v--;
        }
        if (v == u) {
            r.c.add(new Node(u, text.length()));
        } else if (q != null) {
            r.c.add(q);
            q = null;
        }
    }

    private static void printTree(Node r, int t) {
        for (Node n : r.c) {
            for (int i = 0; i < t; i++) {
                System.out.print("\t");
            }
            System.out.println(text.substring(n.u, n.v));
            printTree(n, t + 1);
        }
    }

    private static List<String> search(Node r, int a, int b, String s) {
        ArrayList<String> matches = new ArrayList<String>();
        if (s.isEmpty()) {
            System.out.println(r.u);
            matches.add(a + "\t" + b);
            return matches;
        } else {
            int u = 0;
            while (u != s.length() && s.charAt(u) == '?') {
                u++;
            }
            int v = u;
            while (v != s.length() && s.charAt(v) != '?') {
                v++;
            }

            boolean flag = true;
            while (v > 0 && flag) {
                for (Node n : r.c) {
                    if (text.substring(n.u, n.v).contains(s.substring(u, v)) && a == -1) {
                        a = text.indexOf(s.substring(u, v), n.u);
                        matches.addAll(search(n, a, a + s.length(), s.substring(v)));
                        flag = false;
                    } else if (text.substring(n.u, n.v).startsWith(s.substring(u, v))) {
                        matches.addAll(search(n, a, b, s.substring(v)));
                        matches.addAll(search(n, -1, 0, s));
                        if (flag) {
                            matches.addAll(search(r, -1, 0, s));
                        }
                        flag = false;
                    }
                }
                v--;
            }
        }
        return matches;
    }

    private static List<String> match(List<String> lines) {
        text = lines.get(0) + "$";
        for (int i = 0; i < text.length(); i++) {
            addSuffix(root, i);
        }
        printTree(root, 0);

        int n = Integer.valueOf(lines.get(1));
        ArrayList<String> output = new ArrayList<String>(n);
        for (int i = 2; i < n + 2; i++) {
            output.addAll(search(root, -1, 0, lines.get(i)));
        }
        System.out.println(output);
        return output;
    }

    public static void main(String[] args) {
        try {
            Files.write(Paths.get(args[1]), match(Files.readAllLines(Paths.get(args[0]))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
