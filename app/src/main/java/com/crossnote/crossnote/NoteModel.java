package com.crossnote.crossnote;

import java.util.HashMap;
import java.util.Map;

public class NoteModel {
    public String title;
    public String textOfNote;
    public String key;

    public NoteModel(){

    }


    public NoteModel(String title, String textOfNote, String key){
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