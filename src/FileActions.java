import java.io.*;

public class FileActions {

    public static String readPage(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder content = new StringBuilder();
            String token;
            while ((token = reader.readLine()) != null)
                content.append(token).append("\n");
            reader.close();
            return content.toString();
        } catch (IOException e) {
            System.out.println("page doesn't exist");
        }
        return null;
    }

    public static String readMishna(Units.Mishna mishna) {

        StringBuilder s = new StringBuilder();
        char[] c = readPage(mishna.getPath()).toCharArray();
        int i = 0, end = c.length;
        if (mishna.getStart() != -1)
            i = mishna.getStart();
        if (mishna.getEnd() != -1)
            end = mishna.getEnd();
        for (; i < end; i++) {
            s.append(c[i]);
        }
        s.append("\n");
        return s.toString();
    }

    public static String readLines(BufferedReader reader, int n) {
        String token = "";
        try {
            for (int i = 0; i < n; ) {
                if ((token = reader.readLine()) != null) {
                    if (!((token).isEmpty())) {
                        i++;
                    }
                } else
                    return null;
            }
        } catch (IOException e) {
            System.out.println("file didnt found");
            return null;
        }
        return token;
    }

    static long searchLine(long start, String line, String range) throws IOException {

        long byteCnt = start;
        BufferedReader reader = new BufferedReader(new FileReader(Main.originFileName));
        reader.skip(start);
        String token;

        while ((token = reader.readLine()) != null) {
            if (range != null) {
                if (token.startsWith(range)) {
                    return -1;
                }
            }
            if (token.startsWith(line)) {
                return byteCnt;
            }
            byteCnt += token.length() + 2;
        }
        return -2;
    }

    static void writeToFile(String content, String path) {

        System.out.println("write to: " + path + "\n content: " + content);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            System.out.println("write error");
        }

    }

    public static String readLines(long index, int range) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(Main.originFileName));
        reader.skip(index);
        StringBuilder word = new StringBuilder();
        String token;

        for (int i = 0; i < range; ) {
            token = reader.readLine();
            word.append(token);
            if (!token.isEmpty()) {
                i++;
                word.append("\n");
            }
        }
        return word.toString();
    }
}
