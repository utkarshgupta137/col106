import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Compress {
    public static byte[] compress(byte[] uncompressed) {
        int dictSize = 128;
        HashMap dictionary = new HashMap((int)Math.pow(2, 19) - 1);
        for (int i = 0; i < dictSize; i++) {
            dictionary.put("" + (char)i, i);
        }

        char c;
        String s = "";
        ArrayList<Byte> compressed = new ArrayList<Byte>();
        for (byte b : uncompressed) {
            c = (char)b;
            if (dictionary.get(s + c) != -1) {
                s += c;
            } else {
                if (dictSize < Math.pow(2, 16)) {
                    dictionary.put(s + c, dictSize++);
                }

                compressed.add((byte) (dictionary.get(s) >> 8));
                compressed.add((byte) (dictionary.get(s) & 0xff));
                s = "" + c;
            }
        }
        if (!s.equals("")) {
            compressed.add((byte) (dictionary.get(s) >> 8));
            compressed.add((byte) (dictionary.get(s) & 0xff));
        }

        byte[] comp = new byte[compressed.size()];
        for (int i = 0; i < compressed.size(); i++) {
            comp[i] = compressed.get(i);
        }
        return comp;
    }

    public static void main(String[] args) {
        try {
            Files.write(Paths.get(args[1]), compress(Files.readAllBytes(Paths.get(args[0]))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
