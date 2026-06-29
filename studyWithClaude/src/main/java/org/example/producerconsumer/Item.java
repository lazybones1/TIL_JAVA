package org.example.producerconsumer;

public class Item {
    private final int id;
    private final String data;

    public Item(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Item{id=" + id + ", data='" + data + "'}";
    }
}