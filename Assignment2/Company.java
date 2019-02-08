import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {
    private static class Employee implements Comparable<Employee> {
        @Override
        public int compareTo(Employee employee) {
            return this.name.compareTo(employee.name);
        }

        public int rank;
        public String name;
        public Employee boss;
        public ArrayList<Employee> employees;

        public Employee(int rank, String name, Employee boss) {
            this.rank = rank;
            this.name = name;
            this.boss = boss;
            employees = new ArrayList<Employee>();
        }
    }

    private static class Tree {
        private class Node implements Comparable<String> {
            @Override
            public int compareTo(String name) {
                return this.employee.name.compareTo(name);
            }

            public int height;
            public Employee employee;
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

            public Node(Employee employee) {
                this.employee = employee;
            }

            public Node balance() {
                updateHeight();
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

        private Node insert(Employee employee, Node node) {
            if (node == null) {
                return new Node(employee);
            }

            int compare = node.employee.compareTo(employee);
            if (compare > 0) {
                node.left = insert(employee, node.left);
            } else if (compare < 0) {
                node.right = insert(employee, node.right);
            }
            return node.balance();
        }

        private Node remove(String name, Node node) {
            int compare = node.compareTo(name);
            if (compare > 0) {
                node.left = remove(name, node.left);
            } else if (compare < 0) {
                node.right = remove(name, node.right);
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
                    node.employee = temp.employee;
                    node.right = remove(temp.employee.name, node.right);
                }
            }
            return node.balance();
        }

        private Employee search(String name, Node node) {
            int compare = node.compareTo(name);
            if (compare > 0) {
                return search(name, node.left);
            } else if (compare < 0) {
                return search(name, node.right);
            }
            return node.employee;
        }

        public Tree(Employee employee) {
            root = new Node(employee);
        }

        public void insert(Employee employee) {
            root = insert(employee, root);
        }

        public void remove(String name) {
            root = remove(name, root);
        }

        public Employee search(String name) {
            return search(name, root);
        }
    }

    private static Employee root;
    private static Tree tree;

    private static boolean searchEmployee(Employee employeeE, Employee bossE, Employee ignoreE) {
        if (employeeE.rank == bossE.rank) {
            return false;
        } else if (bossE.employees.contains(employeeE)) {
            return true;
        }

        for (Employee newBossE : bossE.employees) {
            if (newBossE.name != ignoreE.name && searchEmployee(employeeE, newBossE, ignoreE)) {
                return true;
            }
        }
        return false;
    }

    private static void printEmployees(ArrayList<Employee> bosses) {
        Collections.sort(bosses);
        ArrayList<Employee> newBosses = new ArrayList<Employee>();
        for (Employee bossE : bosses) {
            System.out.println(bossE.rank + " " + bossE.name);
            newBosses.addAll(bossE.employees);
        }

        if (newBosses.size() > 0) {
            printEmployees(newBosses);
        }
    }

    private static void addEmployee(String employee, String boss) {
        Employee bossE = tree.search(boss);
        Employee employeeE = new Employee(bossE.rank + 1, employee, bossE);

        bossE.employees.add(employeeE);
        tree.insert(employeeE);
    }

    private static void removeEmployee(String oldBoss, String newBoss) {
        Employee oldBossE = tree.search(oldBoss);
        Employee newBossE = tree.search(newBoss);

        for (Employee employeeE : oldBossE.employees) {
            employeeE.boss = newBossE;
            newBossE.employees.add(employeeE);
        }

        oldBossE.boss.employees.remove(oldBossE);
        tree.remove(oldBoss);
    }

    private static void commonBoss(String employee, String boss) {
        Employee employeeE = tree.search(employee);
        Employee commonBossE = tree.search(boss);
        Employee ignoreE = employeeE;

        while (!searchEmployee(employeeE, commonBossE, ignoreE)) {
            ignoreE = commonBossE;
            commonBossE = commonBossE.boss;
        }

        System.out.println(commonBossE.rank + " " + commonBossE.name);
    }

    private static void printEmployees() {
        System.out.println("1 " + root.name);
        printEmployees(root.employees);
    }

    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String[] input = lines.toArray(new String[0]);
        String[] line;

        root = new Employee(1, input[1].split(" ")[1], null);
        tree = new Tree(root);

        int e = Integer.valueOf(input[0]);
        for (int i = 1; i < e; i++) {
            line = input[i].split(" ");
            addEmployee(line[0], line[1]);
        }

        int q = Integer.valueOf(input[e]);
        for (int i = e + 1; i <= e + q; i++) {
            line = input[i].split(" ");
            switch (Integer.valueOf(line[0])) {
                case 0:
                        addEmployee(line[1], line[2]);
                        break;
                case 1:
                        removeEmployee(line[1], line[2]);
                        break;
                case 2:
                        System.out.println();
                        commonBoss(line[1], line[2]);
                        break;
                case 3:
                        System.out.println();
                        printEmployees();
                        break;
            }
        }
    }
}
