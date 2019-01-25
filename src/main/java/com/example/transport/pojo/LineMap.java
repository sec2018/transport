package com.example.transport.pojo;

import java.util.Map;

public class LineMap {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<Integer, String> getValuemap() {
        return valuemap;
    }

    public void setValuemap(Map<Integer, String> valuemap) {
        this.valuemap = valuemap;
    }

    private Map<Integer,String> valuemap;

    private Integer id;
}
