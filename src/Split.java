import java.io.*;
import java.util.Objects;

public class Split {
    public static void splitToFile() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(Main.originFileName));
        String token, pPath = "", aPath = "", mPath = "", chapter = "";
        StringBuilder content = new StringBuilder();
        String[] sToken;
        boolean startWithChapter = true, page1 = true;
        FilesDirCreations.createDir(Main.dirName);
        int pCnt = 100;

        while ((token = FileActions.readLines(reader, 1)) != null) {

            sToken = token.split(" ");

            if (sToken[0].equals("מסכת")) {
                mPath = Main.dirName + "\\" + token;
                FilesDirCreations.createDir(mPath);
                if (!page1) {
                    FileActions.writeToFile(content.toString(), aPath);
                    content = new StringBuilder();
                }
                page1 = true;
                pCnt = 100;
            } else if (Funcs.startPage(sToken) || token.startsWith("#####")) {
                if (sToken.length > 2 && sToken[3].equals("א") || page1) {
                    FilesDirCreations.createDir((pPath = mPath + "\\" + pCnt + " " + (sToken[1])));
                    pCnt++;
                }
                if (content.toString().endsWith(chapter)) {
                    content = new StringBuilder(content.substring(0, content.length() - (chapter.length())));
                    startWithChapter = true;
                }
                if (!page1) {
                    FileActions.writeToFile(content.toString(), aPath);
                    content = new StringBuilder();
                }

                if (sToken.length > 2) {
                    aPath = pPath + "\\" + sToken[3] + ".txt";
                    FilesDirCreations.createFile(pPath, false);
                }
                page1 = false;
            } else if (Funcs.startPerek(sToken)) {
                chapter = token + "\n";
                content.append(chapter);
            } else {
                if (startWithChapter) {
                    content.append(chapter);
                    chapter = "";
                    startWithChapter = false;
                }
                content.append(token).append("\n");
            }
        }
        reader.close();
    }

    public static Units.Bavli createDB(String path) {
        Units.Bavli bavli = new Units.Bavli(path);
        Units.Masechet masechet;
        Units.Page page;
        Units.Chapter chapter = new Units.Chapter();
        try {

            File mainFile = new File(bavli.getPath());
            File[] bavliF = mainFile.listFiles();
            String content;
            String[] sContent;
            boolean startPerk = true;

            for (int i = 0; i < Objects.requireNonNull(mainFile.listFiles()).length; i++) {

                assert bavliF != null;
                File[] masechetF = bavliF[i].listFiles();
                masechet = bavli.addMasechet(bavliF[i].getName());

                for (int j = 0; j < (masechetF != null ? masechetF.length : 0); j++) {
                    File[] pageF = masechetF[j].listFiles();
                    for (int k = 0; k < pageF.length; k++) {

                        content = FileActions.readPage(pageF[k].toPath().toString());
                        assert content != null;
                        sContent = content.split("\n");

                        if (Funcs.startPerek(sContent[0].split(" "))) {//פרק בתחילת דף
                            chapter = masechet.addChapter(sContent[0]);
                            chapter.addMishna(sContent[1], pageF[k].toPath().toString(), sContent[0].toCharArray().length);
                            startPerk = true;
                        }

                        page = masechet.addPage((masechetF[j].getName() + " - " + pageF[k].getName()).split("\\.")[0], pageF[k].getPath());

                        if (sContent.length > 1)
                            if (Funcs.startPerek(sContent[1].split(" "))) {//פרק באמצע דף
                                chapter.addMishna(sContent[0], page.getPath(), 0);
                                chapter = masechet.addChapter(sContent[1]);
                                chapter.addToEndOfList(page);
                                chapter.addMishna(sContent[2], page.getPath(), (sContent[0] + sContent[1]).toCharArray().length);
                                startPerk = true;
                            }
                        if (!startPerk)
                            chapter.addMishna(sContent[0], page.getPath(), 0);

                        startPerk = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("file didnt found start with 1");
            Interface.start();
        }
        return bavli;
    }
}