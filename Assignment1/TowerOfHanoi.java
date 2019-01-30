import java.util.EmptyStackException;

public class TowerOfHanoi {
    public static void toh_with_recursion(int num_disks, int start_pos, int end_pos) {
        if (num_disks == 0 || start_pos == end_pos) {
            return;
        }

        int via_pos = 6 - start_pos - end_pos;
        toh_with_recursion(num_disks - 1, start_pos, via_pos);
        System.out.println(start_pos + " " + end_pos);
        toh_with_recursion(num_disks - 1, via_pos, end_pos);
    }

    public static void toh_without_recursion(int num_disks, int start_pos, int end_pos) {
        if (num_disks == 0 || start_pos == end_pos) {
            return;
        }

        MyStack<int[]> Stack = new MyStack<int[]>();
        Stack.push(new int[] {num_disks, start_pos, end_pos});

        int via_pos = 6 - start_pos - end_pos;
        int[] stack;
        while (!Stack.empty()) {
            stack = Stack.pop();

            num_disks = stack[0];
            start_pos = stack[1];
            end_pos = stack[2];
            via_pos = 6 - start_pos - end_pos;

            if (num_disks == 1) {
                System.out.println(start_pos + " " + end_pos);
            } else {
                Stack.push(new int[] {num_disks - 1, via_pos, end_pos});
                Stack.push(new int[] {1, start_pos, end_pos});
                Stack.push(new int[] {num_disks - 1, start_pos, via_pos});
            }
        }
    }
}
