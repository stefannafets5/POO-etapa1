package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MoneyTransfer extends Transaction {
    private String senderIban;
    private String receiverIban;
    private double amount;
    private String transferType;
    private String currency;

    public MoneyTransfer(int timestamp, String description, String senderIban, String receiverIban,
                                    double amount, String transferType, String currency) {
        super(timestamp, description);
        this.senderIban = senderIban;
        this.receiverIban = receiverIban;
        this.amount = amount;
        this.transferType = transferType;
        this.currency = currency;
    }

    public String getSenderIban() {
        return senderIban;
    }

    public void setSenderIban(String senderIban) {
        this.senderIban = senderIban;
    }

    public String getReceiverIban() {
        return receiverIban;
    }

    public void setReceiverIban(String receiverIban) {
        this.receiverIban = receiverIban;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode txt = mapper.createObjectNode();
        txt.put("timestamp", getTimestamp());
        txt.put("description", getDescription());
        txt.put("senderIBAN", getSenderIban());
        txt.put("receiverIBAN", getReceiverIban());
        txt.put("amount", getAmount() + " " + getCurrency());
        txt.put("transferType", getTransferType());
        return txt;
    }
}
