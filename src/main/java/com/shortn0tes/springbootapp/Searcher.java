package com.shortn0tes.springbootapp;

import javafx.util.Pair;

import java.net.URL;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

class TimerSearch {
    private long start;
    private long end;
    private long time;

    public TimerSearch(){
        start = 0;
        end = 0;
        time = 0;
    }

    public void startTimer(){
        start = System.nanoTime();
        end = 0;
    }

    public void stopTimer(){
        end = System.nanoTime();
        time += end - start;
    }

    public String getTime(){
        return TimeUnit.NANOSECONDS.toMillis(time) + " ms";
    }

}


public class Searcher {
    private SeacrhEngine engine;
    private TreeMap<String, String> sortedStrTree;
    private String searchSubstring;
    private String timeSearch;

    public String getTimeSearch() {
        return timeSearch;
    }

    public int getLengthCountStrResult(){
        return sortedStrTree.size();
    }

    public Searcher(String searchSubstring, String numberColumn, URL path){
        if (numberColumn != "")
            engine = new SeacrhEngine(Integer.parseInt(numberColumn) - 1);
        else
            engine = new SeacrhEngine();
        this.searchSubstring = searchSubstring;
        this.timeSearch = "0 ms";
        initializateEngine(path);
        sortedStrTree = new TreeMap<>();
    }

    private void initializateEngine(URL path){
        if(engine == null)
            throw new NullPointerException("Initialize the engine");
        engine.initializationInputStream(path);
        engine.initializationScanner();
    }

    public void search(){
        MarkReadFile mark;
        TimerSearch timer = new TimerSearch();
        do {
            mark = engine.readFileToBuffer();
            timer.startTimer();
            Pair<Integer, Integer> indexes = engine.searchInBuffer(this.searchSubstring);
            timer.stopTimer();
            engine.addTreeResult(indexes, this.sortedStrTree);
        }while (mark != MarkReadFile.END_OF_READING);
        timeSearch = timer.getTime();
    }

    public void printResult(){
        for(String str: this.sortedStrTree.values())
            System.out.println(str);
    }
}
