package com.shortn0tes.springbootapp;

public class ExtendMethodString {

    public static String PreparingStrForAdd(String inputStr, String separator, int numberColumn){
        String result;
        try {
            result = skipNoLetterAndNoNumberSym(inputStr.split(separator)[numberColumn]);
        }
        catch (ArrayIndexOutOfBoundsException exception){
            throw new ArrayIndexOutOfBoundsException("Invalid column number");
        }
        return result;
    }

    private static String skipNoLetterAndNoNumberSym(String inputStr){
        if (inputStr.length() == 0)
            return inputStr;
        int count = 0;
        for (int i = 0; i < inputStr.length() && !Character.isLetterOrDigit(inputStr.charAt(i))
                && inputStr.charAt(i) != '-'; i++)
            count++;
        return inputStr.substring(count);
    }
   public static boolean searchSubstring(String str, String searchStr){
       for (int i = 0; i < searchStr.length(); i++)
           if(str.charAt(i) != searchStr.charAt(i))
               return false;
       return true;
   }

    public static void quickSortStr(SearchPair[] source, int leftBorder, int rightBorder) {
        int leftMarker = leftBorder;
        int rightMarker = rightBorder;
        String pivot = source[(leftMarker + rightMarker) / 2].sortedKey;
        do {
            while (source[leftMarker].sortedKey.compareTo(pivot) < 0)
                leftMarker++;
            while (source[rightMarker].sortedKey.compareTo(pivot) > 0)
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
            quickSortStr(source, leftMarker, rightBorder);
        if (leftBorder < rightMarker)
            quickSortStr(source, leftBorder, rightMarker);
    }
}
