package com.shortn0tes.springbootapp;

interface ComparatorSort{
    int compare(String first, String second);
}

public class ExtendMethodString {

    public static int compareInt(String first, String second){
        double firstNum = first.isEmpty()? 0 : Double.parseDouble(first);
        double secondNum = second.isEmpty() ? 0 : Double.parseDouble(second);
        double result = firstNum - secondNum;
        return result == 0 ? 0 : result < 0 ? -1 : 1;
    }

    public static int compareStr(String first, String second){
        return first.compareTo(second);
    }

   public static boolean searchSubstring(String str, String searchStr){
       for (int i = 0; i < searchStr.length(); i++)
           if(str.charAt(i) != searchStr.charAt(i))
               return false;
       return true;
   }

    public static void quickSortStr(SearchPair[] source, int leftBorder, int rightBorder,
                                    ComparatorSort comparatorSort ) {
        int leftMarker = leftBorder;
        int rightMarker = rightBorder;
        String pivot = source[(leftMarker + rightMarker) / 2].sortedKey;
        do {
            while(comparatorSort.compare(source[leftMarker].sortedKey, pivot) < 0)
                leftMarker++;
            while (comparatorSort.compare(source[rightMarker].sortedKey, pivot)  > 0)
                rightMarker--;
            if (leftMarker <= rightMarker) {
                if (leftMarker < rightMarker) {
                    SearchPair tmp = source[leftMarker];
                    source[leftMarker] = source[rightMarker];
                    source[rightMarker] =  tmp;
                }
                leftMarker++;
                rightMarker--;
            }
        } while (leftMarker <= rightMarker);
        if (leftMarker < rightBorder)
            quickSortStr(source, leftMarker, rightBorder, comparatorSort);
        if (leftBorder < rightMarker)
            quickSortStr(source, leftBorder, rightMarker, comparatorSort);
    }
}
