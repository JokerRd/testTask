package com.shortn0tes.springbootapp;

import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.util.TreeMap;
import com.opencsv.CSVReader;

enum MarkReadFile{
    READING,
    END_OF_READING
}

enum ParameterSort{
    INT,
    STRING
}

class SearchPair {
    public String sortedKey;
    public String value;
    public SearchPair(String first, String second){
        this.sortedKey = first;
        this.value = second;
    }
    public void setValues(String first, String second){
        this.sortedKey = first;
        this.value = second;
    }
}

public class SeacrhEngine {

    private int numberColumns;
    private int sizeBufferStr;;
    private SearchPair[] buffer;
    private InputStream input;
    private CSVReader reader;
    private String[] readingLine;
    private ParameterSort parameterSort;

    public ParameterSort getParameterSort() {
        return parameterSort;
    }

    public int getNumberColumns() {
        return numberColumns;
    }

    public int getBufferStr() {
        return sizeBufferStr;
    }

    public void resizeBuffer(int bufferStr) {
        this.sizeBufferStr = bufferStr;
    }

    public void rebuildBuffer(){
        initializationBuffer();
    }

    public SeacrhEngine(int numberColumns){
        this.numberColumns = numberColumns;
        this.sizeBufferStr = Settings.SIZE_BUFFER_STRING;
        initializationBuffer();
        readingLine = null;
    }

    public SeacrhEngine(){
        this.numberColumns = Settings.DEFAULT_NUMBER_COLUMN - 1;
        this.sizeBufferStr = Settings.SIZE_BUFFER_STRING;
        initializationBuffer();
        readingLine = null;
    }

    private void initializationBuffer(){
        buffer = new SearchPair[this.sizeBufferStr];
        for(int i = 0; i < this.sizeBufferStr; i++)
            buffer[i] = new SearchPair("", "");
    }

    public void initializationInputStream(URL path){
        try{
            input = path.openStream();
        }
        catch (FileNotFoundException exception){
            System.out.println("File not found");
        }
        catch (IOException exception){
            System.out.println("Error read file");
        }
    }


    public void initializationCSVReader(){
        if (input == null)
            throw new NullPointerException("Initialize the input stream");
        reader = new CSVReader(new InputStreamReader(input));
    }

    public void setParameterSort(){
        if (Character.isDigit(readingLine[this.numberColumns].charAt(0))){
            parameterSort =  ParameterSort.INT;
            return;
        }
        parameterSort =  ParameterSort.STRING;
    }

    public void readFirstStr(){
        try {
            readingLine = reader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MarkReadFile readFileToBufferWithCSVReader(){
        if (reader == null)
            throw new NullPointerException("Initialize the scanner");
        int countStr = 0;
        try{
            for(int i = 0; readingLine != null && i < sizeBufferStr; i++){
                buffer[i].sortedKey = readingLine[this.numberColumns];
                buffer[i].value = String.join(" ", readingLine);
                readingLine  = reader.readNext();
                countStr++;
            }
        }
        catch (IOException e ){
            System.out.println("Error input");
        }
        if (countStr != sizeBufferStr){
            buildLimitedBuffer(countStr);
            return MarkReadFile.END_OF_READING;
        }
        return MarkReadFile.READING;
    }

    private void buildLimitedBuffer(int size){
        SearchPair[] limBuffer = new SearchPair[size];
        for(int i = 0; i < size; i++)
            limBuffer[i] = buffer[i];
        buffer = limBuffer;
        sizeBufferStr = size;
    }

    public  int binarySearchIndexStart(String substring, ComparatorSort comparatorSort) {
        int searchedIndex = -1;
        int firstIndex = 0;
        int lastIndex = buffer.length - 1;
        String compareStr = "";
        while(firstIndex <= lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            compareStr = ";";
            if (comparatorSort.compare(buffer[middleIndex].sortedKey,substring) < 0)
                firstIndex= middleIndex + 1;
            else if (comparatorSort.compare(buffer[middleIndex].sortedKey,substring) >= 0) {
                if (ExtendMethodString.searchSubstring(buffer[middleIndex].sortedKey, substring))
                    searchedIndex = middleIndex;
                if (parameterSort == ParameterSort.INT)
                    searchedIndex = middleIndex;
                lastIndex = middleIndex - 1;
            }
        }
        return searchedIndex;
    }

    public  int binarySearchIndexEnd(String substring, ComparatorSort comparatorSort) {
        int firstIndex = 0;
        int lastIndex = buffer.length - 1;
        while(firstIndex + 1 < lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            if (comparatorSort.compare(buffer[middleIndex].sortedKey,substring) < 0)
                firstIndex= middleIndex + 1;
            else if (comparatorSort.compare(buffer[middleIndex].sortedKey,substring) > 0) {
                if (ExtendMethodString.searchSubstring(buffer[middleIndex].sortedKey, substring))
                    firstIndex = middleIndex;
                else
                    lastIndex = middleIndex - 1;
            }
        }
        return firstIndex;
    }

    public Pair<Integer, Integer> searchInBuffer(String substring, TimerSearch timerSearch){
        ComparatorSort comparatorSort  = parameterSort == ParameterSort.INT ?
                ExtendMethodString::compareInt : ExtendMethodString:: compareStr;
        ExtendMethodString.quickSortStr(buffer,0, buffer.length - 1, comparatorSort);
        timerSearch.startTimer();
        int startIndex = binarySearchIndexStart(substring, comparatorSort);
        if (startIndex == -1)
            return new Pair<>(-1,-1);
        int endIndex = parameterSort == ParameterSort.INT ? sizeBufferStr - 1 :
                binarySearchIndexEnd(substring, comparatorSort);
        timerSearch.stopTimer();
        return new Pair<>(startIndex,endIndex);
    }

    public void addTreeResult(Pair<Integer, Integer> indexes, TreeMap<Object, String> tree){
        if (indexes.getKey() == -1)
            return;
        for(int i = indexes.getKey(); i <= indexes.getValue(); i++)
            tree.put(parameterSort == ParameterSort.INT ? Double.parseDouble(buffer[i].sortedKey)
                    : buffer[i].sortedKey, buffer[i].value);
    }


}
