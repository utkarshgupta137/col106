public class TreeMap<K extends Comparable<K>, V> {
    private class Node implements Comparable<K> {
        @Override
        public int compareTo(K key) {
            return this.key.compareTo(key);
        }

        public K key;
        public V value;
        public int height;
        public Node left, right;

        private int getBalance() {
            int h1 = 0;
            if (left != null) {
                h1 = left.height;
            }
            int h2 = 0;
            if (right != null) {
                h2 = right.height;
            }
            return h1 - h2;
        }

        private Node updateHeight() {
            int h1 = 0;
            if (left != null) {
                h1 = left.height;
            }
            int h2 = 0;
            if (right != null) {
                h2 = right.height;
            }
            height = 1 + (h1 > h2 ? h1 : h2);
            return this;
        }

        private Node rotateLeft(Node node) {
            Node t = node.right;
            node.right = t.left;
            t.left = node.updateHeight();
            return t.updateHeight();
        }

        private Node rotateRight(Node node) {
            Node t = node.left;
            node.left = t.right;
            t.right = node.updateHeight();
            return t.updateHeight();
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node balance() {
            if (getBalance() > 1) {
                if (left.getBalance() < 0) {
                    left = rotateLeft(left);
                }
                return rotateRight(this);
            } else if (getBalance() < -1) {
                if (right.getBalance() > 0) {
                    right = rotateRight(right);
                }
                return rotateLeft(this);
            }
            return this;
        }
    }

    private Node root;

    private Node put(K key, V value, Node node) {
        if (node == null) {
            return new Node(key, value);
        }

        int compare = node.compareTo(key);
        if (compare > 0) {
            node.left = put(key, value, node.left);
        } else if (compare < 0) {
            node.right = put(key, value, node.right);
        }
        return node.balance();
    }

    private Node remove(K key, Node node) {
        int compare = node.compareTo(key);
        if (compare > 0) {
            node.left = remove(key, node.left);
        } else if (compare < 0) {
            node.right = remove(key, node.right);
        } else {
            if (node.left == null && node.right == null) {
                return null;
            } else if (node.left == null) {
                node = node.right;
            } else if (node.right == null) {
                node = node.left;
            } else {
                Node temp = node.right;
                while (temp.left != null) {
                    temp = temp.left;
                }
                node.value = temp.value;
                node.right = remove(temp.key, node.right);
            }
        }
        return node.balance();
    }

    private V get(K key, Node node) {
        int compare = node.compareTo(key);
        if (compare > 0) {
            return get(key, node.left);
        } else if (compare < 0) {
            return get(key, node.right);
        }
        return node.value;
    }

    public TreeMap(K key, V value) {
        root = new Node(key, value);
    }

    public void put(K key, V value) {
        root = put(key, value, root);
    }

    public void remove(K key) {
        root = remove(key, root);
    }

    public V get(K key) {
        return get(key, root);
    }
}
