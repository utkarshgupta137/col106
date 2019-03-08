import java.util.LinkedList;

public class TreeMap<K extends Comparable<K>, V extends Comparable<V>> {
    private class Node implements Comparable<K> {
        @Override
        public int compareTo(K k) {
            return this.k.compareTo(k);
        }

        public K k;
        public V v;
        public LinkedList<V> q;

        public int h;
        public Node l, r;

        private int getBalance() {
            int hl = 0;
            if (l != null) {
                hl = l.h;
            }
            int hr = 0;
            if (r != null) {
                hr = r.h;
            }
            h = 1 + (hl > hr ? hl : hr);
            return hl - hr;
        }

        private Node updateHeight() {
            int hl = 0;
            if (l != null) {
                hl = l.h;
            }
            int hr = 0;
            if (r != null) {
                hr = r.h;
            }
            h = 1 + (hl > hr ? hl : hr);
            return this;
        }

        private Node rotateLeft(Node n) {
            Node t = n.r;
            n.r = t.l;
            t.l = n.updateHeight();
            return t.updateHeight();
        }

        private Node rotateRight(Node n) {
            Node t = n.l;
            n.l = t.r;
            t.r = n.updateHeight();
            return t.updateHeight();
        }

        public Node(K k, V v) {
            this.k = k;
            this.v = v;

            this.h = 1;
            this.q = new LinkedList<V>();
        }

        public Node balance() {
            int b = getBalance();
            if (b > 1) {
                if (l.getBalance() < 0) {
                    l = rotateLeft(l);
                }
                return rotateRight(this);
            } else if (b < -1) {
                if (r.getBalance() > 0) {
                    r = rotateRight(r);
                }
                return rotateLeft(this);
            }
            return this;
        }
    }

    private Node r;

    private Node put(K k, V v, Node n) {
        if (n == null) {
            return new Node(k, v);
        } else if (k.compareTo(n.k) < 0) {
            n.l = put(k, v, n.l);
        } else if (k.compareTo(n.k) > 0) {
            n.r = put(k, v, n.r);
        } else {
            if (v.compareTo(n.v) < 0) {
                int i = 0;
                for (V q : n.q) {
                    if (v.compareTo(q) < 0) {
                        i++;
                    } else {
                        break;
                    }
                }
                n.q.add(i, v);
            } else {
                n.q.push(n.v);
                n.v = v;
            }
            return n;
        }
        return n.balance();
    }

    private Node remove(K k, V v, Node n) {
        if (n == null) {
            return null;
        } else if (k.compareTo(n.k) < 0) {
            n.l = remove(k, v, n.l);
        } else if (k.compareTo(n.k) > 0) {
            n.r = remove(k, v, n.r);
        } else {
            if (n.q.size() != 0) {
                if (!n.q.remove(v)) {
                    n.v = n.q.pop();
                }
                return n;
            } else if (n.l == null && n.r == null) {
                return null;
            } else if (n.l == null) {
                n = n.r;
            } else if (n.r == null) {
                n = n.l;
            } else {
                Node t = n.r;
                while (t.l != null) {
                    t = t.l;
                }

                n.k = t.k;
                n.v = t.v;
                n.q = t.q;

                t.q = new LinkedList<V>();
                n.r = remove(t.k, t.v, n.r);
                return n;
            }
        }
        return n.balance();
    }

    private V get(K k, Node n) {
        if (n == null) {
            return null;
        } else if (k.compareTo(n.k) < 0) {
            return get(k, n.l);
        } else if (k.compareTo(n.k) > 0) {
            return get(k, n.r);
        }
        return n.v;
    }

    private LinkedList<V> getAll(LinkedList<V> l, Node n) {
        if (n != null) {
            getAll(l, n.l);
            l.add(n.v);
            l.addAll(n.q);
            getAll(l, n.r);
        }
        return l;
    }

    public void put(K k, V v) {
        r = put(k, v, r);
    }

    public void remove(K k, V v) {
        r = remove(k, v, r);
    }

    public V getMax() {
        Node t = r;
        while (t.r != null) {
            t = t.r;
        }
        return t.v;
    }

    public V get(K k) {
        return get(k, r);
    }

    public LinkedList<V> getAll() {
        return getAll(new LinkedList<V>(), r);
    }
}
