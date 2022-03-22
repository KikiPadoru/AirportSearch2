package app;

import java.io.*;
import java.util.ArrayList;

class OneLine {
    String comparable;
    String value;

    public OneLine(String comparable, String value) {
        this.comparable = comparable;
        this.value = value;
    }
}

class SortArrayList {
    private final ArrayList<OneLine> OurLines;

    public SortArrayList() {
        OurLines = new ArrayList<OneLine>();
    }

    public void AddSortArrayList(String comparable, String value) {
        boolean flag = true;
        for (int i = 0; i < OurLines.size() && flag; i++) {
            String a = OurLines.get(i).comparable;
            if (a.compareTo(comparable) >= 0) {
                OurLines.add(i, new OneLine(comparable, value));
                flag = false;
            }
        }
        if (OurLines.size() == 0) {
            OurLines.add(new OneLine(comparable, value));
            return;
        }
        if (flag) {
            OurLines.add(new OneLine(comparable, value));
        }
    }

    public int getsize() {
        return OurLines.size();
    }

    public String getcomparable(int index) {
        return OurLines.get(index).comparable;
    }

    public String getvalue(int index) {
        return OurLines.get(index).value;
    }
}

class AirportSearch {
    private int searchСolumn;
    private String str = "";
    private SortArrayList OurLines;
    private long timeDelay = 0;
    private String fileName;

    private void EntryString(StreamTokenizer reader) {
        long start = System.nanoTime();
        if (reader.ttype == 34 || reader.ttype == -3) {
            str += '\"' + reader.sval + '\"';
        } else if (reader.ttype == StreamTokenizer.TT_NUMBER) {
            str += Double.toString(reader.nval);
        } else if (reader.ttype == ',') {
            str += ',';
        }
        timeDelay += System.nanoTime() - start;
    }

    private void PassTheLine(StreamTokenizer reader) throws IOException {
        while (reader.ttype != StreamTokenizer.TT_EOL && reader.ttype != StreamTokenizer.TT_EOF) {
            reader.nextToken();
            EntryString(reader);
        }
    }

    private void PassTheСolumn(StreamTokenizer reader) throws IOException {
        for (int i = 0; i < searchСolumn; ) {
            if (reader.nextToken() == ',') {
                i++;
            }
            EntryString(reader);
        }
    }

    public AirportSearch(String fileName, int searchСolumn) {
        this.fileName = fileName;
        this.searchСolumn = searchСolumn;
        this.OurLines = new SortArrayList();
    }

    public AirportSearch(String fileName) {
        this.fileName = fileName;
        this.searchСolumn = 0;
        this.OurLines = new SortArrayList();
    }

    public void find() throws IOException {
        BufferedReader readerTerminal = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введите строку: ");
        String wordSearch = readerTerminal.readLine();
        int numberOfLines = 0;

        long start = System.nanoTime();
        StreamTokenizer reader = new StreamTokenizer(new BufferedInputStream(new FileInputStream(fileName)));
        reader.eolIsSignificant(true);
        reader.wordChars('\\', '\\');

        while (true) {
            if (reader.ttype == StreamTokenizer.TT_EOF) break;
            boolean flag = false;
            String comparable = "";
            PassTheСolumn(reader);
            reader.nextToken();
            EntryString(reader);
            if ((reader.ttype == 34 || reader.ttype == -3) && reader.sval.startsWith(wordSearch)) {
                numberOfLines++;
                comparable = reader.sval;
                flag = true;
            } else if (reader.ttype == StreamTokenizer.TT_NUMBER && Double.toString(reader.nval).startsWith(wordSearch)) {
                numberOfLines++;
                comparable = Double.toString(reader.nval);
                flag = true;
            }
            PassTheLine(reader);
            if (flag) {
                long startTime = System.nanoTime();
                OurLines.AddSortArrayList(comparable, str);
                timeDelay += System.nanoTime() - startTime;
            }
            str = "";
        }
        long finish = System.nanoTime();
        for (int i = 0; i < OurLines.getsize(); i++) {
            System.out.println(OurLines.getvalue(i));
        }
        System.out.println("Колличество найденных строк: " + numberOfLines + "\nВремя поиска, мс: " + ((finish - start - timeDelay) / 1000000) + "\nВсё время(с учётом сортировки), мс: " + ((finish - start) / 1000000));

    }
}

public class Main {
    public static void main(String[] args) throws IOException {//1813
        AirportSearch airportSearch;
        if (args.length == 1) {
            airportSearch = new AirportSearch(args[0]);//"static/airports.dat"
        }else if(args.length == 2){
            airportSearch = new AirportSearch(args[0], Integer.valueOf(args[1]));
        } else {
            airportSearch = new AirportSearch("static/airports.dat",1);
        }
        airportSearch.find();
    }
}
