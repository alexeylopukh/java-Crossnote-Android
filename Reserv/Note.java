package com.example.lopuk.registr;

import java.util.HashMap;
import java.util.Map;

public class Note {
    public String title;
    public String textOfNote;
    public String key;

    public Note(){

    }


    public Note(String title, String textOfNote, String key){
        this.title = title;
        this.textOfNote = textOfNote;
        this.key = key;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("textOfNote", textOfNote);
        result.put("key", key);
        return result;
    }

}
