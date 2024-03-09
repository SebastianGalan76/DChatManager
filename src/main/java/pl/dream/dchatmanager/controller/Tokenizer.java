package pl.dream.dchatmanager.controller;

public class Tokenizer {
    public String getTokenizedText(String text){
        text = text.toLowerCase();
        text = removeSigns(text);
        text = removeRepetitiveLetters(text);

        return text;
    }
    public String removeFlood(String text){
        return text.replaceAll("(.)\\1{2,}", "$1");
    }


    private String removeSigns(String text){
        return text.replaceAll("[\\-_\\.\\d]", "");
    }
    private String removeRepetitiveLetters(String text){
        return text.replaceAll("(.)\\1+", "$1");
    }
}
