import java.util.ArrayList;

public class Funcs {
    public static void printLinePieces(String line, int piece) {
        String[] splitLine = line.split(" ");
        for (int i = 0; i < splitLine.length; i++) {
            System.out.print(splitLine[i] + " ");
            if (i % piece == 0 && i != 0)
                System.out.println();
        }
    }

    static boolean startPage(String[] sToken) {
        if (sToken.length > 4)
            return false;
        return sToken[0].contains("דף") && sToken[2].contains("-");
    }

    static boolean startPerek(String[] sToken) {
        if (sToken.length < 4)
            return false;
        return sToken[0].contains("פרק") && (sToken[2].contains("-") || sToken[3].equals("-"));
    }

    public static int calculateSpotLexicographically(ArrayList<Units.Unit> list, Units.Unit unit) {
        int i = binarySearch(unit.getName(), list);
        if (i == -1)
            i = 0;
        return i;
    }

    static Units.Unit binaryUnitSearch(String uName, ArrayList<Units.Unit> list) {
        int i;
        if ((i = binarySearch(uName, list)) > -1)
            return list.get(i);
        return null;
    }

    private static int binarySearch(String name, ArrayList<Units.Unit> list) {
        String temp;
        if (!list.isEmpty()) {
            int low = 0, high = list.size();
            while (low <= high) {
                int mid = (low + high) / 2;
                if (mid >= list.size())
                    return mid;
                temp=list.get(mid).getName();
                if (!name.contains("1") && name.contains("-") && !name.contains("פרק"))
                    temp=temp.substring(4);
                int cmp = temp.compareTo(name);
                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    return mid;
                }
            }
        }
        return -1;
    }
}
