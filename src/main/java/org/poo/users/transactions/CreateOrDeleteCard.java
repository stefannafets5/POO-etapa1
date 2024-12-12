package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CreateOrDeleteCard extends Transaction {
    private String cardNr;
    private String email;
    private String iban;

    public CreateOrDeleteCard(int timestamp, String email, String cardNr,
                              String iban, String description) {
        super(timestamp, description);
        this.cardNr = cardNr;
        this.email = email;
        this.iban = iban;
        setFromIban(iban);
    }

    public String getCardNr() {
        return cardNr;
    }

    public void setCardNr(String cardNr) {
        this.cardNr = cardNr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode txt = mapper.createObjectNode();
        txt.put("timestamp", getTimestamp());
        txt.put("description", getDescription());
        txt.put("card", getCardNr());
        txt.put("cardHolder", getEmail());
        txt.put("account", getIban());
        return txt;
    }
}
