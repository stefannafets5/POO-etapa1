package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public abstract class Transaction {
    private int timestamp;
    private String description;

    public Transaction(int timestamp, String description){
        this.timestamp = timestamp;
        this.description = description;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract ObjectNode toJson(ObjectMapper mapper);
}
