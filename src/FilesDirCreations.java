import java.io.File;
import java.io.IOException;

public class FilesDirCreations {

    static void createDir(String path) {

        System.out.println("create dir " + path);
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

    }

    static void createFile(String path, boolean fileExist) {
        if (!fileExist) {
            System.out.println("create file " + path);
            File file = new File(path);
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error with file " + path);
            }
        }
    }
}
