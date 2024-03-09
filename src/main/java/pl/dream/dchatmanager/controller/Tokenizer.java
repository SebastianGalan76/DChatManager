package pl.dream.dchatmanager.controller;

public class Tokenizer {
    public static String getTokenizedText(String text){
        text = text.toLowerCase();
        text = removeSigns(text);
        text = removeRepetitiveLetters(text);

        return text;
    }
    public static String removeFlood(String text){
        return text.replaceAll("(.)\\1{2,}", "$1");
    }


    private static String removeSigns(String text){
        return text.replaceAll("[\\-_\\.\\d]", "");
    }
    private static String removeRepetitiveLetters(String text){
        return text.replaceAll("(.)\\1+", "$1");
    }
}
