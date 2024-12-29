import java.util.ArrayList;
import java.util.Scanner;

public class Units {
    public static class Unit {
        private final String path;
        private final String name;
        private final ArrayList<Units.Unit> list;

        public Unit(String path, String name) {
            this.path = path;
            this.name = name;
            list = new ArrayList<>();
        }

        public Unit() {
            path = null;
            name = null;
            list = new ArrayList<>();
        }

        public void addToListLexicographically(Unit unit) {
            list.add(Funcs.calculateSpotLexicographically(list, unit), unit);
        }


        public void addToEndOfList(Unit unit) {
            list.addLast(unit);
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public Unit getUnit(String uName) {
            return Funcs.binaryUnitSearch(uName, list);
        }

        public ArrayList<Units.Unit> getList() {
            return list;
        }

        public void printList() {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getName());
            }
        }
    }

    public static class Bavli extends Unit {

        public Bavli(String path) {
            super(path, null);
            System.out.println("build bavli");
        }

        public Units.Masechet addMasechet(String DName) {
            Masechet masechet = new Masechet(DName, super.path);
            super.addToListLexicographically(masechet);
            return masechet;
        }
    }

    public static class Masechet extends Unit {
        private final ArrayList<Chapter> listOfChapters;
        private final Cache cache;

        public Masechet(String name, String path) {
            super(path + "\\" + name, name);
            listOfChapters = new ArrayList<>();
            System.out.println("build DB for masechet " + name);
            cache = new Cache();
        }

        public Masechet() {
            super();
            listOfChapters = null;
            cache = null;
        }

        public Chapter getChapterByName(String name) {
            for (Chapter chapter : listOfChapters)
                if (name.equals(chapter.getName().split("-")[1].trim()))
                    return chapter;
            return null;
        }

        public Chapter addChapter(String name) {
            Chapter chapter = new Chapter(name);
            listOfChapters.add(chapter);
            return chapter;
        }

        public Page addPage(String name, String path) {
            Page page = new Page(name, path);
            super.addToListLexicographically(page);
            listOfChapters.getLast().addToEndOfList(page);
            return page;
        }

        public void addToCache(Page page) {
            cache.addPage(page);
        }

        public Page searchAtCache(String key) {
            return cache.getPage(key);
        }

        public void printCache() {
            cache.printCache();
        }

        public int getNumOfPages() {
            return super.list.size();
        }

        public void printChapters() {
            for (Chapter listOfChapter : listOfChapters) {
                System.out.println(listOfChapter.getName());
            }
        }
    }

    public static class Chapter extends Unit {
        private final ArrayList<Mishna> listOfMishna;

        public Chapter(String name) {
            super(null, name);
            listOfMishna = new ArrayList<>();
        }

        public Chapter() {
            super(null, null);
            listOfMishna = null;
        }

        public ArrayList<Mishna> getListOfMishna() {
            return listOfMishna;
        }

        public void addMishna(String content, String path, int index) {
            ArrayList<Mishna> l = setMishnaIndexes(content, path, index);
            assert listOfMishna != null;
            listOfMishna.addAll(l);
        }
    }

    public static class Page extends Unit {
        public Page(String name, String path) {
            super(path, name);
            System.out.println("build page " + name);
        }
    }

    public static class Mishna {

        private int start;
        private int end;
        private final String path;

        public Mishna(String path) {
            this.start = -1;
            this.end = -1;
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }
    private static ArrayList<Mishna> setMishnaIndexes(String content, String path, int index) {
        ArrayList<Mishna> list = new ArrayList<>();
        int Index = index, size;
        Mishna mishna = new Mishna(path);
        String token;
        String[] sToken;
        Scanner scanner = new Scanner(content);
        scanner.useDelimiter("'");
        while (scanner.hasNext()) {
            token = scanner.next();
            sToken = token.split(" ");
            size = sToken.length;
            Index += token.length();
            if (sToken[size - 1].endsWith("מתני")) {
                mishna.setStart(Index - 4);
                System.out.println("mishna start at " + Index);
                list.add(mishna);
            } else if (sToken[size - 1].endsWith("גמ")) {
                mishna.setEnd(Index - 2);
                if (mishna.getStart() == -1)
                    list.add(mishna);
                mishna = new Mishna(path);
                System.out.println("mishna end at " + Index);
            }
        }
        return list;
    }
}
