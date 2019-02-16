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

    private static Employee root;
    private static TreeMap<String, Employee> tree;

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
        Employee bossE = tree.get(boss);
        Employee employeeE = new Employee(bossE.rank + 1, employee, bossE);

        bossE.employees.add(employeeE);
        tree.put(employee, employeeE);
    }

    private static void removeEmployee(String oldBoss, String newBoss) {
        Employee oldBossE = tree.get(oldBoss);
        Employee newBossE = tree.get(newBoss);

        for (Employee employeeE : oldBossE.employees) {
            employeeE.boss = newBossE;
            newBossE.employees.add(employeeE);
        }

        oldBossE.boss.employees.remove(oldBossE);
        tree.remove(oldBoss);
    }

    private static void commonBoss(String employee, String boss) {
        Employee employeeE = tree.get(employee);
        Employee commonBossE = tree.get(boss);
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
        tree = new TreeMap<String, Employee>(root.name, root);

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
