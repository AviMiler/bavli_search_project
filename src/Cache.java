import java.util.*;

public class Cache {
    private static final int sizeOfCache = 5;
    private final LinkedList<Units.Page> pageLinkedList;
    private final HashMap<String, Units.Page> map;

    public Cache() {
        map = new HashMap<>();
        pageLinkedList = new LinkedList<>();
    }

    public void addPage(Units.Page page) {

        if (page == null)
            return;
        String key = remove(page.getPath());
        if (pageLinkedList.isEmpty()) {
            map.put(key, page);
            pageLinkedList.addFirst(page);
        } else if (map.containsKey(key)) {
            pageLinkedList.remove(page);
            pageLinkedList.addFirst(page);
        } else if (pageLinkedList.size() >= sizeOfCache) {
            map.remove(remove(pageLinkedList.removeLast().getPath()));
            pageLinkedList.addFirst(page);
        } else {
            map.put(key, page);
            pageLinkedList.addFirst(page);
        }
        System.out.println("add to cache " + remove(page.getPath()));
    }

    public Units.Page getPage(String key) {
        return map.get(key.replaceAll(" ", ""));
    }

    public void printCache() {
        System.out.println("the cache is: ");
        for (Units.Page page : pageLinkedList) {
            System.out.println(page.getPath());
        }
    }


    private static String remove(String s) {

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c - '0' >= 0 && c - '0' <= 9) {
                s = s.substring(0, i) + s.substring(i + 1);
                i--;
            }
        }
        s = s.split(".txt")[0];
        return s.replaceAll(" ", "");

    }
}