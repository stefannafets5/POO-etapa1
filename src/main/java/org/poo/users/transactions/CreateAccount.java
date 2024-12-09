package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreateAccount extends Transaction {

    public CreateAccount(int timestamp) {
        super(timestamp, "New account created");
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode txt = mapper.createObjectNode();
        txt.put("timestamp", getTimestamp());
        txt.put("description", getDescription());
        return txt;
    }
}
