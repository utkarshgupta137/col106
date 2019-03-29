import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Decompress {
    public static byte[] decompress(byte[] compressed) {
        int dictSize = 128;
        ArrayList<String> dictionary = new ArrayList<String>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.add("" + (char)i);
        }

        int k = compressed[0] << 8 | compressed[1] & 0xff;
        String s, ps = "" + (char)k;
        ArrayList<Byte> uncompressed = new ArrayList<Byte>();
        uncompressed.add((byte)k);
        for (int i = 2; i < compressed.length;) {
            k = compressed[i++] << 8 | compressed[i++] & 0xff;
            if (k < 0) {
                k += Math.pow(2, 16);
            }

            if (k == dictionary.size()) {
                s = ps + ps.charAt(0);
            } else {
                s = dictionary.get(k);
            }
            dictionary.add(ps + s.charAt(0));
            for (byte b : s.getBytes()) {
                uncompressed.add(b);
            }
            ps = s;
        }

        byte[] uncomp = new byte[uncompressed.size()];
        for (int i = 0; i < uncompressed.size(); i++) {
            uncomp[i] = uncompressed.get(i);
        }
        return uncomp;
    }

    public static void main(String[] args) {
        try {
            Files.write(Paths.get(args[1]), decompress(Files.readAllBytes(Paths.get(args[0]))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
