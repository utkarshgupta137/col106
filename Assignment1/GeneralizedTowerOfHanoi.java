import java.util.EmptyStackException;

public class GeneralizedTowerOfHanoi {
    private static TowerOfHanoi toh = new TowerOfHanoi();

    public static void gtoh_with_recursion(int num_disks, int start_pos, int r, int b) {
        if (num_disks <= 0 || start_pos == r && r == b) {
            return;
        } else if (r == b) {
            toh.toh_with_recursion(num_disks, start_pos, r);
            return;
        }

        int r_ = r;
        int b_ = b;
        if (num_disks % 2 == 1) {
            r_ = b;
            b_ = r;
        }

        int via_pos = 6 - r - b;
        if (start_pos == r_) {
            toh.toh_with_recursion(num_disks - 2, start_pos, via_pos);
            System.out.println(start_pos + " " + b_);
            gtoh_with_recursion(num_disks - 2, via_pos, r, b);
        } else if (start_pos == b_) {
            toh.toh_with_recursion(num_disks - 1, start_pos, via_pos);
            System.out.println(start_pos + " " + r_);
            gtoh_with_recursion(num_disks - 1, via_pos, r, b);
        } else {
            toh.toh_with_recursion(num_disks - 1, start_pos, b_);
            System.out.println(start_pos + " " + r_);
            gtoh_with_recursion(num_disks - 2, b_, r, b);
        }
    }

    public static void gtoh_without_recursion(int num_disks, int start_pos, int r, int b) {
        if (start_pos == r && r == b) {
            return;
        } else if (r == b) {
            toh.toh_without_recursion(num_disks, start_pos, r);
            return;
        }

        int via_pos;
        if (num_disks % 2 == 1) {
            via_pos = r;
            r = b;
            b = via_pos;
        }

        for (int n = num_disks; n > 0;) {
            if (n == 1 && start_pos != r) {
                System.out.println(start_pos + " " + r);
                return;
            }

            via_pos = 6 - r - b;
            if (start_pos == r) {
                toh.toh_without_recursion(n - 2, start_pos, via_pos);
                System.out.println(start_pos + " " + b);
                n -= 2;
                start_pos = via_pos;
            } else if (start_pos == b) {
                toh.toh_without_recursion(n - 1, start_pos, via_pos);
                System.out.println(start_pos + " " + r);
                n -= 1 ;
                start_pos = via_pos;

                via_pos = r;
                r = b;
                b = via_pos;
            } else {
                toh.toh_without_recursion(n - 1, start_pos, b);
                System.out.println(start_pos + " " + r);
                n -= 2;
                start_pos = b;
            }
        }
    }
}
