package fr.istic.project.model.memento;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class Memento  {

    private HashMap<String, Object> data;
    private Gson gson;

    public Memento(String json) {
        this(new HashMap<>());
        if (json == null) {
            throw new IllegalArgumentException();
        }
        fromJson(json);
    }

    public Memento() {
        this(new HashMap<>());
    }

    public Memento(Map<String, Object> data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        this.data = new HashMap<>(data);
        gson = new Gson();
    }

    public Map<String, Object> getData() {
        return (data);
    }

    public String toJson() {
        return gson.toJson(data);
    }

    public void setData(Map<String, Object> data) {
        if (data == null) {
            throw new IllegalArgumentException();
        }
        
        if (data.containsKey("gson")) {
            data = (Map<String, Object>) data.get("data");
        }
        this.data.clear();
        this.data.putAll(data);
    }

    public void fromJson(String json) {
        if (json == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> jsonData = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
        setData(jsonData);
    }
}
