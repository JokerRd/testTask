package com.shortn0tes.springbootapp;

import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

enum MarkReadFile{
    READING,
    END_OF_READING
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
    private Scanner scanner;

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
    }

    public SeacrhEngine(){
        this.numberColumns = Settings.DEFAULT_NUMBER_COLUMN - 1;
        this.sizeBufferStr = Settings.SIZE_BUFFER_STRING;
        initializationBuffer();
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

    public void initializationScanner(){
        if (input == null)
            throw new NullPointerException("Initialize the input stream");
        scanner = new Scanner(input);
    }

    public MarkReadFile readFileToBuffer(){
        if (scanner == null)
            throw new NullPointerException("Initialize the scanner");
        int countStr = 0;
        String addedLine = "";
        try {
            for (int i = 0; scanner.hasNextLine() && i < sizeBufferStr; i++){
                addedLine = scanner.nextLine();
                buffer[i].sortedKey = ExtendMethodString.PreparingStrForAdd(addedLine,
                        Settings.SEPARATOR, this.numberColumns);
                buffer[i].value =addedLine;
                countStr++;
            }
        }
        catch (NoSuchElementException exception){

        }
        if (countStr != sizeBufferStr)
            buildLimitedBuffer(countStr);
        return countStr == sizeBufferStr ? MarkReadFile.READING: MarkReadFile.END_OF_READING;
    }

    private void buildLimitedBuffer(int size){
        SearchPair[] limBuffer = new SearchPair[size];
        for(int i = 0; i < size; i++)
            limBuffer[i] = buffer[i];
        buffer = limBuffer;
    }

    public  int binarySearchIndexStart(String substring) {
        int searchedIndex = -1;
        int firstIndex = 0;
        int lastIndex = buffer.length - 1;
        String compareStr = "";
        while(firstIndex <= lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            compareStr = ";";
            if (buffer[middleIndex].sortedKey.compareTo(substring) < 0)
                firstIndex= middleIndex + 1;
            else if (buffer[middleIndex].sortedKey.compareTo(substring) > 0) {
                if (ExtendMethodString.searchSubstring(buffer[middleIndex].sortedKey, substring))
                    searchedIndex = middleIndex;
                lastIndex = middleIndex - 1;
            }
        }
        return searchedIndex;
    }

    public  int binarySearchIndexEnd(String substring) {
        int firstIndex = 0;
        int lastIndex = buffer.length - 1;
        while(firstIndex + 1 < lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            if (buffer[middleIndex].sortedKey.compareTo(substring) < 0)
                firstIndex= middleIndex + 1;
            else if (buffer[middleIndex].sortedKey.compareTo(substring) > 0) {
                if (ExtendMethodString.searchSubstring(buffer[middleIndex].sortedKey, substring))
                    firstIndex = middleIndex;
                else
                    lastIndex = middleIndex - 1;
            }
        }
        return firstIndex;
    }

    public Pair<Integer, Integer> searchInBuffer(String substring){
        ExtendMethodString.quickSortStr(buffer,0, buffer.length - 1);
        int startIndex = binarySearchIndexStart(substring);
        if (startIndex == -1)
            return new Pair<>(-1,-1);
        int endIndex = binarySearchIndexEnd(substring);
        return new Pair<>(startIndex,endIndex);
    }

    public void addTreeResult(Pair<Integer, Integer> indexes, TreeMap<String, String> tree){
        if (indexes.getKey() == -1)
            return;
        for(int i = indexes.getKey(); i <= indexes.getValue(); i++)
            tree.put(buffer[i].sortedKey, buffer[i].value);
    }


}
